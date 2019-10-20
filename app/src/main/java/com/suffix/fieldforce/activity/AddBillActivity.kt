package com.suffix.fieldforce.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.ActivityAddBillBinding

class AddBillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBillBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bill)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_bill, null)
        val layout = view.findViewById(R.id.layoutAmount) as TextInputLayout
        layout.hint = "Mobile Bill"
        binding.scrollView.addView(view)
    }
}
