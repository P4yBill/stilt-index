package com.p4ybill.stilt.index;

public interface Key {
    int getId();

    float getLatitude();

    float getLongitude();

    String getKeyword();

    long getTimestamp();

    int getKeywordFrequency();

    int getTotalWordFrequency();
}
