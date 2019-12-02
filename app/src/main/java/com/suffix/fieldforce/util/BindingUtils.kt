package com.suffix.fieldforce.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.suffix.fieldforce.R
import kotlinx.android.synthetic.main.list_item_task.view.*

@BindingAdapter("taskBackgroundTint")
fun TextView.setTaskBackgroundTint(status: String) {
  status.let {
    when (it) {
      "Assigned" -> statusTextView.setBackgroundColor(
        resources.getColor(android.R.color.holo_red_dark)
      )
      "Accepted" -> statusTextView.setBackgroundColor(
        resources.getColor(android.R.color.holo_blue_dark)
      )
      "Completed" -> statusTextView.setBackgroundColor(
        resources.getColor(android.R.color.holo_green_dark)
      )
      "In Progress" -> statusTextView.setBackgroundColor(
        resources.getColor(android.R.color.darker_gray)
      )
    }
  }
}

@BindingAdapter("billBackgroundTint")
fun TextView.setBillBackgroundTint(status: String) {
  status.let {
    when (it) {
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
      "R" -> {
        statusTextView.text = "Rejected"
        statusTextView.setBackgroundResource(R.drawable.bg_status_red)
      }
      "A" -> {
        statusTextView.text = "Approved"
        statusTextView.setBackgroundResource(R.drawable.bg_status_blue)
      }
      "D" -> {
        statusTextView.text = "Disbursed"
        statusTextView.setBackgroundResource(R.drawable.bg_status_green)
      }
      "P" -> {
        statusTextView.text = "Pending"
        statusTextView.setBackgroundResource(R.drawable.bg_status_grey)
      }
    }
  }
}
