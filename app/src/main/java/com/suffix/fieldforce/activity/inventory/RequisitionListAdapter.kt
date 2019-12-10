package com.suffix.fieldforce.activity.inventory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ListItemRequisitionBinding
import com.suffix.fieldforce.model.Requisition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequisitionListAdapter() :
  ListAdapter<Requisition, RecyclerView.ViewHolder>(RequisitionDiffCallback()) {

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is RequisitionViewHolder -> {
        val inventoryItem = getItem(position) as Requisition
        holder.bind(inventoryItem)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return RequisitionViewHolder.from(parent)
  }

  fun callSubmitList(list: List<Requisition>?) {
    val adapterScope = CoroutineScope(Dispatchers.Default)
    adapterScope.launch {
      withContext(Dispatchers.Main) {
        submitList(list)
      }
    }
  }
}

class RequisitionViewHolder private constructor(private val binding: ListItemRequisitionBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(item: Requisition) {
    binding.requisition = item
    binding.executePendingBindings()
  }

  companion object {
    fun from(parent: ViewGroup): RequisitionViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = ListItemRequisitionBinding.inflate(layoutInflater, parent, false)
      return RequisitionViewHolder(binding)
    }
  }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class RequisitionDiffCallback : DiffUtil.ItemCallback<Requisition>() {
  override fun areItemsTheSame(oldItem: Requisition, newItem: Requisition): Boolean {
    return oldItem.id == newItem.id
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: Requisition, newItem: Requisition): Boolean {
    return oldItem == newItem
  }
}

