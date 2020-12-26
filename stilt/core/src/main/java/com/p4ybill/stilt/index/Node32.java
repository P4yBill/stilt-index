package com.p4ybill.stilt.index;

public class Node32 extends Node {
    private byte leftLength;
    private int leftPath;
    private byte rightLength;
    private int rightPath;

    public Node32(byte leftLength, int leftPath, byte rightLength, int rightPath) {
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
