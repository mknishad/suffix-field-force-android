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
import com.suffix.fieldforce.adapter.TaskAdapter
import com.suffix.fieldforce.adapter.TaskListener
import com.suffix.fieldforce.databinding.ActivityBillsBinding
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.viewmodel.BillsViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BillsActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityBillsBinding
    private lateinit var viewModel: BillsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bills)
        viewModel = ViewModelProviders.of(this).get(BillsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        setupToolbar()
        setupRecyclerView()
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

    private fun setupRecyclerView() {
        val adapter = TaskAdapter(TaskListener { task ->
            toast(task.name.toString())
        })
        binding.recyclerView.adapter = adapter

        val taskList = listOf(
            Task(";alskf;ls", "Task 1", "Open", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 2", "In Progress", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 3", "Resolved", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 4", "Closed", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 5", "Open", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 6", "In Progress", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 7", "Resolved", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 8", "Closed", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 9", "Open", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 10", "In Progress", "Jul 1, 2020", "Project 1"),
            Task(";alskf;ls", "Task 11", "Resolved", "Jul 1, 2020", "Project 1"))

        adapter.callSubmitList(taskList)
    }

    private fun observeShowAddBills() {
        viewModel.eventShowAddBills.observe(this, Observer {
            if (it) {
                startActivity<AddBillActivity>()
                viewModel.addBillsShown()
            }
        })
    }
}
