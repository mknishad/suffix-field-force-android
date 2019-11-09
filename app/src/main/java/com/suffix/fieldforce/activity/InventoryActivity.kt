package com.suffix.fieldforce.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityInventoryBinding
import com.suffix.fieldforce.viewmodel.InventoryViewModel
import org.jetbrains.anko.toast

class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private lateinit var viewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory)
        viewModel = ViewModelProviders.of(this).get(InventoryViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        setupToolbar()
        observeShowPcEvent()
        observeShowMonitorEvent()
        observeShowRouterEvent()
        observeShowSpeakerEvent()
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

    private fun observeShowPcEvent() {
        viewModel.eventShowPC.observe(this, Observer {
            if (it) {
                toast("PC")
                viewModel.pcShown()
            }
        })
    }

    private fun observeShowMonitorEvent() {
        viewModel.eventShowMonitor.observe(this, Observer {
            if (it) {
                toast("Monitor")
                viewModel.monitorShown()
            }
        })
    }

    private fun observeShowRouterEvent() {
        viewModel.eventShowRouter.observe(this, Observer {
            if (it) {
                toast("Router")
                viewModel.routerShown()
            }
        })
    }

    private fun observeShowSpeakerEvent() {
        viewModel.eventShowSpeaker.observe(this, Observer {
            if (it) {
                toast("Speaker")
                viewModel.speakerShown()
            }
        })
    }
}
