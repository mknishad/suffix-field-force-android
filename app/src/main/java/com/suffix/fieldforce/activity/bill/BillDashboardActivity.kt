package com.suffix.fieldforce.activity.bill

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.BaseActivity
import com.suffix.fieldforce.adapter.CategoryAdapter
import com.suffix.fieldforce.databinding.ActivityBillDashboardBinding
import com.suffix.fieldforce.util.Constants
import org.jetbrains.anko.startActivity

class BillDashboardActivity : BaseActivity() {

  private lateinit var binding: ActivityBillDashboardBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_dashboard)
    binding.lifecycleOwner = this

    init()
  }

  private fun init() {
    setupToolbar()
    setupViewPager()
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
    supportActionBar?.setTitle(R.string.bills)
    binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      binding.toolbar.navigationIcon?.colorFilter =
        BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
    } else {
      binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
  }

  private fun setupViewPager() {
    val adapter = CategoryAdapter(this, supportFragmentManager)
    binding.viewPager.adapter = adapter
    binding.tabLayout.setupWithViewPager(binding.viewPager)
  }

  fun showAddBills(v: View) {
    startActivity<AddBillActivity>(
      Constants.TASK_ID to "149"
    )
  }
}
