package com.suffix.fieldforce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ListItemAdvanceBillBinding
import com.suffix.fieldforce.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdvanceBillsAdapter(private val clickListener: BillsListener) :
    ListAdapter<Bill, RecyclerView.ViewHolder>(AdvanceBillsDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdvanceBillsViewHolder -> {
                val billItem = getItem(position) as Bill
                holder.bind(billItem, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdvanceBillsViewHolder.from(parent)
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

class AdvanceBillsViewHolder private constructor(private val binding: ListItemAdvanceBillBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Bill, clickListener: BillsListener) {
        binding.bill = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AdvanceBillsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAdvanceBillBinding.inflate(layoutInflater, parent, false)
            return AdvanceBillsViewHolder(binding)
        }
    }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class AdvanceBillsDiffCallback : DiffUtil.ItemCallback<Bill>() {
    override fun areItemsTheSame(oldItem: Bill, newItem: Bill): Boolean {
        return oldItem.advanceId == newItem.advanceId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Bill, newItem: Bill): Boolean {
        return oldItem == newItem
    }
}

