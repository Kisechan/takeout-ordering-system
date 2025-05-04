package com.takeout.model;

public enum Role {
    customer("顾客"),
    merchant("商家");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
