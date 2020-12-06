package com.suffix.fieldforce.akg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

import io.realm.RealmObject;

public class InvoiceRequest extends RealmObject implements Parcelable {
  @SerializedName("invoiceType")
  @Expose
  private String invoiceType;
  @SerializedName("customerId")
  @Expose
  private int customerId;
  @SerializedName("invoiceDate")
  @Expose
  private long invoiceDate;
  @SerializedName("invoiceId")
  @Expose
  private String invoiceId;
  @SerializedName("invoiceProducts")
  @Expose
  private RealmList<InvoiceProduct> invoiceProducts;
  @SerializedName("salesRepId")
  @Expose
  private int salesRepId;
  @SerializedName("totalAmount")
  @Expose
  private double totalAmount;
  private boolean status;
  @SerializedName("customerName")
  @Expose
  private String customerName;
  @SerializedName("customerAddress")
  @Expose
  private String customerAddress;
  @SerializedName("recievedAmount")
  @Expose
  private double recievedAmount;

  public InvoiceRequest(){

  }

  public InvoiceRequest(String invoiceType, int customerId, long invoiceDate, String invoiceId,
                        RealmList<InvoiceProduct> invoiceProducts, int salesRepId, double totalAmount,
                        String customerName, String customerAddress, double recievedAmount) {
    this.invoiceType = invoiceType;
    this.customerId = customerId;
    this.invoiceDate = invoiceDate;
    this.invoiceId = invoiceId;
    this.invoiceProducts = invoiceProducts;
    this.salesRepId = salesRepId;
    this.totalAmount = totalAmount;
    this.customerName = customerName;
    this.customerAddress = customerAddress;
    this.recievedAmount = recievedAmount;
  }

  protected InvoiceRequest(Parcel in) {
    invoiceType = in.readString();
    customerId = in.readInt();
    invoiceDate = in.readLong();
    invoiceId = in.readString();
    salesRepId = in.readInt();
    totalAmount = in.readDouble();
    status = in.readByte() != 0;
    this.invoiceProducts = new RealmList<>();
    this.invoiceProducts.addAll(in.createTypedArrayList(InvoiceProduct.CREATOR));
    customerName = in.readString();
    customerAddress = in.readString();
    recievedAmount = in.readDouble();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(invoiceType);
    dest.writeInt(customerId);
    dest.writeLong(invoiceDate);
    dest.writeString(invoiceId);
    dest.writeInt(salesRepId);
    dest.writeDouble(totalAmount);
    dest.writeByte((byte) (status ? 1 : 0));
    dest.writeTypedList(this.invoiceProducts);
    dest.writeString(customerName);
    dest.writeString(customerAddress);
    dest.writeDouble(recievedAmount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<InvoiceRequest> CREATOR = new Creator<InvoiceRequest>() {
    @Override
    public InvoiceRequest createFromParcel(Parcel in) {
      return new InvoiceRequest(in);
    }

    @Override
    public InvoiceRequest[] newArray(int size) {
      return new InvoiceRequest[size];
    }
  };

  @Override
  public String toString() {
    return "InvoiceRequest{" +
        "invoiceType='" + invoiceType + '\'' +
        ", customerId=" + customerId +
        ", invoiceDate=" + invoiceDate +
        ", invoiceId='" + invoiceId + '\'' +
        ", invoiceProducts=" + invoiceProducts +
        ", salesRepId=" + salesRepId +
        ", totalAmount=" + totalAmount +
        ", status=" + status +
        ", customerName='" + customerName + '\'' +
        ", customerAddress='" + customerAddress + '\'' +
        ", recievedAmount=" + recievedAmount +
        '}';
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public long getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(long invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
  }

  public RealmList<InvoiceProduct> getInvoiceProducts() {
    return invoiceProducts;
  }

  public void setInvoiceProducts(RealmList<InvoiceProduct> invoiceProducts) {
    this.invoiceProducts = invoiceProducts;
  }

  public int getSalesRepId() {
    return salesRepId;
  }

  public void setSalesRepId(int salesRepId) {
    this.salesRepId = salesRepId;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerAddress() {
    return customerAddress;
  }

  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public double getRecievedAmount() {
    return recievedAmount;
  }

  public void setRecievedAmount(double recievedAmount) {
    this.recievedAmount = recievedAmount;
  }
}
