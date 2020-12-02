package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveCustomerRequest {

  @SerializedName("customerId")
  @Expose
  private Integer customerId;
  @SerializedName("lat")
  @Expose
  private double lat;
  @SerializedName("lng")
  @Expose
  private double lng;

  public ActiveCustomerRequest() {
  }

  public ActiveCustomerRequest(Integer customerId, double lat, double lng) {
    this.customerId = customerId;
    this.lat = lat;
    this.lng = lng;
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }
}
