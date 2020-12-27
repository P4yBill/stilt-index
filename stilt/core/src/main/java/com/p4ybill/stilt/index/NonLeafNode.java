package com.p4ybill.stilt.index;

public class NonLeafNode implements Node {
    Edge rightEdge;
    Edge leftEdge;

    public NonLeafNode(Edge leftEdge, Edge rightEdge){
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public NonLeafNode(){}

    @Override
    public Edge getRightEdge() {
        return this.rightEdge;
    }

    public void setRightEdge(Edge rightEdge) {
        this.rightEdge = rightEdge;
    }

    @Override
    public Edge getLeftEdge() {
        return this.leftEdge;
    }

    public void setLeftEdge(Edge leftEdge) {
        this.leftEdge = leftEdge;
    }
}
