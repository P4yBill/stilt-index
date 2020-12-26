package com.p4ybill.stilt.index;

public abstract class Node {
    private Object leftChild;
    private Object rightChild;

    private byte leftLength;
    private byte leftPath;
    private byte rightLength;
    private byte rightPath;

    public Object getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Object leftChild) {
        this.leftChild = leftChild;
    }

    public Object getRightChild() {
        return rightChild;
    }

    public void setRightChild(Object rightChild) {
        this.rightChild = rightChild;
    }

    public abstract int getLeftLength();

    public abstract long getLeftPath();

    public abstract int getRightLength();

    public abstract long getRightPath();
}
