package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.index.FourDimensionalKey;
import com.p4ybill.stilt.utils.Validators;

public class SimplePathScheduler implements PathScheduler<FourDimensionalKey> {
    private int bitsPerDimension;
    private int numberOfDimensions;
    private int negationMask;
    private DimensionMapper dimensionMapper;

    public SimplePathScheduler(int numberOfDimensions, int bitLength, DimensionMapper dimensionMapper) {
        this.numberOfDimensions = numberOfDimensions;
        this.bitsPerDimension = bitLength / numberOfDimensions;
        this.negationMask = (1 << this.bitsPerDimension) - 1;
        this.dimensionMapper = dimensionMapper;


    }

    @Override
    public long getKey(FourDimensionalKey data) {
        long key = 0;
        Object[] keyValues = this.getKeyDimensionValues(data);
        int[] binaryStrings = new int[keyValues.length];
        for (int i = 0; i < keyValues.length; i++) {
            binaryStrings[i] = dimensionMapper.getMappingFunction(i).map(keyValues[i], bitsPerDimension);
            // applying mask to ensure that binary string has a specific length
            binaryStrings[i] &= this.negationMask;
        }

        for (int i = this.bitsPerDimension - 1; i >= 0; i--) {
            for (int j = 0; j < this.numberOfDimensions; j++) {
                key = key << 1;
                int bit = (binaryStrings[j] >>> i) & 1;
                key |= bit;
            }
        }
        return key;
    }

    private Object[] getKeyDimensionValues(FourDimensionalKey key) {
        return new Object[]{key.getX(), key.getY(), key.getKeyword(), key.getDate()};
    }


    /**
     * Maps given value to a binary string according to the map function for that dimension
     *
     * @param value the value to be mapped
     * @param d     dimension
     * @return binary string
     */
    public int getMappedValue(Object value, int d) {
        Validators.checkArgument(d <= numberOfDimensions, "Dimension should be lower than the total dimensions");

        return (dimensionMapper.getMappingFunction(d).map(value, bitsPerDimension)) & negationMask;
    }

}
