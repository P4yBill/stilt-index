package com.p4ybill.stilt.index;

public interface Node {
    Edge getRightEdge();

    Edge getLeftEdge();

    void setRightEdge(Edge edge);

    void setLeftEdge(Edge edge);
}