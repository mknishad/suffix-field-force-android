package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupChatInfo {
  @SerializedName("chatGroupId")
  @Expose
  public String chatGroupId;
  @SerializedName("chatGroupName")
  @Expose
  public String chatGroupName;
  @SerializedName("chatGroupDetailObjArray")
  @Expose
  public List<ChatGroupDetailObjArray> chatGroupDetailObjArray = null;
  @SerializedName("chatGroupIcon")
  @Expose
  public String chatGroupIcon;

  public GroupChatInfo(String chatGroupName) {
    this.chatGroupName = chatGroupName;
  }

  public String getChatGroupId() {
    return chatGroupId;
  }

  public String getChatGroupName() {
    return chatGroupName;
  }

  public List<ChatGroupDetailObjArray> getChatGroupDetailObjArray() {
    return chatGroupDetailObjArray;
  }

  public String getChatGroupIcon() {
    return chatGroupIcon;
  }
}
