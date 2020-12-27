package com.p4ybill.stilt.index;

public abstract class AbstractEdge implements Edge {
    byte length;
    Node child;

    @Override
    public byte getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = (byte)length;
    }

    @Override
    public Node getChild() {
        return child;
    }
    
    @Override
    public void setChild(Node child) {
        this.child = child;
    }

    public abstract long getPath();
}
