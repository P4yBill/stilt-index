package com.p4ybill.stilt.index;

public interface Edge {
    long getPath();

    byte getLength();

    void setPath(long length);

    void setLength(int length);

    Node getChild();

    void setChild(Node child);
}
