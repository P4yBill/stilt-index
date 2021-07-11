package com.p4ybill.stilt.index;

/**
 * Represents the Key of the index.
 * Holds the values of dimensions for a document
 */
public interface FourDimensionalKey extends Key {
    // TODO: Create a more abstract representation of components.
    double getY(); // y

    double getX(); // x

    String getKeyword();

    long getDate();

    int getKeywordFrequency();

    int getTotalWordFrequency();
}
