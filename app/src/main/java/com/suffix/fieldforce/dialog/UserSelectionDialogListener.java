package com.suffix.fieldforce.dialog;

import com.suffix.fieldforce.model.ChatGroupMemberDataObj;

import java.util.List;

public interface UserSelectionDialogListener {
  public void onCreate(String groupName, List<ChatGroupMemberDataObj> chatGroupMemberDataObj);
  public void onCancel();
}
