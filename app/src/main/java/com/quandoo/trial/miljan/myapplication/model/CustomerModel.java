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

    public int getId() {return mId; }

    public String getLabel() {
        return mFirstName;
    }

    public boolean contains(String text) {
        return (mFirstName.toLowerCase().contains(text.toLowerCase()) || mLastName.toLowerCase().contains(text.toLowerCase()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!CustomerModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final CustomerModel other = (CustomerModel) obj;
        return (this.mId == other.getId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.mFirstName != null ? this.mFirstName.hashCode() : 0);
        hash = 53 * hash + this.mId;
        return hash;
    }}
