package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransportListRequasition {

  @SerializedName("ResponseCode")
  @Expose
  public String responseCode;
  @SerializedName("ResponseData")
  @Expose
  public List<TransportListRequasitionData> responseData = null;
  @SerializedName("ResponseText")
  @Expose
  public String responseText;

  public String getResponseCode() {
    return responseCode;
  }

  public List<TransportListRequasitionData> getResponseData() {
    return responseData;
  }

  public String getResponseText() {
    return responseText;
  }
}
