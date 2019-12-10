package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Inventory(
  var id: String?,
  var name: String?,
  var quantity: String?
) : Parcelable
