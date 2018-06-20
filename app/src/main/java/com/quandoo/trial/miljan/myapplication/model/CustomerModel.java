package com.quandoo.trial.miljan.myapplication.model;

public class CustomerModel {
    private int mId;
    private String mFirstName;
    private String mLastName;

    public CustomerModel(int id, String firstName, String lastName) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
    }

    public String getLabel() {
        return mFirstName;
    }
}
