package com.suffix.fieldforce.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.bill.BillsActivity
import com.suffix.fieldforce.activity.InventoryActivity
import com.suffix.fieldforce.activity.TaskDashboardActivity
import com.suffix.fieldforce.databinding.FragmentHomeBinding
import com.suffix.fieldforce.viewmodel.HomeViewModel
import org.jetbrains.anko.startActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()

        return binding.root
    }

    private fun init() {
        observeShowTaskEvent()
        observeShowInventoryEvent()
        observeShowBillsEvent()
    }

    private fun observeShowTaskEvent() {
        viewModel.eventNavigateToTask.observe(this, Observer {
            if (it) {
                activity?.startActivity<TaskDashboardActivity>()
                viewModel.taskShown()
            }
        })
    }

    private fun observeShowInventoryEvent() {
        viewModel.eventNavigateToInventory.observe(this, Observer {
            if (it) {
                activity?.startActivity<InventoryActivity>()
                viewModel.inventoryShown()
            }
        })
    }

    private fun observeShowBillsEvent() {
        viewModel.eventNavigateToBills.observe(this, Observer {
            if (it) {
                activity?.startActivity<BillsActivity>()
                viewModel.billsShown()
            }
        })
    }
}
