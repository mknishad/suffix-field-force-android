package com.suffix.fieldforce.akg.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.suffix.fieldforce.R
import com.suffix.fieldforce.akg.adapter.MemoBodyListAdapter
import com.suffix.fieldforce.akg.api.AkgApiClient
import com.suffix.fieldforce.akg.api.AkgApiInterface
import com.suffix.fieldforce.akg.model.InvoiceDetail
import com.suffix.fieldforce.akg.model.product.CategoryModel
import com.suffix.fieldforce.akg.util.AkgPrintService
import com.suffix.fieldforce.databinding.ActivityCheckBinding
import com.suffix.fieldforce.preference.FieldForcePreferences
import io.realm.Realm
import io.realm.RealmResults
import java.util.*


class CheckActivity : AppCompatActivity() {
  private lateinit var binding: ActivityCheckBinding
  //private lateinit var viewModel: CheckViewModel

  private lateinit var preferences: FieldForcePreferences;
  private lateinit var apiInterface: AkgApiInterface;
  private lateinit var memoBodyListAdapter: MemoBodyListAdapter;
  private lateinit var invoiceDetailList: List<InvoiceDetail>;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_check)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_check)
    //viewModel = ViewModelProviders.of(this).get(CheckViewModel::class.java)
    //binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()

    val realm: Realm = Realm.getDefaultInstance();
    val result: RealmResults<CategoryModel> = realm.where(CategoryModel::class.java).findAll();
    Toast.makeText(this@CheckActivity, "Length : "+result.size, Toast.LENGTH_SHORT)
      .show()
  }

  private fun init() {
    setupToolbar()
    //setupTableView()

    preferences = FieldForcePreferences(this)
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface::class.java)
    invoiceDetailList = ArrayList()
    memoBodyListAdapter = MemoBodyListAdapter(this, invoiceDetailList)
    binding.recyclerView.adapter = memoBodyListAdapter
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

  fun printMemo(view: View) {
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
      val memo = "[L]M/S. Style Zone, Pahartoli\n" +
          "[L]01966660507, 29/11/2020 21:37\n" +
          "[L]Memo: 1039304849\n" +
          "[L]SR: Md. Shakil\n" +
          "[L]Malek Store\n" +
          "[L]\n" +
          "[L]<b>Brand</b>[C]<b>Q.</b>[R]<b>Tk</b>\n" +
          "[C]--------------------------------\n" +
          "[L]RV-10s--------[C]100-------[R]630.00\n" +
          "[L]RV-20s--------[C]100-------[R]630.00\n" +
          "[L]Rexon---------[C]200-------[R]840.00\n" +
          "[L]MSB-10s-------[C]100-------[R]350.00\n" +
          "[L]MSB-20s-------[C]100-------[R]350.00\n" +
          "[L]SM------------[C]100-------[R]350.00\n" +
          "[L]SAB-----------[C]100--------[R]71.60\n" +
          "[L]EAB-----------[C]100--------[R]58.00\n" +
          "[L]FB--------------[C]1--------[R]17.00\n" +
          "[L]SL--------------[C]1--------[R]10.50\n" +
          "[C]--------------------------------\n" +
          "[L]TOTAL-------------------[R]3817.10\n\n" +
          "[L]Price per pack:\n" +
          "[L]RV: 10s-63, 20s-126; Rexon: 10s-42; MSB: 10s-35, 20s-70; SM: 10s-35; SAB: 25s-17.90; EAB: 25s-14.50; FB: 1d-17; SL: 1d-10.50\n\n" +
          "[C]Thanks for your purchase!\n"
      AkgPrintService(this).print(memo)
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
              grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          // Permission is granted. Continue the action or workflow
          // in your app.
          //printMemo()
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
    const val PERMISSION_BLUETOOTH = 1
  }
}
