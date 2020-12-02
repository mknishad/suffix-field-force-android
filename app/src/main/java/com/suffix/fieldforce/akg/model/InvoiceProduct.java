package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceProduct {
  @SerializedName("discount")
  @Expose
  private double discount;
  @SerializedName("productId")
  @Expose
  private int productId;
  @SerializedName("productQty")
  @Expose
  private int productQty;
  @SerializedName("rate")
  @Expose
  private double rate;
  @SerializedName("subToalAmount")
  @Expose
  private double subToalAmount;
}
