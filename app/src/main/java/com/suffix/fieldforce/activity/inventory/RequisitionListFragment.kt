package com.suffix.fieldforce.activity.inventory


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentRequisitionListBinding
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.RequisitionListViewModel
import org.jetbrains.anko.design.snackbar

/**
 * A simple [Fragment] subclass.
 */
class RequisitionListFragment : Fragment() {

  private lateinit var binding: FragmentRequisitionListBinding
  private lateinit var viewModel: RequisitionListViewModel
  private lateinit var adapter: RequisitionListAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_requisition_list,
      container,
      false
    )
    viewModel = ViewModelProviders.of(activity!!).get(RequisitionListViewModel::class.java)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()

    return binding.root
  }

  private fun init() {
    setupRecyclerView()
    observeRequisitionList()
    observeMessage()
  }

  private fun setupRecyclerView() {
    adapter = RequisitionListAdapter(RequisitionListener { requisition ->
      val detailsIntent = Intent(context, RequisitionDetailsActivity::class.java)
      detailsIntent.putExtra(Constants.REQUISITION_ID, requisition.itemRequisitionId.toString())
      startActivity(detailsIntent)
    })
    binding.recyclerView.adapter = adapter
  }

  private fun observeRequisitionList() {
    viewModel.requisitionList.observe(this, Observer {
      it.let {
        adapter.callSubmitList(it)
      }
    })
  }

  private fun observeMessage() {
    viewModel.message.observe(this, Observer { message ->
      message.let {
        binding.recyclerView.snackbar(message)
      }
    })
  }
}
