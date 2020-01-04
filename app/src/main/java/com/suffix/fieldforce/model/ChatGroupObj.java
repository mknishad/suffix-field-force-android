package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGroupObj {
  @SerializedName("ResponseCode")
  @Expose
  public String responseCode;
  @SerializedName("ResponseData")
  @Expose
  public List<GroupChatInfo> responseData = null;
  @SerializedName("ResponseText")
  @Expose
  public String responseText;

  public String getResponseCode() {
    return responseCode;
  }

  public List<GroupChatInfo> getResponseData() {
    return responseData;
  }

  public String getResponseText() {
    return responseText;
  }
}
