package com.suffix.fieldforce.activity

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityAddBillBinding
import com.suffix.fieldforce.model.Bill
import com.suffix.fieldforce.model.BillData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.AddBillViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.snackbar
import java.util.*

class AddBillActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityAddBillBinding
    private lateinit var viewModel: AddBillViewModel

    private lateinit var textInputLayouts: MutableList<TextInputLayout>
    private lateinit var linearLayout: LinearLayout
    private lateinit var preferences: FieldForcePreferences

    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bill)
        viewModel = ViewModelProviders.of(this).get(AddBillViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()
    }

    private fun init() {
        linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        textInputLayouts = mutableListOf()
        preferences = FieldForcePreferences(this)

        setupToolbar()
        observeBillTypes()
        observeBillTypes()
        observeAddBillResponse()
        observeMessage()
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
            if (textInputLayouts.size == 0) {
                addDateLayout()
                addBillTypesLayout(it)
                addRemarksLayout(linearLayout)
                addButton()
            }
        })
    }

    private fun addDateLayout() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_bill_input_layout, null)
        val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
        layout.hint = getString(R.string.date)
        layout.editText?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_calendar_black_24dp,
            0
        )
        layout.editText?.inputType = InputType.TYPE_CLASS_TEXT
        layout.editText?.showSoftInputOnFocus = false
        layout.editText?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePicker()
            }
            return@setOnTouchListener false
        }
        linearLayout.addView(view)
        textInputLayouts.add(layout)
    }

    private fun addBillTypesLayout(billTypes: List<BillType>) {
        for (billType in billTypes) {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.item_bill_input_layout, null)
            val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
            layout.hint = billType.billShortName
            layout.tag = billType.billTypeId
            layout.editText?.setText("123")
            linearLayout.addView(view)
            textInputLayouts.add(layout)
        }
    }

    private fun addRemarksLayout(linearLayout: LinearLayout) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_bill_input_layout, null)
        val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
        layout.hint = getString(R.string.remarks)
        layout.editText?.inputType = InputType.TYPE_CLASS_TEXT
        layout.editText?.setText("abc")
        linearLayout.addView(view)
        textInputLayouts.add(layout)
    }

    private fun addButton() {
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
            val billDataObj = mutableListOf<Bill>()
            for (i in 1 until textInputLayouts.size - 1) {
                val bill = Bill(
                    null,
                    null,
                    null,
                    null,
                    textInputLayouts[i].editText?.text.toString().toDouble(),
                    textInputLayouts[i].tag.toString().toInt()
                )
                billDataObj.add(bill)
            }

            val billData = BillData(
                textInputLayouts[0].editText?.text.toString(),
                billDataObj,
                textInputLayouts[textInputLayouts.size - 1].editText?.text.toString()
            )

            viewModel.submitBill(
                Constants.KEY,
                Constants.USER_ID,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString(),
                billData
            )
        }

        linearLayout.addView(button)
        binding.scrollView.addView(linearLayout)
    }

    private fun observeAddBillResponse() {
        viewModel.addBillResponse.observe(this, Observer {
            if (it.responseCode.equals("1", true)) {
                finish()
            } else {
                binding.scrollView.snackbar(it.responseText)
            }
        })
    }

    private fun observeMessage() {
        viewModel.message.observe(this, Observer { message ->
            message?.let {
                binding.scrollView.snackbar(it)
            }
        })
    }

    private fun showDatePicker() {
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                mYear = year
                mMonth = monthOfYear + 1
                mDay = dayOfMonth
                val date = "$year-${(monthOfYear + 1)}-$dayOfMonth"
                debug("onDateSet: dateOfBirth = $date")
                /*viewModel.dob.value =
                    BuddyUtil.convertToUtc(BuddyUtil.createDate(mYear, mMonth, mDay))*/
                textInputLayouts[0].editText?.setText(date)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
        //viewModel.datePickerShown()
    }
}
