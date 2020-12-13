package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoLocationRequest {
  @SerializedName("lat")
  @Expose
  private double lat;
  @SerializedName("lon")
  @Expose
  private double lon;
  @SerializedName("userId")
  @Expose
  private int userId;
  @SerializedName("visittime")
  @Expose
  private long visitTime;

  public GeoLocationRequest(double lat, double lon, int userId, long visitTime) {
    this.lat = lat;
    this.lon = lon;
    this.userId = userId;
    this.visitTime = visitTime;
  }

  @Override
  public String toString() {
    return "GeoLocationRequest{" +
        "lat=" + lat +
        ", lon=" + lon +
        ", userId=" + userId +
        ", visitTime='" + visitTime + '\'' +
        '}';
  }
}
