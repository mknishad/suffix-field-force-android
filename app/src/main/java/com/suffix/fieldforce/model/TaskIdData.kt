package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskIdData(
  var taskIdDataObj:  List<TaskIdObj>?
) : Parcelable
