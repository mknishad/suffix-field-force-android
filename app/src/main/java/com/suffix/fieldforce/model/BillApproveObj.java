package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillApproveObj {

    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseData")
    @Expose
    private List<ApproveResponseData> responseData = null;
    @SerializedName("ResponseText")
    @Expose
    private String responseText;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<ApproveResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ApproveResponseData> responseData) {
        this.responseData = responseData;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

}
