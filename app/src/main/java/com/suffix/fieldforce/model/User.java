package com.suffix.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("UserMobile")
    @Expose
    private String userMobile;
    @SerializedName("UserPhone1")
    @Expose
    private String userPhone1;
    @SerializedName("UserPhone2")
    @Expose
    private String userPhone2;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("UserGender")
    @Expose
    private String userGender;
    @SerializedName("UserBloodGroup")
    @Expose
    private String userBloodGroup;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("UserPicture")
    @Expose
    private String userPicture;
    @SerializedName("MemId")
    @Expose
    private Integer memId;
    @SerializedName("UserEmail")
    @Expose
    private String userEmail;
    @SerializedName("UserType")
    @Expose
    private String userType;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserPhone1() {
        return userPhone1;
    }

    public void setUserPhone1(String userPhone1) {
        this.userPhone1 = userPhone1;
    }

    public String getUserPhone2() {
        return userPhone2;
    }

    public void setUserPhone2(String userPhone2) {
        this.userPhone2 = userPhone2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserBloodGroup() {
        return userBloodGroup;
    }

    public void setUserBloodGroup(String userBloodGroup) {
        this.userBloodGroup = userBloodGroup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
