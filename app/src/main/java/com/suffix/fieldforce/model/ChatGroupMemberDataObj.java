package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatGroupMemberDataObj {
  @SerializedName("groupMemberId")
  @Expose
  private String groupMemberId;

  public ChatGroupMemberDataObj(String groupMemberId) {
    this.groupMemberId = groupMemberId;
  }
}
