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
import com.suffix.fieldforce.databinding.FragmentInventoryListBinding
import com.suffix.fieldforce.model.InventoryItem
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.InventoryListViewModel
import org.jetbrains.anko.design.snackbar

/**
 * A simple [Fragment] subclass.
 */
class InventoryListFragment : Fragment() {

  private lateinit var binding: FragmentInventoryListBinding
  private lateinit var viewModel: InventoryListViewModel
  private lateinit var adapter: InventoryListAdapter

  private var inventoryList = ArrayList<InventoryItem>()

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
    observeOpenCreateRequisition()
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
        inventoryList = ArrayList(it)
      }
    })
  }

  private fun observeOpenCreateRequisition() {
    viewModel.eventOpenCreateRequisition.observe(this, Observer {
      if (it) {
        if (inventoryList.isNotEmpty()) {
          val requisitionIntent = Intent(context, CreateRequisitionActivity::class.java)
          requisitionIntent.putExtra(Constants.INVENTORY_LIST, inventoryList)
          startActivity(requisitionIntent)
        }
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
