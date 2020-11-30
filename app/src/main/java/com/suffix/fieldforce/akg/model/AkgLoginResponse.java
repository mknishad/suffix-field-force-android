package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AkgLoginResponse {
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("code")
  @Expose
  private int code;
  @SerializedName("errorMessage")
  @Expose
  private String errorMessage;
  @SerializedName("data")
  @Expose
  private ResponseData data;

  @Override
  public String toString() {
    return "AbulLoginResponse{" +
        "message='" + message + '\'' +
        ", code=" + code +
        ", errorMessage='" + errorMessage + '\'' +
        ", data=" + data +
        '}';
  }

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

  public ResponseData getData() {
    return data;
  }

  public void setData(ResponseData data) {
    this.data = data;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
