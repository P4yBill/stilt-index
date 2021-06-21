package com.p4ybill.stilt.parser;

public class SpatialMappingFunction implements MappingFunction {

    @Override
    public int map(Object value, int bits) {
        float f = (Float) value;
        return (int)f;
    }
}
