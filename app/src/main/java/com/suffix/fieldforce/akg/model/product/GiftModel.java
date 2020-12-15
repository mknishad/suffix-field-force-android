package com.suffix.fieldforce.akg.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GiftModel extends RealmObject implements Parcelable {
  @PrimaryKey
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("giftName")
  @Expose
  private String giftName;
  @SerializedName("sliderQty")
  @Expose
  private Integer sliderQty;
  @SerializedName("isMemo")
  @Expose
  private Integer isMemo;
  @SerializedName("productId")
  @Expose
  private Integer productId;
  @SerializedName("imageFileName")
  @Expose
  private String imageFileName;
  @SerializedName("inHandQty")
  @Expose
  private Integer inHandQty;
  @SerializedName("salesQty")
  @Expose
  private Integer salesQty;
  @SerializedName("momNo")
  @Expose
  private Integer momNo;
  private int productQuantity;

  public GiftModel() {
  }

  protected GiftModel(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readInt();
    }
    giftName = in.readString();
    if (in.readByte() == 0) {
      sliderQty = null;
    } else {
      sliderQty = in.readInt();
    }
    if (in.readByte() == 0) {
      isMemo = null;
    } else {
      isMemo = in.readInt();
    }
    if (in.readByte() == 0) {
      productId = null;
    } else {
      productId = in.readInt();
    }
    imageFileName = in.readString();
    if (in.readByte() == 0) {
      inHandQty = null;
    } else {
      inHandQty = in.readInt();
    }
    if (in.readByte() == 0) {
      salesQty = null;
    } else {
      salesQty = in.readInt();
    }
    if (in.readByte() == 0) {
      momNo = null;
    } else {
      momNo = in.readInt();
    }
    if (in.readByte() == 0) {
      productQuantity = 0;
    } else {
      productQuantity = in.readInt();
    }
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(id);
    }
    dest.writeString(giftName);
    if (sliderQty == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(sliderQty);
    }
    if (isMemo == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isMemo);
    }
    if (productId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(productId);
    }
    dest.writeString(imageFileName);
    if (inHandQty == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(inHandQty);
    }
    if (salesQty == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(salesQty);
    }
    if (momNo == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(momNo);
    }
    if (productQuantity == 0) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(productQuantity);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<GiftModel> CREATOR = new Creator<GiftModel>() {
    @Override
    public GiftModel createFromParcel(Parcel in) {
      return new GiftModel(in);
    }

    @Override
    public GiftModel[] newArray(int size) {
      return new GiftModel[size];
    }
  };

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getGiftName() {
    return giftName;
  }

  public void setGiftName(String giftName) {
    this.giftName = giftName;
  }

  public Integer getSliderQty() {
    return sliderQty;
  }

  public void setSliderQty(Integer sliderQty) {
    this.sliderQty = sliderQty;
  }

  public Integer getIsMemo() {
    return isMemo;
  }

  public void setIsMemo(Integer isMemo) {
    this.isMemo = isMemo;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public String getImageFileName() {
    return imageFileName;
  }

  public void setImageFileName(String imageFileName) {
    this.imageFileName = imageFileName;
  }

  public Integer getInHandQty() {
    return inHandQty;
  }

  public void setInHandQty(Integer inHandQty) {
    this.inHandQty = inHandQty;
  }

  public Integer getSalesQty() {
    return salesQty;
  }

  public void setSalesQty(Integer salesQty) {
    this.salesQty = salesQty;
  }

  public Integer getMomNo() {
    return momNo;
  }

  public void setMomNo(Integer momNo) {
    this.momNo = momNo;
  }

  public int getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(int productQuantity) {
    this.productQuantity = productQuantity;
  }

  public static Creator<GiftModel> getCREATOR() {
    return CREATOR;
  }
}
