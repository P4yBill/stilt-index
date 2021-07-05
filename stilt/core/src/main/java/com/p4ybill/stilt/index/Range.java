package com.p4ybill.stilt.index;

import com.p4ybill.stilt.utils.Validators;

public class Range {
    private int lowerBound;
    private int upperBound;

    Range(int lowerBound, int upperBound) {
        Validators.checkArgument(lowerBound <= upperBound, "Range lower bound should be lower than or equal the upper bound");
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public boolean intersects(Range range) {
        return (lowerBound >= range.getLowerBound() && lowerBound <= range.getUpperBound()) ||
                (upperBound >= range.getLowerBound() && upperBound <= range.getUpperBound()) ||
                (lowerBound <= range.getLowerBound() && upperBound >= range.getLowerBound()) ||
                (lowerBound <= range.getUpperBound() && upperBound >= range.getUpperBound());
    }
}
