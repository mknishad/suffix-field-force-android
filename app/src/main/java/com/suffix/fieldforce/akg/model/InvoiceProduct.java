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

  public InvoiceProduct(double discount, int productId, int productQty, double rate, double subToalAmount) {
    this.discount = discount;
    this.productId = productId;
    this.productQty = productQty;
    this.rate = rate;
    this.subToalAmount = subToalAmount;
  }

  @Override
  public String toString() {
    return "InvoiceProduct{" +
        "discount=" + discount +
        ", productId=" + productId +
        ", productQty=" + productQty +
        ", rate=" + rate +
        ", subToalAmount=" + subToalAmount +
        '}';
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getProductQty() {
    return productQty;
  }

  public void setProductQty(int productQty) {
    this.productQty = productQty;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

  public double getSubToalAmount() {
    return subToalAmount;
  }

  public void setSubToalAmount(double subToalAmount) {
    this.subToalAmount = subToalAmount;
  }
}
