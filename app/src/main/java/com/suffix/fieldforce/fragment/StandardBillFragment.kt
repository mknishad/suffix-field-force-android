package com.suffix.fieldforce.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.bill.BillDetailsActivity
import com.suffix.fieldforce.adapter.BillsAdapter
import com.suffix.fieldforce.adapter.BillsListener
import com.suffix.fieldforce.databinding.FragmentStandardBillBinding
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.StandardBillViewModel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

/**
 * A simple [Fragment] subclass.
 */
class StandardBillFragment : Fragment() {

    private lateinit var binding: FragmentStandardBillBinding
    private lateinit var viewModel: StandardBillViewModel
    private lateinit var adapter: BillsAdapter

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
        adapter = BillsAdapter(BillsListener { bill ->
            activity?.startActivity<BillDetailsActivity>(
                Constants.BILL_ID to bill.billId
            )
        })
        binding.recyclerView.adapter = adapter

        observeBillsDashboard()
        observeMessage()
    }

    private fun observeBillsDashboard() {
        viewModel.billsDashboard.observe(this, Observer {dashboard ->
            dashboard?.let {
                adapter.callSubmitList(it.billListObj.bills)
            }
        })
    }

    private fun observeMessage() {
        viewModel.message.observe(this, Observer { message ->
            message?.let {
                binding.recyclerView.snackbar(it)
            }
        })
    }
}
