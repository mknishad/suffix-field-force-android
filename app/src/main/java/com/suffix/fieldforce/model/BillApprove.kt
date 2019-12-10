package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillApprove(
  var approveDate: String?,
  var managerStatus: String?,
  var approveEmpId: String?,
  var approveEmpName: String?,
  var billApproveId: String?
) : Parcelable
