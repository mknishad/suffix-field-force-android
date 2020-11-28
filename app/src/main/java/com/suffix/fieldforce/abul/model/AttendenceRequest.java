package com.suffix.fieldforce.abul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendenceRequest {
  @SerializedName("entryTime")
  @Expose
  private long entryTime;
  @SerializedName("lat")
  @Expose
  private double lat;
  @SerializedName("lng")
  @Expose
  private double lng;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("userId")
  @Expose
  private int userId;

  public AttendenceRequest(long entryTime, double lat, double lng, String status, int userId) {
    this.entryTime = entryTime;
    this.lat = lat;
    this.lng = lng;
    this.status = status;
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "AttendenceRequest{" +
        "entryTime=" + entryTime +
        ", lat=" + lat +
        ", lng=" + lng +
        ", status='" + status + '\'' +
        ", userId=" + userId +
        '}';
  }
}
