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
  private Long id;
  @SerializedName("productId")
  @Expose
  private Integer productId;
  @SerializedName("productCode")
  @Expose
  private String productCode;
  @SerializedName("giftName")
  @Expose
  private String giftName;
  @SerializedName("productCatName")
  @Expose
  private String productCatName;
  @SerializedName("imageFileName")
  @Expose
  private String imageFileName;
  @SerializedName("sellingRate")
  @Expose
  private Double sellingRate;
  @SerializedName("momNo")
  @Expose
  private Integer memoNo;
  @SerializedName("uom1")
  @Expose
  private String uom1;
  @SerializedName("qty2")
  @Expose
  private Integer qty2;
  @SerializedName("uom2")
  @Expose
  private String uom2;
  @SerializedName("inHandQty")
  @Expose
  private Integer inHandQty;
  @SerializedName("salesQty")
  @Expose
  private Integer salesQty;
  @SerializedName("isMemo")
  @Expose
  private Integer isMemo;

  private String orderQuantity;

  public GiftModel(){

  };

  protected GiftModel(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readLong();
    }
    if (in.readByte() == 0) {
      productId = null;
    } else {
      productId = in.readInt();
    }
    productCode = in.readString();
    giftName = in.readString();
    productCatName = in.readString();
    imageFileName = in.readString();
    if (in.readByte() == 0) {
      sellingRate = null;
    } else {
      sellingRate = in.readDouble();
    }
    if (in.readByte() == 0) {
      memoNo = null;
    } else {
      memoNo = in.readInt();
    }
    uom1 = in.readString();
    if (in.readByte() == 0) {
      qty2 = null;
    } else {
      qty2 = in.readInt();
    }
    uom2 = in.readString();
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
      isMemo = null;
    } else {
      isMemo = in.readInt();
    }
    orderQuantity = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeLong(id);
    }
    if (productId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(productId);
    }
    dest.writeString(productCode);
    dest.writeString(giftName);
    dest.writeString(productCatName);
    dest.writeString(imageFileName);
    if (sellingRate == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(sellingRate);
    }
    if (memoNo == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(memoNo);
    }
    dest.writeString(uom1);
    if (qty2 == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(qty2);
    }
    dest.writeString(uom2);
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
    if (isMemo == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(isMemo);
    }
    dest.writeString(orderQuantity);
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

  public Long getProductCatId() {
    return id;
  }

  public void setProductCatId(Long id) {
    this.id = id;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getProductName() {
    return giftName;
  }

  public void setProductName(String giftName) {
    this.giftName = giftName;
  }

  public String getProductCatName() {
    return productCatName;
  }

  public void setProductCatName(String productCatName) {
    this.productCatName = productCatName;
  }

  public String getProductImage() {
    return imageFileName;
  }

  public void setProductImage(String imageFileName) {
    this.imageFileName = imageFileName;
  }

  public Double getSellingRate() {
    return sellingRate;
  }

  public void setSellingRate(Double sellingRate) {
    this.sellingRate = sellingRate;
  }

  public Integer getQty1() {
    return memoNo;
  }

  public void setQty1(Integer memoNo) {
    this.memoNo = memoNo;
  }

  public String getUom1() {
    return uom1;
  }

  public void setUom1(String uom1) {
    this.uom1 = uom1;
  }

  public Integer getQty2() {
    return qty2;
  }

  public void setQty2(Integer qty2) {
    this.qty2 = qty2;
  }

  public String getUom2() {
    return uom2;
  }

  public void setUom2(String uom2) {
    this.uom2 = uom2;
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

  public Integer getTotalMemo() {
    return isMemo;
  }

  public void setTotalMemo(Integer isMemo) {
    this.isMemo = isMemo;
  }

  public String getOrderQuantity() {
    return orderQuantity;
  }

  public void setOrderQuantity(String orderQuantity) {
    this.orderQuantity = orderQuantity;
  }
}
