package com.suffix.fieldforce.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUserList implements Parcelable {

  @SerializedName("empId")
  @Expose
  public String empId;
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

  public String getEmpId() {
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

  public static Creator<ModelUserList> getCREATOR() {
    return CREATOR;
  }

  protected ModelUserList(Parcel in) {

    empId = in.readString();
    empOfficeId = in.readString();
    gender = in.readString();
    empName = in.readString();
    pictureLink = in.readString();
    empDept = in.readString();
    empDesg = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(empId);
    dest.writeString(empOfficeId);
    dest.writeString(gender);
    dest.writeString(empName);
    dest.writeString(pictureLink);
    dest.writeString(empDept);
    dest.writeString(empDesg);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ModelUserList> CREATOR = new Creator<ModelUserList>() {
    @Override
    public ModelUserList createFromParcel(Parcel in) {
      return new ModelUserList(in);
    }

    @Override
    public ModelUserList[] newArray(int size) {
      return new ModelUserList[size];
    }
  };
}
