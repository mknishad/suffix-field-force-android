package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;

public class InvoiceRequest extends RealmObject {
  @SerializedName("customerId")
  @Expose
  private int customerId;
  @SerializedName("invoiceDate")
  @Expose
  private long invoiceDate;
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

  public InvoiceRequest(int customerId, long invoiceDate, String invoiceId,
                        List<InvoiceProduct> invoiceProducts, int salesRepId, double totalAmount) {
    this.customerId = customerId;
    this.invoiceDate = invoiceDate;
    this.invoiceId = invoiceId;
    this.invoiceProducts = invoiceProducts;
    this.salesRepId = salesRepId;
    this.totalAmount = totalAmount;
  }

  @Override
  public String toString() {
    return "InvoiceRequest{" +
        "customerId=" + customerId +
        ", invoiceDate=" + invoiceDate +
        ", invoiceId='" + invoiceId + '\'' +
        ", invoiceProducts=" + invoiceProducts +
        ", salesRepId=" + salesRepId +
        ", totalAmount=" + totalAmount +
        '}';
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public long getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(long invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
  }

  public List<InvoiceProduct> getInvoiceProducts() {
    return invoiceProducts;
  }

  public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
    this.invoiceProducts = invoiceProducts;
  }

  public int getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(int salesRepId) {
    this.salesRepId = salesRepId;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }
}
