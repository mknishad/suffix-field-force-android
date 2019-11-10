package com.suffix.fieldforce.model;

public class Issue {

    private String issueName;
    private String issueDescription;
    private String userName;
    private String userExperties;
    private String issueStatus;

    public Issue() {
    }

    public Issue(String issueName, String issueDescription, String userName, String userExperties, String issueStatus) {
        this.issueName = issueName;
        this.issueDescription = issueDescription;
        this.userName = userName;
        this.userExperties = userExperties;
        this.issueStatus = issueStatus;
    }

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
}
