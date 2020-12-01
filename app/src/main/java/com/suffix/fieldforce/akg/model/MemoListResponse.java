package com.suffix.fieldforce.akg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemoListResponse implements Parcelable {

  @SerializedName("invNo")
  @Expose
  private String invNo;
  @SerializedName("discountAmount")
  @Expose
  private Double discountAmount;
  @SerializedName("prevDue")
  @Expose
  private Double prevDue;
  @SerializedName("receivedAmount")
  @Expose
  private Double receivedAmount;
  @SerializedName("totalAmount")
  @Expose
  private Double totalAmount;
  @SerializedName("txnDt")
  @Expose
  private String txnDt;
  @SerializedName("consumer")
  @Expose
  private Consumer consumer;
  @SerializedName("salesRep")
  @Expose
  private SalesRep salesRep;

  protected MemoListResponse(Parcel in) {
    invNo = in.readString();
    if (in.readByte() == 0) {
      discountAmount = null;
    } else {
      discountAmount = in.readDouble();
    }
    if (in.readByte() == 0) {
      prevDue = null;
    } else {
      prevDue = in.readDouble();
    }
    if (in.readByte() == 0) {
      receivedAmount = null;
    } else {
      receivedAmount = in.readDouble();
    }
    if (in.readByte() == 0) {
      totalAmount = null;
    } else {
      totalAmount = in.readDouble();
    }
    txnDt = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(invNo);
    if (discountAmount == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(discountAmount);
    }
    if (prevDue == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(prevDue);
    }
    if (receivedAmount == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(receivedAmount);
    }
    if (totalAmount == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeDouble(totalAmount);
    }
    dest.writeString(txnDt);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MemoListResponse> CREATOR = new Creator<MemoListResponse>() {
    @Override
    public MemoListResponse createFromParcel(Parcel in) {
      return new MemoListResponse(in);
    }

    @Override
    public MemoListResponse[] newArray(int size) {
      return new MemoListResponse[size];
    }
  };

  public String getInvNo() {
    return invNo;
  }

  public void setInvNo(String invNo) {
    this.invNo = invNo;
  }

  public Double getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(Double discountAmount) {
    this.discountAmount = discountAmount;
  }

  public Double getPrevDue() {
    return prevDue;
  }

  public void setPrevDue(Double prevDue) {
    this.prevDue = prevDue;
  }

  public Double getReceivedAmount() {
    return receivedAmount;
  }

  public void setReceivedAmount(Double receivedAmount) {
    this.receivedAmount = receivedAmount;
  }

  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTxnDt() {
    return txnDt;
  }

  public void setTxnDt(String txnDt) {
    this.txnDt = txnDt;
  }

  public Consumer getConsumer() {
    return consumer;
  }

  public void setConsumer(Consumer consumer) {
    this.consumer = consumer;
  }

  public SalesRep getSalesRep() {
    return salesRep;
  }

  public void setSalesRep(SalesRep salesRep) {
    this.salesRep = salesRep;
  }

}