package com.p4ybill.stilt.parser;

public interface PathScheduler<T> {
    long getKey(T dimensionKeys);

    int getMappedValue(Object value, int d);
}
