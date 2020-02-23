package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Project {
  @SerializedName("ResponseCode")
  @Expose
  public String responseCode;
  @SerializedName("ResponseData")
  @Expose
  public List<ProjectData> responseData = null;
  @SerializedName("ResponseText")
  @Expose
  public String responseText;
}
