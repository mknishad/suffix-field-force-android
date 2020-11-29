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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.suffix.fieldforce.R
import com.suffix.fieldforce.akg.adapter.MemoBodyListAdapter
import com.suffix.fieldforce.akg.api.AkgApiClient
import com.suffix.fieldforce.akg.api.AkgApiInterface
import com.suffix.fieldforce.akg.model.InvoiceDetail
import com.suffix.fieldforce.akg.util.AkgPrintService
import com.suffix.fieldforce.databinding.ActivityCheckBinding
import com.suffix.fieldforce.preference.FieldForcePreferences
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
      val memo = "[C]M/S. Style Zone \n" +
          "[C]Pahartoli\n" +
          "[C]01966660507\n" +
          "[C]29/11/2020 21:37 \n" +
          "[C]SR: Md. Shakil\n" +
          "[C]Malek Store\n" +
          "[C]Memo No: 1039304849\n" +
          "[L]\n" +
          "[C]--------------------------------\n" +
          "[L]<b>Brand</b>[C]<b>Q.</b>[R]<b>Tk</b>\n" +
          "[C]--------------------------------\n" +
          "[L]RV-10s--------[C]100--------[R]630.0\n" +
          "[L]RV-20s--------[C]100--------[R]630.0\n" +
          "[L]Rexon---------[C]200--------[R]840.0\n" +
          "[L]MSB-10s-------[C]100--------[R]350.0\n" +
          "[L]MSB-20s-------[C]100--------[R]350.0\n" +
          "[L]Sunmoon-------[C]100--------[R]350.0\n" +
          "[L]SAB-----------[C]100--------[R]71.6\n" +
          "[L]EAB-----------[C]100--------[R]58.0\n" +
          "[L]FB------------[C]120--------[R]70.0\n" +
          "[L]SL-----------[C]420--------[R]367.5\n" +
          "[C]--------------------------------\n" +
          "[L]TOTAL---------------------[R]3817.1\n" +
          "[C]--------------------------------\n" +
          "[L]Price per pack\n" +
          "[L]RV: 10's-63, 20's-126; Rexon: 10's-42; MSB: 10's-35, 20's-70; Sunmoon: 10's-35; SAB: 25's-17.90; EAB: 25's-14.50; FB: 1d-17; SL: 1d-10.50\n" +
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
