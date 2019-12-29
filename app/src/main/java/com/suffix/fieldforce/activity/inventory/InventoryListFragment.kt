package com.suffix.fieldforce.activity.inventory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.suffix.fieldforce.R
import com.suffix.fieldforce.databinding.FragmentInventoryListBinding
import com.suffix.fieldforce.viewmodel.InventoryListViewModel
import org.jetbrains.anko.design.snackbar

/**
 * A simple [Fragment] subclass.
 */
class InventoryListFragment : Fragment() {

  private lateinit var binding: FragmentInventoryListBinding
  private lateinit var viewModel: InventoryListViewModel
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
    viewModel = ViewModelProviders.of(activity!!).get(InventoryListViewModel::class.java)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    init()

    return binding.root
  }

  private fun init() {
    setupRecyclerView()
    observeInventoryList()
    observeMessage()
  }

  private fun setupRecyclerView() {
    adapter = InventoryListAdapter()
    binding.recyclerView.adapter = adapter
  }

  private fun observeInventoryList() {
    viewModel.inventoryList.observe(this, Observer {
      it.let {
        adapter.callSubmitList(it)
      }
    })
  }

  private fun observeMessage() {
    viewModel.message.observe(this, Observer {
      it.let {
        binding.recyclerView.snackbar(it)
      }
    })
  }
}
