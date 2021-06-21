package com.p4ybill.stilt.index;

import java.util.ArrayList;

public class LeafNode<K> implements Node {

    private ArrayList<K> entries;

    public LeafNode() {
        this.entries = new ArrayList();
    }

    public LeafNode(K key){
        this();
        addKey(key);
    }

    public void addKey(K key){
        this.entries.add(key);
    }

    public ArrayList<K> getEntries() {
        return this.entries;
    }

    @Override
    public Edge getRightEdge() {
        return null;
    }
    
    @Override
    public Edge getLeftEdge() {
        return null;
    }

    @Override
    public void setRightEdge(Edge edge) {
    }
    
    @Override
    public void setLeftEdge(Edge edge) {
    }
}
