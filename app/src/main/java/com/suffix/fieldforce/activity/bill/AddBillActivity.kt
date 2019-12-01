package com.suffix.fieldforce.activity.bill

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esafirm.imagepicker.features.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.BaseActivity
import com.suffix.fieldforce.databinding.ActivityAddBillBinding
import com.suffix.fieldforce.model.Bill
import com.suffix.fieldforce.model.BillData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.util.Utils
import com.suffix.fieldforce.viewmodel.AddBillViewModel
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.snackbar
import java.io.ByteArrayOutputStream
import java.util.*

class AddBillActivity : BaseActivity() {

    private lateinit var binding: ActivityAddBillBinding
    private lateinit var viewModel: AddBillViewModel

    private lateinit var textInputLayouts: MutableList<TextInputLayout>
    private lateinit var linearLayout: LinearLayout
    private lateinit var spinner: Spinner
    private lateinit var checkBox: CheckBox
    private lateinit var taskId: String

    private var encodedImage = ""

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
        linearLayout = LinearLayout(applicationContext)
        spinner = Spinner(applicationContext)
        checkBox = CheckBox(applicationContext)
        linearLayout.orientation = LinearLayout.VERTICAL
        textInputLayouts = mutableListOf()
        taskId = intent.getStringExtra(Constants.TASK_ID)

        setupToolbar()
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
        supportActionBar?.setTitle(R.string.bill_entry)
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
                addSpinner()
                addDateLayout()
                addImagePickerLayout()
                addBillTypesLayout(it)
                addRemarksLayout()
                addCheckBox()
                addButton()
            }
        })
    }

    private fun addSpinner() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(20, 20, 20, 0)

        val spinnerArray = ArrayList<String>()
        spinnerArray.add("Select Bill Type")
        spinnerArray.add("Expense")
        spinnerArray.add("Advance")

        val spinnerArrayAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item, spinnerArray
        )

        spinner.layoutParams = params
        spinner.setBackgroundResource(R.drawable.bg_spinner)
        spinner.adapter = spinnerArrayAdapter

        linearLayout.addView(spinner)
        binding.scrollView.addView(linearLayout)
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

    private fun addImagePickerLayout() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_bill_input_layout, null)
        val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
        layout.hint = getString(R.string.bill_image)
        layout.editText?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_image_black_24dp,
            0
        )
        layout.editText?.inputType = InputType.TYPE_CLASS_TEXT
        layout.editText?.showSoftInputOnFocus = false
        layout.editText?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                ImagePicker.create(this).start()
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
            linearLayout.addView(view)
            textInputLayouts.add(layout)
        }
    }

    private fun addRemarksLayout() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_bill_input_layout, null)
        val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
        layout.hint = getString(R.string.remarks)
        layout.editText?.inputType = InputType.TYPE_CLASS_TEXT
        linearLayout.addView(view)
        textInputLayouts.add(layout)
    }

    private fun addCheckBox() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 16, 16, 0)

        checkBox.text = getString(R.string.urgent)
        checkBox.textSize = 16f
        checkBox.layoutParams = params
        linearLayout.addView(checkBox)
    }

    private fun addButton() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(20, 20, 20, 20)

        val button = Button(applicationContext)
        button.text = getString(R.string.submit)
        button.textSize = 16f
        button.setTextColor(Color.parseColor("#FFFFFF"))
        button.background = getDrawable(R.drawable.bg_button)
        button.layoutParams = params
        button.setOnClickListener {
            submitBill()
        }

        linearLayout.addView(button)
    }

    private fun submitBill() {
        val billType: String = when (spinner.selectedItemPosition) {
            0 -> {
                spinner.snackbar("Select Bill Type")
                return
            }
            1 -> Constants.EXPENSE
            else -> Constants.ADVANCE
        }

        val date: String

        if (TextUtils.isEmpty(textInputLayouts[0].editText?.text.toString())) {
            linearLayout.snackbar("Select Date")
            return
        } else {
            date = textInputLayouts[0].editText?.text.toString()
        }

        val billDataObj = mutableListOf<Bill>()
        for (i in 3 until textInputLayouts.size - 2) {
            val billAmount: Double =
                if (TextUtils.isEmpty(textInputLayouts[i].editText?.text.toString())) {
                    0.0
                } else {
                    textInputLayouts[i].editText?.text.toString().toDouble()
                }

            val bill = Bill(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                billAmount,
                textInputLayouts[i].tag.toString().toInt(),
                null
            )
            billDataObj.add(bill)
        }

        val billData = BillData(
            date,
            billDataObj,
            textInputLayouts[textInputLayouts.size - 1].editText?.text.toString()
        )

        val priority: String = if (checkBox.isChecked) {
            "1"
        } else {
            "0"
        }

        if (billType.equals(Constants.EXPENSE, true)) {
            viewModel.submitBillWithAdvanceId(
                Constants.KEY,
                preferences.getUser().userId,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString(),
                billData,
                encodedImage,
                taskId,
                priority
            )
        } else {
            viewModel.submitAdvanceBill(
                Constants.KEY,
                preferences.getUser().userId,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString(),
                billData,
                encodedImage,
                taskId,
                priority
            )
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val images = ImagePicker.getImages(data)
            val image = ImagePicker.getFirstImageOrNull(data)
            textInputLayouts[1].editText?.setText(image.path)

            val bitmap = BitmapFactory.decodeFile(image.path)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            val byteArray = baos.toByteArray()

            //encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

            encodedImage = Utils.encodeToBase64(byteArray)
            debug("encodedImage = $encodedImage")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
