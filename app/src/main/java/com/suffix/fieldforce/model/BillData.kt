package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillData(
    var billDate: String,
    var billDataObj: List<Bill>,
    var remarks: String
): Parcelable
