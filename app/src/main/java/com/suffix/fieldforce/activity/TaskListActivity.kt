package com.suffix.fieldforce.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.adapter.TaskAdapter
import com.suffix.fieldforce.adapter.TaskListener
import com.suffix.fieldforce.databinding.ActivityTaskListBinding
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.TaskListViewModel
import org.jetbrains.anko.startActivity

class TaskListActivity : BaseActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var viewModel: TaskListViewModel
    private lateinit var taskType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_list)
        viewModel = ViewModelProviders.of(this).get(TaskListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        setupToolbar()
        //setupRecyclerView()
        observeTaskList()

        taskType = intent.getStringExtra(Constants.TASK_TYPE)
        viewModel.getTaskList(preferences.getUser().userId, taskType)
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

    private fun setupRecyclerView(taskList: List<Task>) {
        val adapter = TaskAdapter(TaskListener { task ->
            startActivity<TaskDetailsActivity>(
                Constants.TASK_ID to task.ticketId
            )
        })
        binding.recyclerView.adapter = adapter

        adapter.callSubmitList(taskList)
    }

    private fun observeTaskList() {
        viewModel.taskList.observe(this, Observer { taskList ->
            taskList.let {
                if (it.isNotEmpty()) {
                    setupRecyclerView(it)
                }
            }
        })
    }
}
