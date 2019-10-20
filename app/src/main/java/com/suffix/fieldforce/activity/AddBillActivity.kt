package com.suffix.fieldforce.activity

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityAddBillBinding
import com.suffix.fieldforce.viewmodel.AddBillViewModel

class AddBillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBillBinding
    private lateinit var viewModel: AddBillViewModel

    private lateinit var textInputLayouts: MutableList<TextInputLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bill)
        viewModel = ViewModelProviders.of(this).get(AddBillViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        textInputLayouts = mutableListOf()

        setupToolbar()
        observeBillTypes()
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
        supportActionBar?.setTitle(R.string.add_bill)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.toolbar.navigationIcon?.colorFilter =
                BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
        } else {
            binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun observeBillTypes() {
        viewModel.billTypes.observe(this, Observer {
            it.let {
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.VERTICAL
                for (billType in it) {
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.item_bill, null)
                    val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
                    layout.hint = billType.billShortName
                    linearLayout.addView(view)
                    textInputLayouts.add(layout)
                }
                addButton(linearLayout)
                binding.scrollView.addView(linearLayout)
            }
        })
    }

    private fun addButton(linearLayout: LinearLayout) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(20, 20, 20, 20)

        val button = Button(this)
        button.text = getString(R.string.submit)
        button.textSize = 20f
        button.setTextColor(Color.parseColor("#FFFFFF"))
        button.background = getDrawable(R.drawable.bg_button)
        button.layoutParams = params
        button.setOnClickListener {
            viewModel.submitBill()
        }

        linearLayout.addView(button)
    }
}
