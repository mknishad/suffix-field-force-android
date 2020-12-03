package com.suffix.fieldforce.akg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class InvoiceProduct extends RealmObject implements Parcelable{
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

  private String productCode;
  private double sellingRate;

  protected InvoiceProduct(Parcel in) {
    discount = in.readDouble();
    productId = in.readInt();
    productQty = in.readInt();
    rate = in.readDouble();
    subToalAmount = in.readDouble();
    productCode = in.readString();
    sellingRate = in.readDouble();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(discount);
    dest.writeInt(productId);
    dest.writeInt(productQty);
    dest.writeDouble(rate);
    dest.writeDouble(subToalAmount);
    dest.writeString(productCode);
    dest.writeDouble(sellingRate);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<InvoiceProduct> CREATOR = new Creator<InvoiceProduct>() {
    @Override
    public InvoiceProduct createFromParcel(Parcel in) {
      return new InvoiceProduct(in);
    }

    @Override
    public InvoiceProduct[] newArray(int size) {
      return new InvoiceProduct[size];
    }
  };

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public InvoiceProduct(){

  }

  public double getSellingRate() {
    return sellingRate;
  }

  public void setSellingRate(double sellingRate) {
    this.sellingRate = sellingRate;
  }

  public InvoiceProduct(double discount, int productId, int productQty, double rate,
                        double subToalAmount, String productCode, double sellingRate) {
    this.discount = discount;
    this.productId = productId;
    this.productQty = productQty;
    this.rate = rate;
    this.subToalAmount = subToalAmount;
    this.productCode = productCode;
    this.sellingRate = sellingRate;
  }

  @Override
  public String toString() {
    return "InvoiceProduct{" +
        "discount=" + discount +
        ", productId=" + productId +
        ", productQty=" + productQty +
        ", rate=" + rate +
        ", subToalAmount=" + subToalAmount +
        ", productCode=" + productCode +
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
