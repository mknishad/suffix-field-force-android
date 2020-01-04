package com.suffix.fieldforce.activity.inventory

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.BaseActivity
import com.suffix.fieldforce.databinding.ActivityCreateRequisitionBinding
import com.suffix.fieldforce.model.*
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.CreateRequisitionViewModel
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.snackbar
import java.util.*
import kotlin.collections.ArrayList

class CreateRequisitionActivity : BaseActivity() {

  private lateinit var binding: ActivityCreateRequisitionBinding
  private lateinit var viewModel: CreateRequisitionViewModel

  private lateinit var textInputLayouts2: MutableList<TextInputLayout>
  private lateinit var linearLayout2: LinearLayout

  private lateinit var textInputLayouts1: MutableList<TextInputLayout>
  private lateinit var linearLayout1: LinearLayout

  private var inventoryList = ArrayList<InventoryItem>()

  private var mDay: Int = 0
  private var mMonth: Int = 0
  private var mYear: Int = 0

  private var taskIdNumber = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_create_requisition)
    viewModel = ViewModelProviders.of(this).get(CreateRequisitionViewModel::class.java)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()
  }

  private fun init() {
    linearLayout1 = LinearLayout(applicationContext)
    linearLayout2 = LinearLayout(applicationContext)
    linearLayout1.orientation = LinearLayout.VERTICAL
    linearLayout2.orientation = LinearLayout.VERTICAL
    textInputLayouts1 = mutableListOf()
    textInputLayouts2 = mutableListOf()
    inventoryList = intent.getParcelableArrayListExtra(Constants.INVENTORY_LIST)

    setupToolbar()
    addTaskIdLayout()
    addDateLayout()
    addInventoryTypesLayout()
    addRemarksLayout()
    addButton()
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
    supportActionBar?.setTitle(R.string.create_requisition)
    binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      binding.toolbar.navigationIcon?.colorFilter =
        BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
    } else {
      binding.toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
  }

  fun addTaskIdLayout() {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.item_bill_input_layout, null)
    val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
    layout.hint = "Task Id"
    linearLayout1.addView(view)
    textInputLayouts1.add(layout)
    binding.scrollView1.addView(linearLayout1)
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
    linearLayout2.addView(view)
    textInputLayouts2.add(layout)
    binding.scrollView2.addView(linearLayout2)
  }

  private fun addInventoryTypesLayout() {
    for (inventory in inventoryList) {
      val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      val view = inflater.inflate(R.layout.item_bill_input_layout, null)
      val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
      layout.hint = inventory.productName
      layout.tag = inventory.productInvId
      linearLayout2.addView(view)
      textInputLayouts2.add(layout)
    }
  }

  private fun addRemarksLayout() {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.item_bill_input_layout, null)
    val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
    layout.hint = getString(R.string.remarks)
    layout.editText?.inputType = InputType.TYPE_CLASS_TEXT
    linearLayout2.addView(view)
    textInputLayouts2.add(layout)
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
      //submitBill()
    }

    linearLayout2.addView(button)
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
        textInputLayouts2[0].editText?.setText(date)
      }, mYear, mMonth, mDay
    )
    datePickerDialog.show()
    //viewModel.datePickerShown()
  }

  fun addAnotherTaskLayout(view: View) {
    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.item_bill_input_layout, null)
    val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
    layout.hint = "Task Id"
    linearLayout1.addView(view)
    textInputLayouts1.add(layout)
    taskIdNumber++
  }

  private fun submitRequisition() {
    var taskIdObjList = mutableListOf<TaskIdObj>()
    var taskIdData: TaskIdData
    var date: String

    if (TextUtils.isEmpty(textInputLayouts1[0].editText?.text.toString())) {
      linearLayout1.snackbar("Enter Task Id")
      return
    } else {
      for (i in 0 until taskIdNumber) {
        if (!TextUtils.isEmpty(textInputLayouts1[i].editText?.text.toString())) {
          taskIdObjList.add(TaskIdObj(textInputLayouts1[i].editText?.text.toString().toInt()))
        }
      }
      var taskIdData = TaskIdData(taskIdObjList)
    }

    if (TextUtils.isEmpty(textInputLayouts2[0].editText?.text.toString())) {
      linearLayout2.snackbar("Select Date")
      return
    } else {
      date = textInputLayouts2[0].editText?.text.toString()
    }

    var itemRequisitionDataObjList = mutableListOf<ItemRequisitionDataObj>()
    for (i in 1 until textInputLayouts2.size - 2) {
      val itemRequisitionDataObj =
        if (TextUtils.isEmpty(textInputLayouts2[i].editText?.text.toString())) {
          ItemRequisitionDataObj(
            0,
            textInputLayouts2[i].editText?.tag.toString().toInt()
          )
        } else {
          ItemRequisitionDataObj(
            textInputLayouts2[i].editText?.text.toString().toInt(),
            textInputLayouts2[i].editText?.tag.toString().toInt()
          )
        }
      itemRequisitionDataObjList.add(itemRequisitionDataObj)
    }

    val itemRequisitionData = ItemRequisitionData(
      date,
      itemRequisitionDataObjList,
      textInputLayouts2[textInputLayouts2.size - 1].editText?.text.toString()
    )
  }

  /*private fun submitBill() {
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
  }*/
}
