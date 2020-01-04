package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ChatGroupDetailObjArray {
  @SerializedName("memberImageName")
  @Expose
  public String memberImageName;
  @SerializedName("groupMemberId")
  @Expose
  public String groupMemberId;
  @SerializedName("groupMemberOfficeId")
  @Expose
  public String groupMemberOfficeId;
  @SerializedName("groupMemberName")
  @Expose
  public String groupMemberName;

  public String getMemberImageName() {
    return memberImageName;
  }

  public String getGroupMemberId() {
    return groupMemberId;
  }

  public String getGroupMemberOfficeId() {
    return groupMemberOfficeId;
  }

  public String getGroupMemberName() {
    return groupMemberName;
  }
}
