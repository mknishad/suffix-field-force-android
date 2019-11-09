package com.suffix.fieldforce.activity

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityTaskDetailsBinding
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.TaskDetailsViewModel

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding
    private lateinit var viewModel: TaskDetailsViewModel
    private lateinit var taskId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_details)
        viewModel = ViewModelProviders.of(this).get(TaskDetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        setupToolbar()
        //hideSoftKeyboard()
        disableSoftInput()

        taskId = intent.getStringExtra(Constants.TASK_ID)
        viewModel.getTaskDetails(taskId)
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
        supportActionBar?.setTitle(R.string.task_details)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.toolbar.navigationIcon?.colorFilter =
                BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
        } else {
            binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun hideSoftKeyboard() {
        currentFocus.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun disableSoftInput() {
        binding.ticketTitleEditText.showSoftInputOnFocus = false
        binding.consumerNameEditText.showSoftInputOnFocus = false
        binding.consumerMobileEditText.showSoftInputOnFocus = false
        binding.consumerAddressEditText.showSoftInputOnFocus = false
        binding.consumerThanaEditText.showSoftInputOnFocus = false
        binding.consumerDistrictEditText.showSoftInputOnFocus = false
        binding.ticketRemarkEditText.showSoftInputOnFocus = false
        binding.deviceNameEditText.showSoftInputOnFocus = false
        binding.ticketCategoryCodeEditText.showSoftInputOnFocus = false
        binding.ticketStartDateEditText.showSoftInputOnFocus = false
        binding.ticketEndDateEditText.showSoftInputOnFocus = false
        binding.ticketStatusEditText.showSoftInputOnFocus = false
        binding.ticketStatusTextEditText.showSoftInputOnFocus = false
        binding.ticketCategoryTitleEditText.showSoftInputOnFocus = false
        binding.ticketCodeEditText.showSoftInputOnFocus = false
    }
}
