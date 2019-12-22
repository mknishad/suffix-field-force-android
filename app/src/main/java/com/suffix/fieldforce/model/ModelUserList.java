package com.suffix.fieldforce.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUserList {

  @SerializedName("empId")
  @Expose
  public Integer empId;
  @SerializedName("empOfficeId")
  @Expose
  public String empOfficeId;
  @SerializedName("gender")
  @Expose
  public String gender;
  @SerializedName("empName")
  @Expose
  public String empName;
  @SerializedName("pictureLink")
  @Expose
  public String pictureLink;

  @SerializedName("empDept")
  @Expose
  public String empDept;
  @SerializedName("empDesg")
  @Expose
  public String empDesg;

  public Integer getEmpId() {
    return empId;
  }

  public String getEmpOfficeId() {
    return empOfficeId;
  }

  public String getGender() {
    return gender;
  }

  public String getEmpName() {
    return empName;
  }

  public String getPictureLink() {
    return pictureLink;
  }

  public String getEmpDept() {
    return empDept;
  }

  public String getEmpDesg() {
    return empDesg;
  }

}
