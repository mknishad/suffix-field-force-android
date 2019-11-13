package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseData")
    @Expose
    private User responseData;
    @SerializedName("ResponseText")
    @Expose
    private String responseText;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public User getResponseData() {
        return responseData;
    }

    public void setResponseData(User responseData) {
        this.responseData = responseData;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

}
