package com.suffix.fieldforce.akg.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.levitnudi.legacytableview.LegacyTableView
import com.levitnudi.legacytableview.LegacyTableView.CENTER
import com.levitnudi.legacytableview.LegacyTableView.ODD
import com.suffix.fieldforce.R
import com.suffix.fieldforce.akg.util.AkgPrintService
import com.suffix.fieldforce.databinding.ActivityCheckBinding
import com.suffix.fieldforce.akg.viewmodel.CheckViewModel


class CheckActivity : AppCompatActivity() {
  private lateinit var binding: ActivityCheckBinding
  private lateinit var viewModel: CheckViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_check)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_check)
    viewModel = ViewModelProviders.of(this).get(CheckViewModel::class.java)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()
  }

  private fun init() {
    setupToolbar()
    setupTableView()
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

  private fun setupTableView() {
    LegacyTableView.insertLegacyTitle("এস,কে,ইউ", "শলাকা", "টাকা")
    //set table contents as string arrays
    LegacyTableView.insertLegacyContent(
      "AGT 1234",
      "123",
      "111",
      "AGT 1234",
      "123",
      "111",
      "AGT 1234",
      "123",
      "111",
      "AGT 1234",
      "123",
      "111",
      "AGT 1234",
      "123",
      "111",
      "AGT 1234",
      "123",
      "111",
      "মোট",
      "7456",
      "666"
    )

    binding.tableView.setTitle(LegacyTableView.readLegacyTitle())
    binding.tableView.setContent(LegacyTableView.readLegacyContent())
    //binding.tableView.setFooterText(LegacyTableView.readLegacyTitle())

    binding.tableView.setHighlight(ODD)
    binding.tableView.setBackgroundOddColor("#999999")

    //depending on the phone screen size default table scale is 100
    //you can change it using this method
    //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

    //if you want a smaller table, change the padding setting
    binding.tableView.setTitleTextAlignment(CENTER)
    binding.tableView.setContentTextAlignment(CENTER)
    binding.tableView.setTablePadding(16)

    binding.tableView.setHeaderBackgroundLinearGradientBOTTOM("#6D7AE0")
    binding.tableView.setHeaderBackgroundLinearGradientTOP("#6D7AE0")

    //to enable users to zoom in and out:
    //binding.tableView.setZoomEnabled(true)
    //binding.tableView.setShowZoomControls(true)

    binding.tableView.setTitleTextSize(40)
    binding.tableView.setContentTextSize(36)

    //remember to build your table as the last step
    binding.tableView.build()
  }

  fun printMemo(view: View) {
    val htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
    "testing, testing...</p></body></html>";

    AkgPrintService().doWebViewPrint(this, htmlDocument)
  }
}
