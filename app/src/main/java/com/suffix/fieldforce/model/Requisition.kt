package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Requisition(
  var itemRequisitionDate: String?,
  var description: String?,
  var itemRequisitionId: Int?,
  var status: String?
) : Parcelable
