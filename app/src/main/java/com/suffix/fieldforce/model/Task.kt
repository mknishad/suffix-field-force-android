package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var id: String?,
    var name: String?,
    var status: String?,
    var date: String?,
    var project: String?
) : Parcelable
