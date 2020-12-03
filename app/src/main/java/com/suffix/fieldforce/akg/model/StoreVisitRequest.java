package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreVisitRequest {

  @SerializedName("consumerId")
  @Expose
  public int consumerId;
  @SerializedName("entryTime")
  @Expose
  public long entryTime;
  @SerializedName("lat")
  @Expose
  public double lat;
  @SerializedName("lng")
  @Expose
  public double lng;
  @SerializedName("salesRepId")
  @Expose
  public int salesRepId;
  @SerializedName("status")
  @Expose
  public String status;


  public StoreVisitRequest() {
  }

  public StoreVisitRequest(int consumerId, int entryTime, int lat, int lng, int salesRepId) {
    this.consumerId = consumerId;
    this.entryTime = entryTime;
    this.lat = lat;
    this.lng = lng;
    this.salesRepId = salesRepId;
  }

  public int getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(int consumerId) {
    this.consumerId = consumerId;
  }

  public long getEntryTime() {
    return entryTime;
  }

  public void setEntryTime(long entryTime) {
    this.entryTime = entryTime;
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

  public int getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(int salesRepId) {
    this.salesRepId = salesRepId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
