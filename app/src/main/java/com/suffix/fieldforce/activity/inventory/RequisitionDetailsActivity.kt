package com.suffix.fieldforce.activity.inventory

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suffix.fieldforce.R
import com.suffix.fieldforce.adapter.BillApproveListAdapter
import com.suffix.fieldforce.databinding.ActivityRequisitionDetailsBinding

class RequisitionDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRequisitionDetailsBinding
  private lateinit var adapter: BillApproveListAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_requisition_details)

    init()
  }

  private fun init() {
    setupToolbar()
    setupRecyclerView()
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
    supportActionBar?.setTitle(R.string.requisition_details)
    binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      binding.toolbar.navigationIcon?.colorFilter =
        BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
    } else {
      binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
  }

  private fun setupRecyclerView() {
    adapter = BillApproveListAdapter()
    binding.recyclerView.adapter = adapter
  }
}
