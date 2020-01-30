package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectData {
  @SerializedName("projectId")
  @Expose
  public String projectId;
  @SerializedName("projectTitle")
  @Expose
  public String projectTitle;

  public String getProjectId() {
    return projectId;
  }

  public String getProjectTitle() {
    return projectTitle;
  }
}
