package com.suffix.fieldforce.activity.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentRequisitionListBinding
import com.suffix.fieldforce.model.Requisition

/**
 * A simple [Fragment] subclass.
 */
class RequisitionListFragment : Fragment() {

  private lateinit var binding: FragmentRequisitionListBinding
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
    binding.lifecycleOwner = this

    init()

    return binding.root
  }

  private fun init() {
    setupRecyclerView()
  }

  private fun setupRecyclerView() {
    adapter = RequisitionListAdapter(RequisitionListener { requisition ->
      Toast.makeText(activity, requisition.description, Toast.LENGTH_SHORT).show()
    })
    binding.recyclerView.adapter = adapter

    val requisitions = listOf(
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P"),
      Requisition("1230", "inventories", 1, "P")
    )

    adapter.callSubmitList(requisitions)
  }
}
