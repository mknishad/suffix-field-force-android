package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distributor {
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("code")
  @Expose
  private int code;
  @SerializedName("data")
  @Expose
  private DistributorData data;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public DistributorData getData() {
    return data;
  }

  public void setData(DistributorData data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Distributor{" +
        "message=" + message +
        ", code=" + code +
        ", data=" + data +
        '}';
  }
}
