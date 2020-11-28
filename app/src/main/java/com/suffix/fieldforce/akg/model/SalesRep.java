package com.suffix.fieldforce.akg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesRep {

  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("addDate")
  @Expose
  private Integer addDate;
  @SerializedName("addUser")
  @Expose
  private String addUser;
  @SerializedName("modDate")
  @Expose
  private Integer modDate;
  @SerializedName("modUser")
  @Expose
  private String modUser;
  @SerializedName("phoneNo")
  @Expose
  private String phoneNo;
  @SerializedName("salesRepId")
  @Expose
  private String salesRepId;
  @SerializedName("salesRepName")
  @Expose
  private String salesRepName;
  @SerializedName("status")
  @Expose
  private String status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAddDate() {
    return addDate;
  }

  public void setAddDate(Integer addDate) {
    this.addDate = addDate;
  }

  public String getAddUser() {
    return addUser;
  }

  public void setAddUser(String addUser) {
    this.addUser = addUser;
  }

  public Integer getModDate() {
    return modDate;
  }

  public void setModDate(Integer modDate) {
    this.modDate = modDate;
  }

  public String getModUser() {
    return modUser;
  }

  public void setModUser(String modUser) {
    this.modUser = modUser;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(String salesRepId) {
    this.salesRepId = salesRepId;
  }

  public String getSalesRepName() {
    return salesRepName;
  }

  public void setSalesRepName(String salesRepName) {
    this.salesRepName = salesRepName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}