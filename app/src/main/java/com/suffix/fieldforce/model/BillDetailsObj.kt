package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDetailsObj(
  @Json(name = "ResponseCode") var responseCode: String,
  @Json(name = "ResponseData") var billDetailsList: List<BillDetails>,
  @Json(name = "ResponseText") var responseText: String
) : Parcelable
