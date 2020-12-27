package com.p4ybill.stilt.index;

/**
 * Represents the Key of the index.
 * Holds the values of dimensions for a document
 */
public interface Key {
    int getId();

    // TODO: Create a more abstract representation of components.
    float getLatitude();

    float getLongitude();

    String getKeyword();

    long getDate();

    int getKeywordFrequency();

    int getTotalWordFrequency();
}
