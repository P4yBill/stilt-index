package com.p4ybill.stilt.index;

import java.util.List;
import java.util.Optional;

public class Query {
    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;

    private Long minTimestamp;
    private Long maxTimestamp;

    private String word;

    private Range rangeX;
    private Range rangeY;
    private Range rangeTimestamp;
    private Range rangeWord;

    private List<String> words;

    protected Query(int bitsPerDimension) {
        this.rangeX = SearchUtils.getRange(bitsPerDimension);
        this.rangeY = SearchUtils.getRange(bitsPerDimension);
        this.rangeTimestamp = SearchUtils.getRange(bitsPerDimension);
    }

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

    public Optional<Long> getMaxTimestamp() {
        return Optional.ofNullable(maxTimestamp);
    }

    public void setMaxTimestamp(Long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public Optional<String> getWord() {
        return Optional.ofNullable(word);
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Optional<List<String>> getWords() {
        return Optional.ofNullable(words);
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public boolean intersects(Key k) {
        FourDimensionalKey key = (FourDimensionalKey)k;

        return (this.getMinX().isEmpty() || key.getX() >= this.getMinX().get()) &&
                (this.getMinY().isEmpty() || key.getY() >= this.getMinY().get()) &&
                (this.getMaxX().isEmpty() || key.getX() <= this.getMaxX().get()) &&
                (this.getMaxY().isEmpty() || key.getY() <= this.getMaxY().get()) &&

                (this.getMinTimestamp().isEmpty() || key.getDate() >= this.getMinTimestamp().get()) &&
                (this.getMaxTimestamp().isEmpty() || key.getDate() <= this.getMaxTimestamp().get()) &&

                (this.getWord().isEmpty() || this.getWord().get().equals(key.getKeyword()));

    }

    public Range getRangeX() {
        return rangeX;
    }

    public Range getRangeY() {
        return rangeY;
    }

    public Range getRangeTimestamp() {
        return rangeTimestamp;
    }

    public void setRangeX(Range rangeX) {
        this.rangeX = rangeX;
    }

    public void setRangeY(Range rangeY) {
        this.rangeY = rangeY;
    }

    public void setRangeTimestamp(Range rangeTimestamp) {
        this.rangeTimestamp = rangeTimestamp;
    }

    public Range getRangeWord() {
        return rangeWord;
    }

    public void setRangeWord(Range rangeWord) {
        this.rangeWord = rangeWord;
    }

    public boolean intersects(Range range, int dimension){
        Range dRange = getRangeForDimension(dimension);

        return dRange != null && range.intersects(dRange);
    }

    public Range getRangeForDimension(int dimension){
        if(dimension == 0){
            return this.getRangeX();
        }else if(dimension == 1){
            return this.getRangeY();
        }else if(dimension == 2){
            return this.getRangeWord();
        }else if(dimension == 3){
            return this.getRangeTimestamp();
        }

        return null;
    }
}
