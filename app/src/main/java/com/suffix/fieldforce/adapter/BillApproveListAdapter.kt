package com.suffix.fieldforce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ItemBillApproveBinding
import com.suffix.fieldforce.model.BillApprove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillApproveListAdapter() :
    ListAdapter<BillApprove, RecyclerView.ViewHolder>(BillApproveDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BillApproveViewHolder -> {
                val billApproveItem = getItem(position) as BillApprove
                holder.bind(billApproveItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BillApproveViewHolder.from(parent)
    }

    fun callSubmitList(list: List<BillApprove>?) {
        val adapterScope = CoroutineScope(Dispatchers.Default)
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}

class BillApproveViewHolder private constructor(private val binding: ItemBillApproveBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BillApprove) {
        binding.billApprove = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): BillApproveViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBillApproveBinding.inflate(layoutInflater, parent, false)
            return BillApproveViewHolder(binding)
        }
    }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class BillApproveDiffCallback : DiffUtil.ItemCallback<BillApprove>() {
    override fun areItemsTheSame(oldItem: BillApprove, newItem: BillApprove): Boolean {
        return oldItem.billApproveId == newItem.billApproveId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BillApprove, newItem: BillApprove): Boolean {
        return oldItem == newItem
    }
}
