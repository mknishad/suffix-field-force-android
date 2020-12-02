package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceRequest {
  @SerializedName("customerId")
  @Expose
  private int customerId;
  @SerializedName("invoiceDate")
  @Expose
  private int invoiceDate;
  @SerializedName("invoiceId")
  @Expose
  private String invoiceId;
  @SerializedName("invoiceProducts")
  @Expose
  private List<InvoiceProduct> invoiceProducts;
  @SerializedName("salesRepId")
  @Expose
  private int salesRepId;
  @SerializedName("totalAmount")
  @Expose
  private double totalAmount;
}
