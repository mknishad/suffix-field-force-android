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
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.TaskDashboardViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

class TaskDashboardActivity : AppCompatActivity(), AnkoLogger {

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
        observeShowAssignedTaskList()
        observeShowAcceptedTaskList()
        observeShowCompletedTaskList()
        observeShowInProgressTaskList()
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

    private fun observeShowAssignedTaskList() {
        viewModel.eventShowAssignedTaskList.observe(this, Observer {
            if (it) {
                startActivity<TaskListActivity>(
                    Constants.ASSIGNED to Constants.TASK_TYPE
                )
                viewModel.assignedTaskListShown()
            }
        })
    }

    private fun observeShowAcceptedTaskList() {
        viewModel.eventShowAcceptedTaskList.observe(this, Observer {
            if (it) {
                startActivity<TaskListActivity>(
                    Constants.ACCEPTED to Constants.TASK_TYPE
                )
                viewModel.assignedTaskListShown()
            }
        })
    }

    private fun observeShowCompletedTaskList() {
        viewModel.eventShowCompletedTaskList.observe(this, Observer {
            if (it) {
                startActivity<TaskListActivity>(
                    Constants.COMPLETED to Constants.TASK_TYPE
                )
                viewModel.assignedTaskListShown()
            }
        })
    }

    private fun observeShowInProgressTaskList() {
        viewModel.eventShowInProgressTaskList.observe(this, Observer {
            if (it) {
                startActivity<TaskListActivity>(
                    Constants.IN_PROGRESS to Constants.TASK_TYPE
                )
                viewModel.assignedTaskListShown()
            }
        })
    }
}
