package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDetailsResponseData(
    var billDetailsObj: BillDetailsObj
) : Parcelable
