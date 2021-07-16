package com.p4ybill.stilt.parser;

/**
 * Simple Mapping function for temporal component.
 *
 */
public class DateMappingFunction implements MappingFunction {

    @Override
    public int map(Object value, int bits) {
        long timestamp = (Long) value;
        return (int)timestamp;
    }
}
