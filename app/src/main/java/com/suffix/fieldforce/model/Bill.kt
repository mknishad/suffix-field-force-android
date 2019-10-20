package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bill(
    var billAmount: Double,
    var billTypeId: Int
): Parcelable