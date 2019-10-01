package com.suffix.fieldforce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suffix.fieldforce.databinding.ListItemTaskBinding
import com.suffix.fieldforce.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAdapter(val clickListener: TaskListener) :
    ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val taskItem = getItem(position) as Task
                holder.bind(taskItem, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    fun callSubmitList(list: List<Task>?) {
        val adapterScope = CoroutineScope(Dispatchers.Default)
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}

class ViewHolder private constructor(val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Task, clickListener: TaskListener) {
        binding.task = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }
}


/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

class TaskListener(val clickListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = clickListener(task)
}
