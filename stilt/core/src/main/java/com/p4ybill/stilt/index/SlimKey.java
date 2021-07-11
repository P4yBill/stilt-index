package com.p4ybill.stilt.index;

public class SlimKey implements FourDimensionalKey {
    SlimCommon common;
    String keyword;
    int keywordFrequency;

    public SlimKey(SlimCommon common, String keyword, int keywordFrequency) {
        this.common = common;
        this.keyword = keyword;
        this.keywordFrequency = keywordFrequency;
    }

    @Override
    public int getId() {
        return this.common.getId();
    }

    @Override
    public double getY() {
        return this.common.getY();
    }

    @Override
    public double getX() {
        return this.common.getX();
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public long getDate() {
        return this.common.getDate();
    }

    @Override
    public int getKeywordFrequency() {
        return keywordFrequency;
    }

    @Override
    public int getTotalWordFrequency() {
        return this.common.getTotalWordFrequency();
    }

    @Override
    public String toString() {
        return "SlimKey{" +
                "common=" + common.toString() +
                ", keyword='" + keyword + '\'' +
                ", keywordFrequency=" + keywordFrequency +
                '}';
    }
}
