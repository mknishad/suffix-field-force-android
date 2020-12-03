package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

  @SerializedName("collectionDate")
  @Expose
  private Long collectionDate;
  @SerializedName("customerId")
  @Expose
  private long customerId;
  @SerializedName("quantity")
  @Expose
  private Integer quantity;
  @SerializedName("salesRepId")
  @Expose
  private long salesRepId;

  public Long getCollectionDate() {
    return collectionDate;
  }

  public void setCollectionDate(Long collectionDate) {
    this.collectionDate = collectionDate;
  }

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public long getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(long salesRepId) {
    this.salesRepId = salesRepId;
  }


}
