package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillStatObj(
    @Json(name = "ResponseData") var dashboards: List<DashBoard>
): Parcelable
