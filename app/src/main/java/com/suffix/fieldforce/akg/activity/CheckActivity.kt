package com.suffix.fieldforce.akg.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.home.MainDashboardActivity
import com.suffix.fieldforce.akg.adapter.CategoryListAdapter
import com.suffix.fieldforce.akg.adapter.PrintingInterface
import com.suffix.fieldforce.akg.api.AkgApiClient
import com.suffix.fieldforce.akg.api.AkgApiInterface
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager
import com.suffix.fieldforce.akg.database.manager.SyncManager
import com.suffix.fieldforce.akg.model.*
import com.suffix.fieldforce.akg.model.product.CartModel
import com.suffix.fieldforce.akg.util.AkgConstants
import com.suffix.fieldforce.akg.util.AkgPrintingService
import com.suffix.fieldforce.akg.util.NetworkUtils
import com.suffix.fieldforce.databinding.ActivityCheckBinding
import com.suffix.fieldforce.preference.FieldForcePreferences
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_check.*
import okhttp3.Credentials
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CheckActivity : AppCompatActivity() {
  private lateinit var binding: ActivityCheckBinding
  //private lateinit var viewModel: CheckViewModel

  private lateinit var invoiceType: String
  private lateinit var preferences: FieldForcePreferences
  private lateinit var apiInterface: AkgApiInterface
  private lateinit var loginResponse: AkgLoginResponse
  private lateinit var adapter: CategoryListAdapter
  private lateinit var customerData: CustomerData
  private lateinit var products: RealmResults<CartModel>
  private lateinit var invoiceProducts: RealmList<InvoiceProduct>
  private lateinit var invoiceRequest: InvoiceRequest

  private lateinit var progressDialog: ProgressDialog
  private lateinit var alertDialogBuilder: AlertDialog.Builder

  private var invoiceDate = 0L

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_check)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_check)
    //viewModel = ViewModelProviders.of(this).get(CheckViewModel::class.java)
    //binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()
  }

  private fun init() {
    setupToolbar()
    //setupTableView()

    invoiceType = intent.getStringExtra(AkgConstants.INVOICE_TYPE)
    Log.d(TAG, "init: invoiceType = $invoiceType")
    preferences = FieldForcePreferences(this)
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface::class.java)
    loginResponse = Gson().fromJson(preferences.getLoginResponse(), AkgLoginResponse::class.java)
    customerData = intent.getParcelableExtra(AkgConstants.CUSTOMER_INFO)!!

    progressDialog = ProgressDialog(this)
    progressDialog.setMessage("Printing...")

    alertDialogBuilder = AlertDialog.Builder(this)

    binding.storeNameTextView.text = customerData.customerName
    binding.storeAddressTextView.text = customerData.customerName

    setupRecyclerView()
    setupTotal()

    if (invoiceType.equals(AkgConstants.DAMP, true)) {
      receivedAmountLayout.visibility = View.GONE
    }
  }

  private fun setupToolbar() {
    setSupportActionBar(binding.toolbar)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      binding.toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))
    } else {
      binding.toolbar.setTitleTextColor(resources.getColor(android.R.color.white))
    }
    supportActionBar?.setDisplayShowTitleEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setTitle(R.string.nirikkhon)
    binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      binding.toolbar.navigationIcon?.colorFilter =
        BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
    } else {
      binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
  }

  private fun setupRecyclerView() {
    val realm: Realm = Realm.getDefaultInstance()
    products = realm.where(CartModel::class.java).findAll()
    Log.d(TAG, "setupRecyclerView: products = $products")
    adapter = CategoryListAdapter(this, products)
    binding.recyclerView.adapter = adapter
  }

  private fun setupTotal() {
    var quantity = 0
    var totalAmount = 0.0
    products.forEach {
      quantity += it.orderQuantity.toInt()
      totalAmount += (it.orderQuantity.toInt() * it.sellingRate)
    }

    //binding.txtTotalQuantity.text = quantity.toString()
    binding.txtTotalAmount.text = String.format(Locale.getDefault(), "%.2f", totalAmount)
    binding.receivedAmountLayout.editText?.setText(String.format(Locale.getDefault(), "%.2f", totalAmount))
  }

  fun submit(view: View) {
    if (TextUtils.isEmpty(binding.receivedAmountLayout.editText?.text.toString())) {
      Toast.makeText(this, "আদায়কৃত টাকার পরিমাণ লিখুন", Toast.LENGTH_SHORT).show()
      return
    }

    if (invoiceType.equals(AkgConstants.SALE, true)) {
      createInvoice(AkgConstants.INVOICE_TYPE_NORMAL,
        binding.receivedAmountLayout.editText?.text.toString().toDouble())
    } else if (invoiceType.equals(AkgConstants.DAMP, true)) {
      createInvoice(AkgConstants.INVOICE_TYPE_DAMP)
    }
  }

  private fun createInvoice(invoiceType: String, receivedAmount: Double = 0.0) {
    progressDialog.show()
    invoiceProducts = RealmList()
    var totalAmount = 0.0
    products.forEach {
      val invoiceProduct = InvoiceProduct(
        0.0,
        it.productId,
        it.orderQuantity.toInt(),
        it.sellingRate,
        (it.orderQuantity.toDouble() * it.sellingRate),
        it.productCode,
        it.sellingRate
      )

      invoiceProducts.add(invoiceProduct)
      totalAmount += invoiceProduct.subToalAmount

      Log.d(TAG, "submit: $invoiceProduct")
    }

    invoiceDate = System.currentTimeMillis()

    invoiceRequest = InvoiceRequest(invoiceType, customerData.id, invoiceDate,
      customerData.id.toString() + "_" + invoiceDate.toString(), invoiceProducts,
      loginResponse.data.id, totalAmount, customerData.customerName, customerData.address,
      receivedAmount
    )

    val basicAuthorization = Credentials.basic(
      loginResponse.data.userId.toString(),
      preferences.getPassword()
    )

    if (!NetworkUtils.isNetworkConnected(this)) {
      //TODO: save to database
      invoiceRequest.status = false
      SyncManager(this@CheckActivity).insertInvoice(invoiceRequest)
      RealMDatabaseManager().deleteAllCart()
      printMemo()
      //Toast.makeText(this, "Invoice Created!", Toast.LENGTH_SHORT).show()
    } else {
      val call = apiInterface.createInvoice(basicAuthorization, invoiceRequest)
      call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
          Log.d(TAG, "onResponse: response.body() = " + response.body())
          if (response.isSuccessful) {
            //TODO: save to database
            invoiceRequest.status = true
            SyncManager(this@CheckActivity).insertInvoice(invoiceRequest)
            RealMDatabaseManager().deleteAllCart()
            printMemo()
          } else {
            //TODO: save to database
            invoiceRequest.status = false
            SyncManager(this@CheckActivity).insertInvoice(invoiceRequest)
            RealMDatabaseManager().deleteAllCart()
            printMemo()
          }
          //Toast.makeText(this@CheckActivity, "Invoice Created!", Toast.LENGTH_SHORT).show()
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
          Log.e(TAG, "onFailure: ", t)
          //TODO: save to database
          invoiceRequest.status = false
          SyncManager(this@CheckActivity).insertInvoice(invoiceRequest)
          RealMDatabaseManager().deleteAllCart()
          printMemo()
          //Toast.makeText(this@CheckActivity, "Invoice Created!", Toast.LENGTH_SHORT).show()
        }
      })
    }
  }

  fun printMemo() {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.BLUETOOTH
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.BLUETOOTH),
        PERMISSION_BLUETOOTH
      )
    } else {
      val distributor = Gson().fromJson(preferences.getDistributor(), Distributor::class.java)
      Log.d(TAG, "printMemo: distributor = " + preferences.getDistributor())
      AkgPrintingService().print(
        distributor.data.distributorName, distributor.data.mobile,
        loginResponse, invoiceRequest, object : PrintingInterface {
          override fun onPrintFail(message: String?) {
            progressDialog.dismiss()
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
              startActivity<MainDashboardActivity>()
              finishAffinity()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
          }

          override fun onPrintSuccess(message: String?) {
            progressDialog.dismiss()
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
              startActivity<MainDashboardActivity>()
              finishAffinity()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
          }
        }
      )
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      PERMISSION_BLUETOOTH -> {
        // If request is cancelled, the result arrays are empty.
        if ((grantResults.isNotEmpty() &&
              grantResults[0] == PackageManager.PERMISSION_GRANTED)
        ) {
          // Permission is granted. Continue the action or workflow
          // in your app.
          printMemo()
        } else {
          // Explain to the user that the feature is unavailable because
          // the features requires a permission that the user has denied.
          // At the same time, respect the user's decision. Don't link to
          // system settings in an effort to convince the user to change
          // their decision.
        }
        return
      }

      // Add other 'when' lines to check for other
      // permissions this app might request.
      else -> {
        // Ignore all other requests.
      }
    }
  }

  companion object {
    private const val PERMISSION_BLUETOOTH = 1
    private const val TAG = "CheckActivity"
  }
}
