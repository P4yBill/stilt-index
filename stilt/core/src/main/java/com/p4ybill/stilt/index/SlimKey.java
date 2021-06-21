package com.p4ybill.stilt.index;

public class SlimKey implements Key {
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
    public float getLatitude() {
        return this.common.getLatitude();
    }

    @Override
    public float getLongitude() {
        return this.common.getLongitude();
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
}
