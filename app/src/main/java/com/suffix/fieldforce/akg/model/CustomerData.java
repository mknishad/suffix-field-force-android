package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerData {
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("customerName")
  @Expose
  private String customerName;
  @SerializedName("mobileNo")
  @Expose
  private String mobileNo;
  @SerializedName("tradeLicenseNo")
  @Expose
  private String tradeLicenseNo;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("consumerCode")
  @Expose
  private String consumerCode;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getTradeLicenseNo() {
    return tradeLicenseNo;
  }

  public void setTradeLicenseNo(String tradeLicenseNo) {
    this.tradeLicenseNo = tradeLicenseNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getConsumerCode() {
    return consumerCode;
  }

  public void setConsumerCode(String consumerCode) {
    this.consumerCode = consumerCode;
  }

}
