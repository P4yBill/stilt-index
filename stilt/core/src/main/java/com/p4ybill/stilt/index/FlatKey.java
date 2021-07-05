package com.p4ybill.stilt.index;

public class FlatKey extends SlimCommon implements Key {
    String keyword;
    int keywordFrequency;

    public FlatKey(int id, double y, double x, String keyword, long timestamp, int totalWordFrequency, int keywordFrequency) {
        super(id, y, x, timestamp, totalWordFrequency);
        this.keyword = keyword;
        this.keywordFrequency = keywordFrequency;
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public int getKeywordFrequency() {
        return keywordFrequency;
    }
}
