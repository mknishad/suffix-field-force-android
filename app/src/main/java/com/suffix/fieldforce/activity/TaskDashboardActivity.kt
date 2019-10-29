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
import com.suffix.fieldforce.databinding.ActivityTaskDashboardBinding
import com.suffix.fieldforce.viewmodel.TaskDashboardViewModel
import org.jetbrains.anko.design.snackbar

class TaskDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDashboardBinding
    private lateinit var viewModel: TaskDashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_dashboard)
        viewModel = ViewModelProviders.of(this).get(TaskDashboardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        setupToolbar()
        observeMessage()
        //observeTaskDashboard()
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
        supportActionBar?.setTitle(R.string.task)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.toolbar.navigationIcon?.colorFilter =
                BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
        } else {
            binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun observeMessage() {
        viewModel.message.observe(this, Observer { message ->
            message.let {
                binding.scrollView.snackbar(it)
            }
        })
    }

    private fun observeTaskDashboard() {
        viewModel.taskDashboard.observe(this, Observer { taskDashboard ->
            taskDashboard.let {
                binding.vAssign.text = it.acceptedTicketCount
                binding.vAccept.text = it.acceptedTicketCount
                binding.vComplete.text = it.completedTicketCount
                binding.vProgress.text = it.assignedTicketCount
            }
        })
    }
}
