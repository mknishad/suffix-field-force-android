package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictData {
    @SerializedName("responseData")
    @Expose
    private List<DistrictInfo> responseData = null;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseMsg")
    @Expose
    private String responseMsg;

    public List<DistrictInfo> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<DistrictInfo> responseData) {
        this.responseData = responseData;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
