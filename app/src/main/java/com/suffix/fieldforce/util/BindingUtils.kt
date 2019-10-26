package com.suffix.fieldforce.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.suffix.fieldforce.model.Bill
import com.suffix.fieldforce.model.Task
import kotlinx.android.synthetic.main.list_item_task.view.*

@BindingAdapter("taskBackgroundTint")
fun TextView.setTaskBackgroundTint(task: Task) {
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

@BindingAdapter("billBackgroundTint")
fun TextView.setBillBackgroundTint(bill: Bill) {
    bill.let { bill ->
        when (bill.status) {
            "R" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_red_dark)
            )
            "A" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_blue_dark)
            )
            "D" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.holo_green_dark)
            )
            "P" -> statusTextView.setBackgroundColor(
                resources.getColor(android.R.color.darker_gray)
            )
        }
    }
}

@BindingAdapter("billStatus")
fun TextView.setBillStatus(status: String) {
    status.let {
        when (status) {
            "R" -> statusTextView.text = "Rejected"
            "A" -> statusTextView.text = "Approved"
            "D" -> statusTextView.text = "Disbursed"
            "P" -> statusTextView.text = "Pending"
        }
    }
}
