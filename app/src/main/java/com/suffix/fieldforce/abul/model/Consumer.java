package com.suffix.fieldforce.abul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Consumer {

  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("consumerCode")
  @Expose
  private String consumerCode;
  @SerializedName("customerName")
  @Expose
  private String customerName;
  @SerializedName("lat")
  @Expose
  private Double lat;
  @SerializedName("lng")
  @Expose
  private Double lng;
  @SerializedName("mobileNo")
  @Expose
  private String mobileNo;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("tradeLicenseName")
  @Expose
  private String tradeLicenseName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getConsumerCode() {
    return consumerCode;
  }

  public void setConsumerCode(String consumerCode) {
    this.consumerCode = consumerCode;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTradeLicenseName() {
    return tradeLicenseName;
  }

  public void setTradeLicenseName(String tradeLicenseName) {
    this.tradeLicenseName = tradeLicenseName;
  }

}