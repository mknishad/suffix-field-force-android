package com.suffix.fieldforce.activity.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentInventoryListBinding
import com.suffix.fieldforce.model.Inventory

/**
 * A simple [Fragment] subclass.
 */
class InventoryListFragment : Fragment() {

  private lateinit var binding: FragmentInventoryListBinding
  private lateinit var adapter: InventoryListAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_inventory_list,
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
    adapter = InventoryListAdapter()
    binding.recyclerView.adapter = adapter

    val inventories = listOf(
      Inventory("10", "Cable", "50"),
      Inventory("11", "Cable", "50"),
      Inventory("12", "Cable", "50"),
      Inventory("13", "Cable", "50"),
      Inventory("14", "Cable", "50"),
      Inventory("15", "Cable", "50"),
      Inventory("16", "Cable", "50"),
      Inventory("17", "Cable", "50"),
      Inventory("18", "Cable", "50"),
      Inventory("19", "Cable", "50"),
      Inventory("20", "Cable", "50"),
      Inventory("21", "Cable", "50"),
      Inventory("22", "Cable", "50")
    )

    adapter.callSubmitList(inventories)
  }
}
