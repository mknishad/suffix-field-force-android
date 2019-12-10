package com.suffix.fieldforce.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RosterScheduleModel {

  @SerializedName("ResponseCode")
  @Expose
  private String responseCode;
  @SerializedName("ResponseData")
  @Expose
  private List<RosterScheduleDateModel> responseData = null;
  @SerializedName("ResponseText")
  @Expose
  private String responseText;

  public String getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(String responseCode) {
    this.responseCode = responseCode;
  }

  public List<RosterScheduleDateModel> getResponseData() {
    return responseData;
  }

  public void setResponseData(List<RosterScheduleDateModel> responseData) {
    this.responseData = responseData;
  }

  public String getResponseText() {
    return responseText;
  }

  public void setResponseText(String responseText) {
    this.responseText = responseText;
  }

}
