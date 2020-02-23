package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequisitionResponse(
  @Json(name = "ResponseCode") var responseCode: String,
  @Json(name = "ResponseData") var responseData: RequisitionResponseData,
  @Json(name = "ResponseText") var responseText: String
) : Parcelable
