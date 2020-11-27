package com.suffix.fieldforce.abul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
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
        "userId='" + userId + '\'' +
        ", password='" + password + '\'' +
        ", globalSettingList=" + globalSettingList +
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

  public List<GlobalSettings> getGlobalSettingList() {
    return globalSettingList;
  }

  public void setGlobalSettingList(List<GlobalSettings> globalSettingList) {
    this.globalSettingList = globalSettingList;
  }
}
