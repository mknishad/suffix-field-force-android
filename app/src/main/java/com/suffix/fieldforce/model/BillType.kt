package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillType(
  @Json(name = "BillShortName") var billShortName: String,
  var billTypeId: Int
) : Parcelable
