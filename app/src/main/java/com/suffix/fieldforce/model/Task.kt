package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var ticketTitle: String?,
    var consumerAddress: String?,
    var consumerThana: String?,
    var ticketRemark: String?,
    var deviceName: String?,
    var consumerDistrict: String?,
    @Json(name = "ticketCateroryCode") var ticketCategoryCode: String?,
    var ticketStartDate: String?,
    var ticketEndDate: String?,
    var ticketStatus: String?,
    var ticketStatusText: String?,
    @Json(name = "ticketCateroryTitle") var ticketCategoryTitle: String?,
    var ticketCode: String?,
    var ticketId: String?,
    var consumerMobile: String?,
    var consumerName: String?
) : Parcelable
