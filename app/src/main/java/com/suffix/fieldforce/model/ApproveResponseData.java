package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApproveResponseData {

    @SerializedName("approveDate")
    @Expose
    private String approveDate;
    @SerializedName("managerStatus")
    @Expose
    private String managerStatus;
    @SerializedName("approveEmpId")
    @Expose
    private String approveEmpId;
    @SerializedName("approveEmpName")
    @Expose
    private String approveEmpName;
    @SerializedName("billApproveId")
    @Expose
    private String billApproveId;

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }

    public String getApproveEmpId() {
        return approveEmpId;
    }

    public void setApproveEmpId(String approveEmpId) {
        this.approveEmpId = approveEmpId;
    }

    public String getApproveEmpName() {
        return approveEmpName;
    }

    public void setApproveEmpName(String approveEmpName) {
        this.approveEmpName = approveEmpName;
    }

    public String getBillApproveId() {
        return billApproveId;
    }

    public void setBillApproveId(String billApproveId) {
        this.billApproveId = billApproveId;
    }

}