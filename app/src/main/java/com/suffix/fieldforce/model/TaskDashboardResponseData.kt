package com.suffix.fieldforce.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class TaskDashboardResponseData(
  @Json(name = "assignTicketCount") var assignedTicketCount: String,
  @Json(name = "completedTicketCount") var completedTicketCount: String,
  @Json(name = "inprogressTicketCount") var inProgressTicketCount: String,
  @Json(name = "accepetedTicketCount") var acceptedTicketCount: String
) : Parcelable