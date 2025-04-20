package com.nibm.sos_app;

public class User {

    private String fullName;
    private String mobile;
    private String relationship;


    public User() {
    }


    public User(String fullName, String mobile, String relationship) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.relationship = relationship;
    }


    public String getFullName() {
        return fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRelationship() {
        return relationship;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
