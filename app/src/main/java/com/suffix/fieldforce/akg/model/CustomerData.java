package com.suffix.fieldforce.akg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.suffix.fieldforce.akg.model.product.CategoryModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CustomerData extends RealmObject implements Parcelable {
  @PrimaryKey
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

  @SerializedName("lat")
  @Expose
  private Double lat;

  @SerializedName("lng")
  @Expose
  private Double lng;

  @SerializedName("address")
  @Expose
  private String address;

  private RealmList<CategoryModel> categoryModels;

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

  public RealmList<CategoryModel> getCategoryModels() {
    return categoryModels;
  }

  public void setCategoryModels(RealmList<CategoryModel> categoryModels) {
    this.categoryModels = categoryModels;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public static Creator<CustomerData> getCREATOR() {
    return CREATOR;
  }

  public CustomerData() {
  }

  public CustomerData(String customerName) {
    this.customerName = customerName;
  }

  public CustomerData(Integer id, String customerName, String mobileNo, String tradeLicenseNo, String status, String consumerCode, Double lat, Double lng, String address, RealmList<CategoryModel> categoryModels) {
    this.id = id;
    this.customerName = customerName;
    this.mobileNo = mobileNo;
    this.tradeLicenseNo = tradeLicenseNo;
    this.status = status;
    this.consumerCode = consumerCode;
    this.lat = lat;
    this.lng = lng;
    this.address = address;
    this.categoryModels = categoryModels;
  }

  protected CustomerData(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readInt();
    }
    customerName = in.readString();
    mobileNo = in.readString();
    tradeLicenseNo = in.readString();
    status = in.readString();
    consumerCode = in.readString();
    this.lat = in.readDouble();
    this.lng = in.readDouble();
    this.address = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(id);
    }
    dest.writeString(customerName);
    dest.writeString(mobileNo);
    dest.writeString(tradeLicenseNo);
    dest.writeString(status);
    dest.writeString(consumerCode);
    dest.writeDouble(this.lat);
    dest.writeDouble(this.lng);
    dest.writeString(this.address);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CustomerData> CREATOR = new Creator<CustomerData>() {
    @Override
    public CustomerData createFromParcel(Parcel in) {
      return new CustomerData(in);
    }

    @Override
    public CustomerData[] newArray(int size) {
      return new CustomerData[size];
    }
  };

  @Override
  public String toString() {
    return "CustomerData{" +
        "id=" + id +
        ", customerName='" + customerName + '\'' +
        ", mobileNo='" + mobileNo + '\'' +
        ", tradeLicenseNo='" + tradeLicenseNo + '\'' +
        ", status='" + status + '\'' +
        ", consumerCode='" + consumerCode + '\'' +
        '}';
  }
}
