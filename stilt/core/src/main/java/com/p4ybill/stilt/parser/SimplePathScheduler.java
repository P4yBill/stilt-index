package com.p4ybill.stilt.parser;

import com.p4ybill.stilt.index.Key;

public class SimplePathScheduler implements PathScheduler<Key> {
    private int bitsPerDimension;
    private int bitLength;
    private int numberOfDimensions;
    private int negationMask;
    private DimensionMapper dimensionMapper;

    public SimplePathScheduler(int numberOfDimensions, int bitLength, DimensionMapper dimensionMapper) {
        this.numberOfDimensions = numberOfDimensions;
        this.bitLength = bitLength;
        this.bitsPerDimension = bitLength / numberOfDimensions;
        this.negationMask = (1 << this.bitsPerDimension) - 1;
        this.dimensionMapper = dimensionMapper;
    }

    @Override
    public long getKey(Key data) {
        // TODO: Interleave bits of each component
        long key = 0;
        Object[] keyValues = this.getKeyDimensionValues(data);
        int[] binaryStrings = new int[keyValues.length];
        for(int i = 0; i < keyValues.length ; i++){
            binaryStrings[i] = dimensionMapper.getMappingFunction(i).map(keyValues[i], bitsPerDimension);
            binaryStrings[i] &= this.negationMask;
        }

       for(int i = this.bitsPerDimension - 1; i >= 0 ; i--){
           for(int j = 0; j < this.numberOfDimensions ; j++){
               key = key << 1;
               key |= (binaryStrings[j] >>> i);
           }
       }
        return key;
    }

    private Object[] getKeyDimensionValues(Key key){
        return new Object[]{ key.getLatitude(), key.getLongitude(), key.getKeyword(), key.getDate() };
    }
}
