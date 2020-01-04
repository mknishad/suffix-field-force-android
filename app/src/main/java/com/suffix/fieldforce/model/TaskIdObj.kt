package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskIdObj(
  var taskId: Int?
) : Parcelable
