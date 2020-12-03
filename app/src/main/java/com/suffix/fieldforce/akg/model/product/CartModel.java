package com.suffix.fieldforce.akg.model.product;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CartModel extends RealmObject implements Parcelable {

  @SerializedName("productCatId")
  @Expose
  private Integer productCatId;
  @SerializedName("productId")
  @Expose
  private Integer productId;
  @SerializedName("productCode")
  @Expose
  private String productCode;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("productCatName")
  @Expose
  private String productCatName;
  @SerializedName("productImage")
  @Expose
  private String productImage;
  @SerializedName("sellingRate")
  @Expose
  private Double sellingRate;
  @SerializedName("qty1")
  @Expose
  private Integer qty1;
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
  @SerializedName("totalMemo")
  @Expose
  private Integer totalMemo;

  private String orderQuantity;

  public CartModel(){

  };

  protected CartModel(Parcel in) {
    if (in.readByte() == 0) {
      productCatId = null;
    } else {
      productCatId = in.readInt();
    }
    if (in.readByte() == 0) {
      productId = null;
    } else {
      productId = in.readInt();
    }
    productCode = in.readString();
    productName = in.readString();
    productCatName = in.readString();
    productImage = in.readString();
    if (in.readByte() == 0) {
      sellingRate = null;
    } else {
      sellingRate = in.readDouble();
    }
    if (in.readByte() == 0) {
      qty1 = null;
    } else {
      qty1 = in.readInt();
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
      totalMemo = null;
    } else {
      totalMemo = in.readInt();
    }
    orderQuantity = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (productCatId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(productCatId);
    }
    if (productId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(productId);
    }
    dest.writeString(productCode);
    dest.writeString(productName);
    dest.writeString(productCatName);
    dest.writeString(productImage);
    if (sellingRate == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(sellingRate);
    }
    if (qty1 == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(qty1);
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
    if (totalMemo == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(totalMemo);
    }
    dest.writeString(orderQuantity);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
    @Override
    public CartModel createFromParcel(Parcel in) {
      return new CartModel(in);
    }

    @Override
    public CartModel[] newArray(int size) {
      return new CartModel[size];
    }
  };

  public Integer getProductCatId() {
    return productCatId;
  }

  public void setProductCatId(Integer productCatId) {
    this.productCatId = productCatId;
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
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductCatName() {
    return productCatName;
  }

  public void setProductCatName(String productCatName) {
    this.productCatName = productCatName;
  }

  public String getProductImage() {
    return productImage;
  }

  public void setProductImage(String productImage) {
    this.productImage = productImage;
  }

  public Double getSellingRate() {
    return sellingRate;
  }

  public void setSellingRate(Double sellingRate) {
    this.sellingRate = sellingRate;
  }

  public Integer getQty1() {
    return qty1;
  }

  public void setQty1(Integer qty1) {
    this.qty1 = qty1;
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
    return totalMemo;
  }

  public void setTotalMemo(Integer totalMemo) {
    this.totalMemo = totalMemo;
  }

  public String getOrderQuantity() {
    return orderQuantity;
  }

  public void setOrderQuantity(String orderQuantity) {
    this.orderQuantity = orderQuantity;
  }
}
