package com.suffix.fieldforce.akg.model.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategory {

  @SerializedName("Cigrettee")
  @Expose
  private List<CategoryModel> cigarette = null;
  @SerializedName("Bidi")
  @Expose
  private List<CategoryModel> bidi = null;
  @SerializedName("Match")
  @Expose
  private List<CategoryModel> match = null;

  public List<CategoryModel> getCigarette() {
    return cigarette;
  }

  public void setCigarette(List<CategoryModel> cigarette) {
    this.cigarette = cigarette;
  }

  public List<CategoryModel> getBidi() {
    return bidi;
  }

  public void setBidi(List<CategoryModel> bidi) {
    this.bidi = bidi;
  }

  public List<CategoryModel> getMatch() {
    return match;
  }

  public void setMatch(List<CategoryModel> match) {
    this.match = match;
  }
}
