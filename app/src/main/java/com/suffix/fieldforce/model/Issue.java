package com.suffix.fieldforce.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Issue implements Parcelable {

    private String issueName;
    private String issueDescription;
    private String userName;
    private String userExperties;
    private String issueStatus;

    public String getIssueName() {
        return issueName;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserExperties() {
        return userExperties;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public static Creator<Issue> getCREATOR() {
        return CREATOR;
    }

    public Issue() {
    }

    public Issue(String issueName, String issueDescription, String userName, String userExperties, String issueStatus) {
        this.issueName = issueName;
        this.issueDescription = issueDescription;
        this.userName = userName;
        this.userExperties = userExperties;
        this.issueStatus = issueStatus;
    }

    protected Issue(Parcel in) {
        issueName = in.readString();
        issueDescription = in.readString();
        userName = in.readString();
        userExperties = in.readString();
        issueStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(issueName);
        dest.writeString(issueDescription);
        dest.writeString(userName);
        dest.writeString(userExperties);
        dest.writeString(issueStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

}
