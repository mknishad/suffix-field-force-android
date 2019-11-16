package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillListObj(
    @Json(name = "ResponseCode") var responseCode: String,
    @Json(name = "ResponseData") var bills: List<Bill>,
    @Json(name = "ResponseText") var responseText: String
) : Parcelable
