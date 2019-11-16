package com.suffix.fieldforce.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentStandardBillBinding
import com.suffix.fieldforce.viewmodel.StandardBillViewModel

/**
 * A simple [Fragment] subclass.
 */
class StandardBillFragment : Fragment() {

    private lateinit var binding: FragmentStandardBillBinding
    private lateinit var viewModel: StandardBillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_standard_bill, container,
            false)
        viewModel = ViewModelProviders.of(this).get(StandardBillViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        init()

        return binding.root
    }

    private fun init() {

    }
}
