package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThanaInfo {

    @SerializedName("thanaName")
    @Expose
    private String thanaName;
    @SerializedName("thanaId")
    @Expose
    private String thanaId;

    public String getThanaName() {
        return thanaName;
    }

    public void setThanaName(String thanaName) {
        this.thanaName = thanaName;
    }

    public String getThanaId() {
        return thanaId;
    }

    public void setThanaId(String thanaId) {
        this.thanaId = thanaId;
    }

}