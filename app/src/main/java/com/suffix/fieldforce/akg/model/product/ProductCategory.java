package com.suffix.fieldforce.akg.model.product;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategory {

  @SerializedName("Cigrettee")
  @Expose
  private List<CategoryModel> categoryModel = null;
  @SerializedName("Bidi")
  @Expose
  private List<CategoryModel> bidi = null;
  @SerializedName("Match")
  @Expose
  private List<CategoryModel> match = null;

  public List<CategoryModel> getCategoryModel() {
    return categoryModel;
  }

  public void setCategoryModel(List<CategoryModel> categoryModel) {
    this.categoryModel = categoryModel;
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
