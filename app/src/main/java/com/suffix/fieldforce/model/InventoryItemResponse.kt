package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InventoryItemResponse(
  @Json(name = "ResponseCode") var responseCode: String,
  @Json(name = "ResponseData") var responseData: List<InventoryItem>,
  @Json(name = "ResponseText") var responseText: String
) : Parcelable
