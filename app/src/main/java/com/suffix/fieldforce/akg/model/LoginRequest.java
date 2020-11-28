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

  public LoginRequest(String userId, String password) {
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
}
