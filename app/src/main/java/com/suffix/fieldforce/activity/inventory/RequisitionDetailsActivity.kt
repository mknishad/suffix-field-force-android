package com.suffix.fieldforce.activity.inventory

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.adapter.BillApproveListAdapter
import com.suffix.fieldforce.databinding.ActivityRequisitionDetailsBinding
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.RequisitionDetailsViewModel
import org.jetbrains.anko.design.snackbar

class RequisitionDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityRequisitionDetailsBinding
  private lateinit var viewModel: RequisitionDetailsViewModel
  private lateinit var adapter: BillApproveListAdapter
  private lateinit var requisitionId: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_requisition_details)
    viewModel = ViewModelProviders.of(this).get(RequisitionDetailsViewModel::class.java)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()
  }

  private fun init() {
    setupToolbar()
    setupRecyclerView()
    requisitionId = intent.getStringExtra(Constants.REQUISITION_ID)
    observeRequisitionDetails()
    observeMessage()
    viewModel.getRequisitionDetails(requisitionId)
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

  private fun observeRequisitionDetails() {
    viewModel.requisitionDetails.observe(this, Observer {
      it.let {
        adapter.callSubmitList(it.billApproveObj.billApproveList)
      }
    })
  }

  private fun observeMessage() {
    viewModel.message.observe(this, Observer { message ->
      message?.let {
        binding.scrollView.snackbar(it)
      }
    })
  }
}
