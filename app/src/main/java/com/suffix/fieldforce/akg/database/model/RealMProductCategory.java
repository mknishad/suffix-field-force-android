package com.suffix.fieldforce.akg.database.model;

import com.suffix.fieldforce.akg.model.product.CategoryModel;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealMProductCategory extends RealmObject {

  private RealmList<CategoryModel> cigrettee = null;
  private RealmList<CategoryModel> bidi = null;
  private RealmList<CategoryModel> match = null;

  public RealmList<CategoryModel> getCigrettee() {
    return cigrettee;
  }

  public void setCigrettee(RealmList<CategoryModel> cigrettee) {
    this.cigrettee = cigrettee;
  }

  public RealmList<CategoryModel> getBidi() {
    return bidi;
  }

  public void setBidi(RealmList<CategoryModel> bidi) {
    this.bidi = bidi;
  }

  public RealmList<CategoryModel> getMatch() {
    return match;
  }

  public void setMatch(RealmList<CategoryModel> match) {
    this.match = match;
  }
}
