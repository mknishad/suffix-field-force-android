package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelGroupChatResponse {
  @SerializedName("ResponseCode")
  @Expose
  private String responseCode;
  @SerializedName("ResponseData")
  @Expose
  private List<Object> responseData = null;
  @SerializedName("ResponseText")
  @Expose
  private String responseText;
}
