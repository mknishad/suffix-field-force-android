package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillDetailsTypeTwo {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseData")
    @Expose
    private BillResponseData responseData;
    @SerializedName("ResponseText")
    @Expose
    private String responseText;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public BillResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(BillResponseData responseData) {
        this.responseData = responseData;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
