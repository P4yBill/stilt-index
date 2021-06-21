package com.p4ybill.stilt.parser;

public class SpatialMappingFunction implements MappingFunction {

    @Override
    public int map(Object value, int bits) {
        return (int)value;
    }
}
