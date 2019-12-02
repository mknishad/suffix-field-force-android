package com.suffix.fieldforce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ListItemBillBinding
import com.suffix.fieldforce.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillsAdapter(private val clickListener: BillsListener) :
  ListAdapter<Bill, RecyclerView.ViewHolder>(BillsDiffCallback()) {

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is BillsViewHolder -> {
        val billItem = getItem(position) as Bill
        holder.bind(billItem, clickListener)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return BillsViewHolder.from(parent)
  }

  fun callSubmitList(list: List<Bill>?) {
    val adapterScope = CoroutineScope(Dispatchers.Default)
    adapterScope.launch {
      withContext(Dispatchers.Main) {
        submitList(list)
      }
    }
  }
}

class BillsViewHolder private constructor(private val binding: ListItemBillBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(item: Bill, clickListener: BillsListener) {
    binding.bill = item
    binding.clickListener = clickListener
    binding.executePendingBindings()
  }

  companion object {
    fun from(parent: ViewGroup): BillsViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ListItemBillBinding.inflate(layoutInflater, parent, false)
      return BillsViewHolder(binding)
    }
  }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class BillsDiffCallback : DiffUtil.ItemCallback<Bill>() {
  override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
    return oldItem.billId == newItem.billId
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
    return oldItem == newItem
  }
}

class BillsListener(val clickListener: (bill: Bill) -> Unit) {
  fun onClick(bill: Bill) = clickListener(bill)
}
