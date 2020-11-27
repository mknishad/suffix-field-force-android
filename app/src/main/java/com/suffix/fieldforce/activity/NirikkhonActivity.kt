package com.suffix.fieldforce.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.levitnudi.legacytableview.LegacyTableView
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityNirikkhonBinding
import com.suffix.fieldforce.viewmodel.NirikkhonViewModel


class NirikkhonActivity : AppCompatActivity() {
  private lateinit var binding: ActivityNirikkhonBinding
  private lateinit var viewModel: NirikkhonViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_nirikkhon)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_nirikkhon)
    viewModel = ViewModelProviders.of(this).get(NirikkhonViewModel::class.java)
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
    LegacyTableView.insertLegacyTitle("Id", "Name", "Age", "Email")
    //set table contents as string arrays
    LegacyTableView.insertLegacyContent(
      "2999010",
      "John Deer",
      "50",
      "john@example.com",
      "332312",
      "Kennedy F",
      "33",
      "ken@example.com",
      "42343243",
      "Java Lover",
      "28",
      "Jlover@example.com",
      "4288383",
      "Mike Tee",
      "22",
      "miket@example.com"
    )

    binding.tableView.setTitle(LegacyTableView.readLegacyTitle())
    binding.tableView.setContent(LegacyTableView.readLegacyContent())
    //binding.tableView.setFooterText(LegacyTableView.readLegacyTitle())

    binding.tableView.setTableFooterTextSize(0)
    //depending on the phone screen size default table scale is 100
    //you can change it using this method
    //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

    //if you want a smaller table, change the padding setting
    binding.tableView.setTablePadding(7)

    //to enable users to zoom in and out:
    //binding.tableView.setZoomEnabled(true)
    //binding.tableView.setShowZoomControls(true)

    binding.tableView.setTitleTextSize(40)
    binding.tableView.setContentTextSize(30)

    //remember to build your table as the last step
    binding.tableView.build()
  }
}
