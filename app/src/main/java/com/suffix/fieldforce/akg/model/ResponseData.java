package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("password")
  @Expose
  private String password;
  @SerializedName("globalSettingList")
  @Expose
  private List<GlobalSettings> globalSettingList;

  @Override
  public String toString() {
    return "ResponseData{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", password='" + password + '\'' +
        ", globalSettingList=" + globalSettingList +
        '}';
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

  public List<GlobalSettings> getGlobalSettingList() {
    return globalSettingList;
  }
}
