package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class BillResponseData {
    @SerializedName("billDetailsObj")
    @Expose
    private BillDetailsObj billDetailsObj;
    @SerializedName("billListObj")
    @Expose
    private BillListObj billListObj;
    @SerializedName("billApproveObj")
    @Expose
    private BillApproveObj billApproveObj;

    public BillDetailsObj getBillDetailsObj() {
        return billDetailsObj;
    }

    public void setBillDetailsObj(BillDetailsObj billDetailsObj) {
        this.billDetailsObj = billDetailsObj;
    }

    public BillListObj getBillListObj() {
        return billListObj;
    }

    public void setBillListObj(BillListObj billListObj) {
        this.billListObj = billListObj;
    }

    public BillApproveObj getBillApproveObj() {
        return billApproveObj;
    }

    public void setBillApproveObj(BillApproveObj billApproveObj) {
        this.billApproveObj = billApproveObj;
    }

}
