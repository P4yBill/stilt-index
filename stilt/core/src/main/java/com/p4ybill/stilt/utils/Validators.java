package com.p4ybill.stilt.utils;

public class Validators {
    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
}
