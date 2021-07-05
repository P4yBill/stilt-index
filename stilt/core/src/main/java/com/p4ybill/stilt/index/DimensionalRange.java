package com.p4ybill.stilt.index;

import com.p4ybill.stilt.utils.Validators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DimensionalRange {
    private final Range[] ranges;

    private DimensionalRange(Range[] ranges) {
        this.ranges = ranges;
    }


    /**
     * Retrieves the range for a given dimension.
     *
     * @param d dimension to retrieve
     */
    public Range getRange(int d) {
        return this.ranges[d];
    }

    /**
     * Sets a new range for a given dimension
     *
     * @param range new range to set
     * @param d dimension number
     */
    public void setRange(Range range, int d){
        ranges[d] = range;
    }

    public int size(){
        return this.ranges.length;
    }

    public static DimensionalRange clone(DimensionalRange dimensionalRange){
        Range[] ranges = new Range[dimensionalRange.size()];

        for(int i = 0 ; i < dimensionalRange.size() ; i++){
            Range range  = dimensionalRange.getRange(i);
            ranges[i] = range != null ? new Range(range.getLowerBound(), range.getUpperBound()) : null;
        }

        return new DimensionalRange(ranges);
    }

    public static class Builder {
        private List<Range> ranges;
        private int dimensions;

        public Builder(int dimensions) {
            this.dimensions = dimensions;
            this.ranges = new ArrayList<>();
        }

        /**
         * Adds specified range.
         * If null provided then the range will be added and this should mean that the dimension will be ignored.
         *
         * @param range
         */
        public Builder withRange(Range range){
            this.ranges.add(range);
            return this;
        }

        public DimensionalRange build() {
            Validators.checkArgument(this.ranges.size() == this.dimensions, "The number of mapping functions should be equal to the number of dimensions.");

            Range[] array = new Range[this.ranges.size()];
            DimensionalRange dr = new DimensionalRange(this.ranges.toArray(array));
            this.ranges = Collections.emptyList();

            return dr;
        }

    }
}
