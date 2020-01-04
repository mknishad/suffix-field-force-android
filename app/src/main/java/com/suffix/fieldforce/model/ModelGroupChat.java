package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGroupChat {
  @SerializedName("ResponseCode")
  @Expose
  public String responseCode;
  @SerializedName("ResponseData")
  @Expose
  public GroupChatData responseData;
  @SerializedName("ResponseText")
  @Expose
  public String responseText;

  public String getResponseCode() {
    return responseCode;
  }

  public GroupChatData getResponseData() {
    return responseData;
  }

  public String getResponseText() {
    return responseText;
  }
}
