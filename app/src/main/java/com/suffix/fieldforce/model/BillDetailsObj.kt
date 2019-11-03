package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDetailsObj(
    @Json(name = "ResponseData") var billDetailsList: List<BillDetails>
): Parcelable
