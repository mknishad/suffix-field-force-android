package com.suffix.fieldforce.activity

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
import com.suffix.fieldforce.adapter.BillsAdapter
import com.suffix.fieldforce.adapter.BillsListener
import com.suffix.fieldforce.databinding.ActivityBillsBinding
import com.suffix.fieldforce.viewmodel.BillsViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity

class BillsActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityBillsBinding
    private lateinit var viewModel: BillsViewModel
    private lateinit var adapter: BillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BillsViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bills)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        adapter = BillsAdapter(BillsListener { bill ->
            startActivity<BillDetailsActivity>(
                "billId" to bill.billId
            )
        })
        binding.recyclerView.adapter = adapter

        setupToolbar()
        //setupRecyclerView()
        observeBillsDashboard()
        observeShowAddBills()
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

    private fun observeShowAddBills() {
        viewModel.eventShowAddBills.observe(this, Observer {
            if (it) {
                startActivity<AddBillActivity>()
                viewModel.addBillsShown()
            }
        })
    }

    private fun observeBillsDashboard() {
        viewModel.billsDashboard.observe(this, Observer {dashboard ->
            dashboard?.let {
                adapter.callSubmitList(it.billListObj.bills)
            }
        })
    }
}
