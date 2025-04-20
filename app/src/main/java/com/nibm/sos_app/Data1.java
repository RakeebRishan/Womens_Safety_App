package com.nibm.sos_app;

public class Data1 {
    private String fullName;
    private String mobile;
    private String relationship;

    public Data1() {
        // Default constructor required for calls to DataSnapshot.getValue(Data1.class)
    }

    public Data1(String fullName, String mobile, String relationship) {
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
}

