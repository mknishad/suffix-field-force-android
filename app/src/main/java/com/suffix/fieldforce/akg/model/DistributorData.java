package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributorData {
  @SerializedName("distributorId")
  @Expose
  private long distributorId;
  @SerializedName("distributorName")
  @Expose
  private String distributorName;
  @SerializedName("teritoryName")
  @Expose
  private String teritoryName;

  @Override
  public String toString() {
    return "DistributorData{" +
        "distributorId=" + distributorId +
        ", distributorName='" + distributorName + '\'' +
        ", teritoryName='" + teritoryName + '\'' +
        '}';
  }

  public long getDistributorId() {
    return distributorId;
  }

  public void setDistributorId(long distributorId) {
    this.distributorId = distributorId;
  }

  public String getDistributorName() {
    return distributorName;
  }

  public void setDistributorName(String distributorName) {
    this.distributorName = distributorName;
  }

  public String getTeritoryName() {
    return teritoryName;
  }

  public void setTeritoryName(String teritoryName) {
    this.teritoryName = teritoryName;
  }
}
