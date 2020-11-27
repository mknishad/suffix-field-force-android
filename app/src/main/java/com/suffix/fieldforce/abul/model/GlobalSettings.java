package com.suffix.fieldforce.abul.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalSettings {
  @SerializedName("id")
  @Expose
  private int id;
  @SerializedName("attributeName")
  @Expose
  private String attributeName;
  @SerializedName("attributeValue")
  @Expose
  private int attributeValue;

  @Override
  public String toString() {
    return "GlobalSettings{" +
        "id=" + id +
        ", attributeName='" + attributeName + '\'' +
        ", attributeValue=" + attributeValue +
        '}';
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public int getAttributeValue() {
    return attributeValue;
  }

  public void setAttributeValue(int attributeValue) {
    this.attributeValue = attributeValue;
  }
}
