package com.suffix.fieldforce.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.suffix.fieldforce.model.Task
import kotlinx.android.synthetic.main.list_item_task.view.*

@BindingAdapter("backgroundTint")
fun TextView.setBackgroundTint(task: Task) {
    task.let { task ->
        when (task.status) {
            "Open" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_red_dark)
            )
            "In Progress" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_blue_dark)
            )
            "Resolved" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_green_dark)
            )
            "Closed" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.darker_gray)
            )
        }
    }
}
