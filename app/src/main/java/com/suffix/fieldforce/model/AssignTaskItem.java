package com.suffix.fieldforce.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignTaskItem implements Parcelable {

    @SerializedName("ticketTitle")
    @Expose
    private String ticketTitle;
    @SerializedName("consumerAddress")
    @Expose
    private String consumerAddress;
    @SerializedName("consumerThana")
    @Expose
    private String consumerThana;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("ticketRemark")
    @Expose
    private String ticketRemark;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("consumerDistrict")
    @Expose
    private String consumerDistrict;
    @SerializedName("ticketCateroryCode")
    @Expose
    private String ticketCateroryCode;
    @SerializedName("ticketStartDate")
    @Expose
    private String ticketStartDate;
    @SerializedName("ticketEndDate")
    @Expose
    private String ticketEndDate;
    @SerializedName("ticketStatus")
    @Expose
    private String ticketStatus;
    @SerializedName("ticketStatusText")
    @Expose
    private String ticketStatusText;
    @SerializedName("ticketCateroryTitle")
    @Expose
    private String ticketCateroryTitle;
    @SerializedName("ticketCode")
    @Expose
    private String ticketCode;
    @SerializedName("ticketId")
    @Expose
    private String ticketId;
    @SerializedName("consumerMobile")
    @Expose
    private String consumerMobile;
    @SerializedName("consumerName")
    @Expose
    private String consumerName;

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public String getConsumerThana() {
        return consumerThana;
    }

    public void setConsumerThana(String consumerThana) {
        this.consumerThana = consumerThana;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTicketRemark() {
        return ticketRemark;
    }

    public void setTicketRemark(String ticketRemark) {
        this.ticketRemark = ticketRemark;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getConsumerDistrict() {
        return consumerDistrict;
    }

    public void setConsumerDistrict(String consumerDistrict) {
        this.consumerDistrict = consumerDistrict;
    }

    public String getTicketCateroryCode() {
        return ticketCateroryCode;
    }

    public void setTicketCateroryCode(String ticketCateroryCode) {
        this.ticketCateroryCode = ticketCateroryCode;
    }

    public String getTicketStartDate() {
        return ticketStartDate;
    }

    public void setTicketStartDate(String ticketStartDate) {
        this.ticketStartDate = ticketStartDate;
    }

    public String getTicketEndDate() {
        return ticketEndDate;
    }

    public void setTicketEndDate(String ticketEndDate) {
        this.ticketEndDate = ticketEndDate;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketStatusText() {
        return ticketStatusText;
    }

    public void setTicketStatusText(String ticketStatusText) {
        this.ticketStatusText = ticketStatusText;
    }

    public String getTicketCateroryTitle() {
        return ticketCateroryTitle;
    }

    public void setTicketCateroryTitle(String ticketCateroryTitle) {
        this.ticketCateroryTitle = ticketCateroryTitle;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getConsumerMobile() {
        return consumerMobile;
    }

    public void setConsumerMobile(String consumerMobile) {
        this.consumerMobile = consumerMobile;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    protected AssignTaskItem(Parcel in) {
        ticketTitle = in.readString();
        consumerAddress = in.readString();
        consumerThana = in.readString();
        orderId = in.readString();
        ticketRemark = in.readString();
        deviceName = in.readString();
        consumerDistrict = in.readString();
        ticketCateroryCode = in.readString();
        ticketStartDate = in.readString();
        ticketEndDate = in.readString();
        ticketStatus = in.readString();
        ticketStatusText = in.readString();
        ticketCateroryTitle = in.readString();
        ticketCode = in.readString();
        ticketId = in.readString();
        consumerMobile = in.readString();
        consumerName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ticketTitle);
        parcel.writeString(consumerAddress);
        parcel.writeString(consumerThana);
        parcel.writeString(orderId);
        parcel.writeString(ticketRemark);
        parcel.writeString(deviceName);
        parcel.writeString(consumerDistrict);
        parcel.writeString(ticketCateroryCode);
        parcel.writeString(ticketStartDate);
        parcel.writeString(ticketEndDate);
        parcel.writeString(ticketStatus);
        parcel.writeString(ticketStatusText);
        parcel.writeString(ticketCateroryTitle);
        parcel.writeString(ticketCode);
        parcel.writeString(ticketId);
        parcel.writeString(consumerMobile);
        parcel.writeString(consumerName);
    }

    public static final Creator<AssignTaskItem> CREATOR = new Creator<AssignTaskItem>() {
        @Override
        public AssignTaskItem createFromParcel(Parcel in) {
            return new AssignTaskItem(in);
        }

        @Override
        public AssignTaskItem[] newArray(int size) {
            return new AssignTaskItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
