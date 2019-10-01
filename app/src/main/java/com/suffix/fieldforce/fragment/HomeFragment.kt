package com.suffix.fieldforce.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.InventoryActivity
import com.suffix.fieldforce.activity.TaskListActivity
import com.suffix.fieldforce.databinding.FragmentHomeBinding
import com.suffix.fieldforce.viewmodel.HomeViewModel

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

        init()

        return binding.root
    }

    private fun init() {
        observeShowTaskEvent()
        observeShowInventoryEvent()
    }

    private fun observeShowTaskEvent() {
        viewModel.eventNavigateToTask.observe(this, Observer {
            if (it) {
                val taskListIntent = Intent(context, TaskListActivity::class.java)
                startActivity(taskListIntent)
                viewModel.taskShown()
            }
        })
    }

    private fun observeShowInventoryEvent() {
        viewModel.eventNavigateToInventory.observe(this, Observer {
            if (it) {
                val inventoryIntent = Intent(context, InventoryActivity::class.java)
                startActivity(inventoryIntent)
                viewModel.inventoryShown()
            }
        })
    }
}
