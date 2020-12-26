package com.p4ybill.stilt.index;

public class Node8 extends Node {
    private byte leftLength;
    private byte leftPath;
    private byte rightLength;
    private byte rightPath;

    public Node8(byte leftLength, byte leftPath, byte rightLength, byte rightPath) {
        this.leftLength = leftLength;
        this.leftPath = leftPath;
        this.rightLength = rightLength;
        this.rightPath = rightPath;
    }

    @Override
    public int getLeftLength() {
        return leftLength;
    }

    @Override
    public long getLeftPath() {
        return leftPath;
    }

    @Override
    public int getRightLength() {
        return rightLength;
    }

    @Override
    public long getRightPath() {
        return rightPath;
    }
}
