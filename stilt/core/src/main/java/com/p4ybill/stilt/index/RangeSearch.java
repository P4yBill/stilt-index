package com.p4ybill.stilt.index;

import com.p4ybill.stilt.utils.BinaryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RangeSearch<K extends Key> {
    private int bitsPerDimension;
    private int dimensions;
    private int length;

    public RangeSearch(int length, int bitsPerDimension, int dimensions) {
        this.bitsPerDimension = bitsPerDimension;
        this.dimensions = dimensions;
        this.length = length;
    }

    public List<K> searchNode(Node node, Query query, int depth, DimensionalRange dimensionalRange) {
        if (depth < length) {
            int dimension = (depth % this.dimensions);
            depth++;

            List<K> l = new ArrayList<>();
            List<K> u = new ArrayList<>();
            Range v = dimensionalRange.getRange(dimension);
            Edge leftEdge = node.getLeftEdge();
            Edge rightEdge = node.getRightEdge();
            if (v == null) {
                // dimension ignored
                if (leftEdge != null) {
                    l = searchEdge(node.getLeftEdge(), query, depth, dimensionalRange);
                }
                if (rightEdge != null) {
                    u = searchEdge(node.getRightEdge(), query, depth, dimensionalRange);
                }
            } else {
                Range queryDimensionRange = query.getRangeForDimension(dimension);
                int dimensionDepth = getDimensionDepth(depth - 1);
                if (leftEdge != null) {
                    Range lowerHalf = lowerHalf(v, dimensionDepth);
                    if (queryDimensionRange.intersects(lowerHalf)) {
                        DimensionalRange clonedRange = DimensionalRange.clone(dimensionalRange);
                        clonedRange.setRange(lowerHalf, dimension);
                        l = searchEdge(leftEdge, query, depth, clonedRange);
                    }
                }

                if (rightEdge != null) {
                    Range upperHalf = upperHalf(v, dimensionDepth);
                    if (queryDimensionRange.intersects(upperHalf)) {
                        DimensionalRange clonedRange = DimensionalRange.clone(dimensionalRange);
                        clonedRange.setRange(upperHalf, dimension);
                        u = searchEdge(rightEdge, query, depth, clonedRange);
                    }
                }
            }

            return Stream.concat(l.stream(), u.stream())
                    .collect(Collectors.toList());
        } else {
            // its a leaf
            List<K> entries = new ArrayList<>();
            LeafNode<K> leafNode = (LeafNode<K>) node;

            for (K key : leafNode.getEntries()) {
                if (query.intersects(key)) {
                    entries.add(key);
                }
            }

            return entries;
        }
    }

    private int getDimensionDepth(int depth) {
        return bitsPerDimension - ((depth + 1) / dimensions);
    }

    private List<K> searchEdge(Edge edge, Query query, int depth, DimensionalRange dimensionalRange) {
        long path = edge.getPath();
        int length = edge.getLength();
        int depth2;
        for (int i = 0; i < length - 1; i++) {
            depth2 = depth + i;
            int d = depth2 % 4;
            Range v = dimensionalRange.getRange(d);
            if (v != null) {
                int dimensionDepth = getDimensionDepth(depth2);
                if (BinaryUtils.isSetAtPosition(path, length - (i + 1))) {
                    v = upperHalf(v, dimensionDepth);
                } else {
                    v = lowerHalf(v, dimensionDepth);
                }

                dimensionalRange.setRange(v, d);
            }
        }
//        012301230123012301230123012301230123012301230123
//        000000000000001000110000001000000000000011001000

        if (rangeIntersectsQuery(dimensionalRange, query)) {
            depth += (length - 1);
            return searchNode(edge.getChild(), query, depth, dimensionalRange);
        }

        return Collections.emptyList();
    }


    private boolean rangeIntersectsQuery(DimensionalRange range, Query query) {
        return ((range.getRange(0) == null || query.getRangeX() == null) || range.getRange(0).intersects(query.getRangeX())) &&
                ((range.getRange(1) == null || query.getRangeY() == null) || range.getRange(1).intersects(query.getRangeY())) &&
                ((range.getRange(3) == null || query.getRangeTimestamp() == null) || range.getRange(3).intersects(query.getRangeTimestamp())) &&
                ((query.getWord().isEmpty() || query.getRangeWord() == null) || range.getRange(2).intersects(query.getRangeWord()));
    }

    private Range lowerHalf(Range range, int pos) {
        int unSetMask = ~(1 << (pos - 1));
        return new Range(range.getLowerBound() & unSetMask, range.getUpperBound() & unSetMask);
    }

    private Range upperHalf(Range range, int pos) {
        int setMask = 1 << (pos - 1);
        return new Range(range.getLowerBound() | setMask, range.getUpperBound() | setMask);
    }
}
