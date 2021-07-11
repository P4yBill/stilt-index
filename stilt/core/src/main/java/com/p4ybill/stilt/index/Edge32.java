package com.p4ybill.stilt.index;

public class Edge32 extends AbstractEdge {
    int path;

    @Override
    public long getPath() {
        return Integer.toUnsignedLong(this.path);
    }

    public void setPath(long path) {
        this.path = (int)path;
    }
}
