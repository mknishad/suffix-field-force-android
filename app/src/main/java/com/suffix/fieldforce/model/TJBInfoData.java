package com.suffix.fieldforce.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TJBInfoData implements Parcelable {

  public String getLocationName() {
    return locationName;
  }

  public String getTjbLat() {
    return tjbLat;
  }

  public String getTjbLng() {
    return tjbLng;
  }

  public String getFromCoreId() {
    return fromCoreId;
  }

  public String getFromCore() {
    return fromCore;
  }

  public String getToCoreId() {
    return toCoreId;
  }

  public String getToCore() {
    return toCore;
  }

  public static Creator<TJBInfoData> getCREATOR() {
    return CREATOR;
  }

  public String locationName;
  public String tjbLat;
  public String tjbLng;
  public String fromCoreId;
  public String fromCore;
  public String toCoreId;
  public String toCore;

  public TJBInfoData() {
  }

  public TJBInfoData(String locationName, String tjbLat, String tjbLng, String fromCoreId, String fromCore, String toCoreId, String toCore) {
    this.locationName = locationName;
    this.tjbLat = tjbLat;
    this.tjbLng = tjbLng;
    this.fromCoreId = fromCoreId;
    this.fromCore = fromCore;
    this.toCoreId = toCoreId;
    this.toCore = toCore;
  }

  protected TJBInfoData(Parcel in) {
    locationName = in.readString();
    tjbLat = in.readString();
    tjbLng = in.readString();
    fromCoreId = in.readString();
    fromCore = in.readString();
    toCoreId = in.readString();
    toCore = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(locationName);
    dest.writeString(tjbLat);
    dest.writeString(tjbLng);
    dest.writeString(fromCoreId);
    dest.writeString(fromCore);
    dest.writeString(toCoreId);
    dest.writeString(toCore);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TJBInfoData> CREATOR = new Creator<TJBInfoData>() {
    @Override
    public TJBInfoData createFromParcel(Parcel in) {
      return new TJBInfoData(in);
    }

    @Override
    public TJBInfoData[] newArray(int size) {
      return new TJBInfoData[size];
    }
  };
}
