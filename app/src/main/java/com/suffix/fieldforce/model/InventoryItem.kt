package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InventoryItem(
  var productId: Int,
  var stockQuantity: Double,
  var productInvId: Int,
  var productName: String
) : Parcelable
