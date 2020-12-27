package com.p4ybill.stilt.index;

public class SlimCommon {
    private int id;
    private float latitude;
    private float longitude;
    private long date;
    private int totalWordFrequency;

    public SlimCommon(int id, float latitude, float longitude, long date, int totalWordFrequency) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.totalWordFrequency = totalWordFrequency;
    }

    public int getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public long getDate() {
        return date;
    }

    public int getTotalWordFrequency() {
        return totalWordFrequency;
    }
}
