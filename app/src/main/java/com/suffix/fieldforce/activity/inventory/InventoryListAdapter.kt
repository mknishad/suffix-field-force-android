package com.suffix.fieldforce.activity.inventory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ListItemInventoryBinding
import com.suffix.fieldforce.model.InventoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InventoryListAdapter :
  ListAdapter<InventoryItem, RecyclerView.ViewHolder>(InventoryDiffCallback()) {

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is InventoryViewHolder -> {
        val inventoryItem = getItem(position) as InventoryItem
        holder.bind(inventoryItem)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return InventoryViewHolder.from(parent)
  }

  fun callSubmitList(list: List<InventoryItem>?) {
    val adapterScope = CoroutineScope(Dispatchers.Default)
    adapterScope.launch {
      withContext(Dispatchers.Main) {
        submitList(list)
      }
    }
  }
}

class InventoryViewHolder private constructor(private val binding: ListItemInventoryBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(item: InventoryItem) {
    binding.inventoryItem = item
    binding.executePendingBindings()
  }

  companion object {
    fun from(parent: ViewGroup): InventoryViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ListItemInventoryBinding.inflate(layoutInflater, parent, false)
      return InventoryViewHolder(binding)
    }
  }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class InventoryDiffCallback : DiffUtil.ItemCallback<InventoryItem>() {
  override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
    return oldItem.productInvId == newItem.productInvId
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
    return oldItem == newItem
  }
}
