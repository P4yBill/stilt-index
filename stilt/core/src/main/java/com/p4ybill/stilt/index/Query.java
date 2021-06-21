package com.p4ybill.stilt.index;

import java.util.List;
import java.util.Optional;

public class Query {
    Double minX;
    Double maxX;
    Double minY;
    Double maxY;

    Long minTimestamp;
    Long maxTimestamp;

    List<String> words;

    public Query(){};

    public Optional<Double> getMinX() {
        return Optional.ofNullable(minX);
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Optional<Double> getMaxX() {
        return Optional.ofNullable(maxX);
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Optional<Double> getMinY() {
        return Optional.ofNullable(minY);
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Optional<Double> getMaxY() {
        return Optional.ofNullable(maxY);
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public Optional<Long> getMinTimestamp() {
        return Optional.ofNullable(minTimestamp);
    }

    public void setMinTimestamp(Long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(Long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public Optional<List<String>> getWords() {
        return Optional.ofNullable(words);
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
