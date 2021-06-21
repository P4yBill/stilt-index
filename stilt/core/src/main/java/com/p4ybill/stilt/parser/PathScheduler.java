package com.p4ybill.stilt.parser;

public interface PathScheduler<T> {
    long getKey(T dimensionKeys);
}
