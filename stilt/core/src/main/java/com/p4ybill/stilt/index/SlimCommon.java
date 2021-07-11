package com.p4ybill.stilt.index;

public class SlimCommon {
    private int id;
    private double y;
    private double x;
    private long date;
    private int totalWordFrequency;

    public SlimCommon(int id, double y, double x, long date, int totalWordFrequency) {
        this.id = id;
        this.y = y;
        this.x = x;
        this.date = date;
        this.totalWordFrequency = totalWordFrequency;
    }

    public int getId() {
        return id;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public long getDate() {
        return date;
    }

    public int getTotalWordFrequency() {
        return totalWordFrequency;
    }

    @Override
    public String toString() {
        return "SlimCommon{" +
                "id=" + id +
                ", y=" + y +
                ", x=" + x +
                ", date=" + date +
                '}';
    }
}
