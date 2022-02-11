package com.nextradioapp.nextradio.ottos;

public class NRInitCompleted {
    public static int STATUS_CODE_REGISTER_FAILED;
    public static int STATUS_CODE_SUCCESS;
    public static int STATUS_CODE_UNKNOWN;
    public int statusCode;

    public NRInitCompleted() {
        this.statusCode = STATUS_CODE_UNKNOWN;
    }

    static {
        STATUS_CODE_UNKNOWN = 0;
        STATUS_CODE_SUCCESS = 1;
        STATUS_CODE_REGISTER_FAILED = 2;
    }

    public String toString() {
        return "NRInitCompleted statusCode:" + this.statusCode;
    }
}
