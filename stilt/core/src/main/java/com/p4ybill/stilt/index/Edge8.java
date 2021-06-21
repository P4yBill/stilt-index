package com.p4ybill.stilt.index;

public class Edge8 extends AbstractEdge {
    byte path;

    @Override
    public long getPath() {
        return path;
    }

    @Override
    public void setPath(long path) {
        this.path = (byte)path;
    }
}
