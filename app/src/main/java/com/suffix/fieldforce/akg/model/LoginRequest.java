package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("password")
  @Expose
  private String password;

  @SerializedName("deviceId")
  @Expose
  private String deviceId;

  public LoginRequest(String deviceId, String userId, String password) {
    this.deviceId = deviceId;
    this.userId = userId;
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginRequest{" +
        "userId='" + userId + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }
}
