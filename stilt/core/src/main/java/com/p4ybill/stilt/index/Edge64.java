package com.p4ybill.stilt.index;

public class Edge64 extends AbstractEdge {
    long path;

    @Override
    public long getPath() {
        return path;
    }

    public void setPath(long path) {
        this.path = path;
    }
}
