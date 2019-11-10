package com.suffix.fieldforce.model.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignedTask {
    @SerializedName("responseData")
    @Expose
    private List<AssignTaskItem> responseData = null;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseMsg")
    @Expose
    private String responseMsg;

    public List<AssignTaskItem> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<AssignTaskItem> responseData) {
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
