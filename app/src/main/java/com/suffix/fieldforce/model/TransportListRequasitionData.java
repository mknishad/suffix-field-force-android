package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportListRequasitionData {
  @SerializedName("startTime")
  @Expose
  public String startTime;
  @SerializedName("remark")
  @Expose
  public String remark;
  @SerializedName("id")
  @Expose
  public String id;
  @SerializedName("endTime")
  @Expose
  public String endTime;
  @SerializedName("status")
  @Expose
  public String status;

  public String getStartTime() {
    return startTime;
  }

  public String getRemark() {
    return remark;
  }

  public String getId() {
    return id;
  }

  public String getEndTime() {
    return endTime;
  }

  public String getStatus() {
    return status;
  }
}
