package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillListObj(
    @Json(name = "ResponseData") var bills: List<Bill>
): Parcelable
