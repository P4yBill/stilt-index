package com.p4ybill.stilt.index;

import com.p4ybill.stilt.utils.Validators;

import java.util.stream.IntStream;

public class SearchUtils {
    public static Range getRange(int b) {
        Validators.checkArgument(b <= 64, "Range should not have more 64 bits.");
        return new Range(0, (1 << b) - 1);
    }

    /**
     * @param b bits per dimension
     * @param d bits per dimension
     * @return
     */
    public static DimensionalRange getFullRange(int b, int d) {
        Validators.checkArgument(b * d < 64, "Length should not be above 64.");

        DimensionalRange.Builder builder = new DimensionalRange.Builder(d);
        IntStream.of(d).forEach(__ -> builder.withRange(getRange(b)));

        return builder.build();
    }
}
