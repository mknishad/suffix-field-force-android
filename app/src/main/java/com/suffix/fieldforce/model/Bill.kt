package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bill(
    var billId: String?,
    var description: String?,
    var billDate: String?,
    var status: String?,
    var billAmount: Double?,
    var billTypeId: Int?
): Parcelable