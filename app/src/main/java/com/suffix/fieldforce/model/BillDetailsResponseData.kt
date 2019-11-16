package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDetailsResponseData(
    @Json(name = "ResponseCode") var responseCode: String,
    var billDetailsObj: BillDetailsObj,
    var billListObj: BillListObj,
    var billApproveObj: BillApproveObj,
    @Json(name = "ResponseText") var responseText: String
) : Parcelable
