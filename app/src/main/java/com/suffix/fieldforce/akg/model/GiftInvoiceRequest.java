package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GiftInvoiceRequest extends RealmObject {

  @PrimaryKey
  @SerializedName("consumerId")
  @Expose
  public int consumerId;
  @SerializedName("invoiceDate")
  @Expose
  public long invoiceDate;
  @SerializedName("invoiceId")
  @Expose
  public String invoiceId;
  @SerializedName("invoiceProducts")
  @Expose
  public RealmList<Gift> gifts;
  @SerializedName("invoiceType")
  @Expose
  public String invoiceType;
  @SerializedName("salesRepId")
  @Expose
  public int salesRepId;

  public boolean isSynced;

  public GiftInvoiceRequest() {
  }

  public GiftInvoiceRequest(int consumerId, long invoiceDate, String invoiceId, RealmList<Gift> gifts,
                            String invoiceType, int salesRepId) {
    this.consumerId = consumerId;
    this.invoiceDate = invoiceDate;
    this.invoiceId = invoiceId;
    this.gifts = gifts;
    this.invoiceType = invoiceType;
    this.salesRepId = salesRepId;
  }

  public int getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(int consumerId) {
    this.consumerId = consumerId;
  }

  public long getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(long invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
  }

  public List<Gift> getGifts() {
    return gifts;
  }

  public void setGifts(RealmList<Gift> gifts) {
    this.gifts = gifts;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public int getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(int salesRepId) {
    this.salesRepId = salesRepId;
  }

  public boolean isSynced() {
    return isSynced;
  }

  public void setSynced(boolean synced) {
    isSynced = synced;
  }

  @Override
  public String toString() {
    return "GiftInvoiceRequest{" +
        "consumerId=" + consumerId +
        ", invoiceDate=" + invoiceDate +
        ", invoiceId='" + invoiceId + '\'' +
        ", gifts=" + gifts +
        ", invoiceType='" + invoiceType + '\'' +
        ", salesRepId=" + salesRepId +
        ", isSynced=" + isSynced +
        '}';
  }
}
