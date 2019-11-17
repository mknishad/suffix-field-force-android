package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bill(
    var billId: String?,
    var advanceId: String?,
    var advanceBillId: String?,
    var description: String?,
    var billDate: String?,
    var advanceBillDate: String?,
    var status: String?,
    var billAmount: Double?,
    var billTypeId: Int?,
    var taskId: String?
): Parcelable
