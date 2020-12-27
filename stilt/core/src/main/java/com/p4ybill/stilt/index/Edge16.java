package com.p4ybill.stilt.index;

public class Edge16 extends AbstractEdge {
    short path;

    @Override
    public long getPath() {
        return path;
    }

    public void setPath(long path) {
        this.path = (short)path;
    }
}
