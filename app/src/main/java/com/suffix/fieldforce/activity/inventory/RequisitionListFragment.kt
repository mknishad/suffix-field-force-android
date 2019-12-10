package com.suffix.fieldforce.activity.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentRequisitionListBinding
import com.suffix.fieldforce.model.Inventory
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
    adapter = RequisitionListAdapter()
    binding.recyclerView.adapter = adapter

    val inventories = listOf(
      Inventory("10", "Cable", "50"),
      Inventory("11", "Cable", "50"),
      Inventory("12", "Cable", "50"),
      Inventory("13", "Cable", "50"),
      Inventory("14", "Cable", "50"),
      Inventory("15", "Cable", "50")
    )

    val requisitions = listOf(
      Requisition("1230", inventories, "Approved"),
      Requisition("1231", inventories, "Approved"),
      Requisition("1232", inventories, "Approved"),
      Requisition("1233", inventories, "Approved"),
      Requisition("1234", inventories, "Approved"),
      Requisition("1235", inventories, "Approved"),
      Requisition("1236", inventories, "Approved"),
      Requisition("1237", inventories, "Approved"),
      Requisition("1238", inventories, "Approved"),
      Requisition("1239", inventories, "Approved"),
      Requisition("1240", inventories, "Approved"),
      Requisition("1231", inventories, "Approved"),
      Requisition("1232", inventories, "Approved"),
      Requisition("1233", inventories, "Approved")
    )

    adapter.callSubmitList(requisitions)
  }
}
