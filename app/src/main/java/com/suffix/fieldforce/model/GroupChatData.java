package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupChatData {
  @SerializedName("chatGroupObj")
  @Expose
  private ChatGroupObj chatGroupObj;

  public ChatGroupObj getChatGroupObj() {
    return chatGroupObj;
  }

}
