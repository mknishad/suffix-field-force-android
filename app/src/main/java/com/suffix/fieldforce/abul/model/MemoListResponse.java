package com.suffix.fieldforce.abul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemoListResponse {

  @SerializedName("invNo")
  @Expose
  private String invNo;
  @SerializedName("discountAmount")
  @Expose
  private Integer discountAmount;
  @SerializedName("prevDue")
  @Expose
  private Integer prevDue;
  @SerializedName("receivedAmount")
  @Expose
  private Integer receivedAmount;
  @SerializedName("totalAmount")
  @Expose
  private Integer totalAmount;
  @SerializedName("txnDt")
  @Expose
  private String txnDt;
  @SerializedName("consumer")
  @Expose
  private Consumer consumer;
  @SerializedName("salesRep")
  @Expose
  private SalesRep salesRep;

  public String getInvNo() {
    return invNo;
  }

  public void setInvNo(String invNo) {
    this.invNo = invNo;
  }

  public Integer getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(Integer discountAmount) {
    this.discountAmount = discountAmount;
  }

  public Integer getPrevDue() {
    return prevDue;
  }

  public void setPrevDue(Integer prevDue) {
    this.prevDue = prevDue;
  }

  public Integer getReceivedAmount() {
    return receivedAmount;
  }

  public void setReceivedAmount(Integer receivedAmount) {
    this.receivedAmount = receivedAmount;
  }

  public Integer getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Integer totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTxnDt() {
    return txnDt;
  }

  public void setTxnDt(String txnDt) {
    this.txnDt = txnDt;
  }

  public Consumer getConsumer() {
    return consumer;
  }

  public void setConsumer(Consumer consumer) {
    this.consumer = consumer;
  }

  public SalesRep getSalesRep() {
    return salesRep;
  }

  public void setSalesRep(SalesRep salesRep) {
    this.salesRep = salesRep;
  }

}