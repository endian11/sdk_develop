package com.travelrely.sdk;

public class SimpleResult {
    public boolean success;
    public String errorMsg;

    public SimpleResult(boolean success, String errorMsg) {
        super();
        this.success = success;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "SimpleResult [success=" + success + ", errorMsg=" + errorMsg + "]";
    }

}