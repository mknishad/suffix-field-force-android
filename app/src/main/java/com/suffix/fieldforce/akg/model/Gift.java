package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Gift extends RealmObject {

  @SerializedName("giftId")
  @Expose
  private int giftId;
  @SerializedName("giftQty")
  @Expose
  private int giftQty;

  public Gift(int giftId, int giftQty) {
    this.giftId = giftId;
    this.giftQty = giftQty;
  }

  public long getGiftId() {
    return giftId;
  }

  public void setGiftId(int giftId) {
    this.giftId = giftId;
  }

  public int getGiftQty() {
    return giftQty;
  }

  public void setGiftQty(int giftQty) {
    this.giftQty = giftQty;
  }

  @Override
  public String toString() {
    return "Gift{" +
        "giftId=" + giftId +
        ", giftQty=" + giftQty +
        '}';
  }
}
