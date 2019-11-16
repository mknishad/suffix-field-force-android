package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictInfo {
    @SerializedName("disttictId")
    @Expose
    private String disttictId;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("thanaTotal")
    @Expose
    private String thanaTotal;
    @SerializedName("thanaInfo")
    @Expose
    private List<ThanaInfo> thanaInfo = null;

    public String getDisttictId() {
        return disttictId;
    }

    public void setDisttictId(String disttictId) {
        this.disttictId = disttictId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getThanaTotal() {
        return thanaTotal;
    }

    public void setThanaTotal(String thanaTotal) {
        this.thanaTotal = thanaTotal;
    }

    public List<ThanaInfo> getThanaInfo() {
        return thanaInfo;
    }

    public void setThanaInfo(List<ThanaInfo> thanaInfo) {
        this.thanaInfo = thanaInfo;
    }
}
