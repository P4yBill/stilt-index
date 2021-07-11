package com.p4ybill.stilt.index;

import com.p4ybill.stilt.parser.PathScheduler;
import com.p4ybill.stilt.store.LSMStorage;
import com.p4ybill.stilt.utils.BinaryUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Stilt<K extends Key> {
    private Node root;
    private int numberOfDimensions;
    private int length;
    private LSMStorage<String> lsmStorage;
    private int bitsPerDimension;
    private PathScheduler<K> pathScheduler;

    public Stilt(int length, int numberOfDimensions) {
        this.numberOfDimensions = numberOfDimensions;
        this.length = length;
        this.bitsPerDimension = length / numberOfDimensions;
        this.root = new NonLeafNode();
    }

    public Stilt(int length, int numberOfDimensions, LSMStorage<String> lsmStorage, PathScheduler<K> pathScheduler) {
        this.numberOfDimensions = numberOfDimensions;
        this.length = length;
        this.bitsPerDimension = length / numberOfDimensions;
        this.root = new NonLeafNode();
        this.lsmStorage = lsmStorage;
        this.pathScheduler = pathScheduler;
    }

    public boolean insert(Node node, K key, int id) {
        if (node == null) {
            return false;
        }
        long path = pathOf(key);

        int pathLen = this.length;
        while (path != 0 && pathLen != 0) {
            // TODO: Lock Node.
            Edge edge = pickEdge(node, path, pathLen);
            if (edge == null) { // node does not exists, create it
                Node nodeNext = new LeafNode<K>();
                this.putEdge(node, path, pathLen, nodeNext);
                node = nodeNext;
                break;
            }
            Node nodeMatched = this.edgeMatchesPath(edge, path, pathLen);
            if (nodeMatched != null) {
                node = nodeMatched;
            } else { // split the edge
                node = this.split(edge, path, pathLen);
            }

            path -= (edge.getPath() << pathLen - edge.getLength());
            pathLen -= edge.getLength();

            // TODO: ReleaseLock of concurrent index.
        }

        this.addKeyToNode(node, key);
        return true;
    }

    public boolean insert(K key, int id) {
        return this.insert(root, key, id);
    }

    public Query initQuery() {
        return new Query(this.bitsPerDimension);
    }

    public Set<Integer> rangeSearch(Query query) {
        List<K> entries = new ArrayList<>();
        Set<Integer> entryIds = new HashSet<>();
        initQueryRanges(query);

        RangeSearch<K> rangeSearch;
        DimensionalRange dr;

        if (query.getWords().isPresent()) {
            List<String> words = query.getWords().get();
            for (String w : words) {
                query.setWord(w);
                int mappedWord = pathScheduler.getMappedValue(w, 2);
                query.setRangeWord(new Range(mappedWord, mappedWord));
                dr = initWithFullRange(query);
                rangeSearch = new RangeSearch<>(length, bitsPerDimension, numberOfDimensions);
                List<K> l = rangeSearch.searchNode(root, query, 0, dr);
                entries.addAll(l);
                entryIds.addAll(l.stream().map(Key::getId).collect(Collectors.toSet()));
            }
        } else {
            // String dimension is ignored
            query.setRangeWord(null);
            dr = initWithFullRange(query);
            rangeSearch = new RangeSearch<>(length, bitsPerDimension, numberOfDimensions);
            List<K> l = rangeSearch.searchNode(root, query, 0, dr);
            entries.addAll(l);
            entryIds.addAll(l.stream().map(Key::getId).collect(Collectors.toSet()));
        }

        return entryIds;
    }

    public DimensionalRange initWithFullRange(Query query) {
        DimensionalRange.Builder drBuilder = new DimensionalRange.Builder(this.numberOfDimensions);
        for (int i = 0; i < this.numberOfDimensions; i++) {
            if (query.getRangeForDimension(i) != null) {
                drBuilder.withRange(SearchUtils.getRange(this.bitsPerDimension));
            } else {
                drBuilder.withRange(null);
            }
        }

        return drBuilder.build();
    }

    private void initQueryRanges(Query query) {
        if (query.getMinX().isPresent() || query.getMaxX().isPresent()) {
            if (query.getMinX().isPresent()) {
                query.getRangeX().setLowerBound(pathScheduler.getMappedValue(query.getMinX().get(), 0));
            }
            if (query.getMaxX().isPresent()) {
                query.getRangeX().setUpperBound(pathScheduler.getMappedValue(query.getMaxX().get(), 0));
            }
        } else {
            query.setRangeX(null);
        }

        if (query.getMinY().isPresent() || query.getMaxY().isPresent()) {
            if (query.getMinY().isPresent()) {
                query.getRangeY().setLowerBound(pathScheduler.getMappedValue(query.getMinY().get(), 1));
            }
            if (query.getMaxY().isPresent()) {
                query.getRangeY().setUpperBound(pathScheduler.getMappedValue(query.getMaxY().get(), 1));
            }
        } else {
            query.setRangeY(null);
        }

        if (query.getMinTimestamp().isPresent() || query.getMaxTimestamp().isPresent()) {
            if (query.getMinTimestamp().isPresent()) {
                query.getRangeTimestamp().setLowerBound(pathScheduler.getMappedValue(query.getMinTimestamp().get(), 3));
            }
            if (query.getMaxTimestamp().isPresent()) {
                query.getRangeTimestamp().setUpperBound(pathScheduler.getMappedValue(query.getMaxTimestamp().get(), 3));
            }
        } else {
            query.setRangeTimestamp(null);
        }


    }

    private long pathOf(K key) {
        return pathScheduler.getKey(key);
    }

    private Node split(Edge edge, long path, int pathLen) {
        Node nodeO = edge.getChild();
        int lengthO = edge.getLength();
        long pathO = edge.getPath();

        int lengthC = BinaryUtils.clzBounded((path >>> (pathLen - lengthO)) ^ pathO, lengthO);

        Node nodeC = new NonLeafNode();

        edge.setChild(nodeC);
        edge.setLength(lengthC);
        edge.setPath(pathO >>> (lengthO - lengthC));

        int lengthNew = lengthO - lengthC;

        Edge edgeNew = getRightSizedEdge(lengthNew);
        long maskC = (1L << lengthNew) - 1;

        long edgeNewPath = pathO & maskC;
        edgeNew.setLength(lengthNew);
        edgeNew.setPath(edgeNewPath);
        edgeNew.setChild(nodeO);

        edge.setChild(nodeC);

        // setEdge
        if (BinaryUtils.isSetAtPosition(edgeNewPath, lengthNew)) { // right
            nodeC.setRightEdge(edgeNew);
        } else {
            nodeC.setLeftEdge(edgeNew);
        }

        return nodeC;
    }

    private Edge pickEdge(Node node, long path, int pathLen) {
        return BinaryUtils.isSetAtPosition(path, pathLen) ? node.getRightEdge() : node.getLeftEdge();
    }

    /**
     * Checks if edge's path completely match the given path.
     *
     * @param edge    desired edge to check
     * @param path    the binary path
     * @param pathLen The length of the path.
     * @return the edge's child node if the edge matched, otherwise null
     */
    private Node edgeMatchesPath(Edge edge, long path, int pathLen) {
        if (edge.getPath() == (path >>> (pathLen - edge.getLength()))) {
            return edge.getChild();
        }

        return null;
    }

    private void putEdge(Node node, long path, int pathLen, Node nodeNext) {
        Edge edge = getRightSizedEdge(pathLen);
        edge.setChild(nodeNext);
        edge.setPath(path);
        edge.setLength(pathLen);


        if (BinaryUtils.isSetAtPosition(path, pathLen)) {
            node.setRightEdge(edge);
        } else {
            node.setLeftEdge(edge);
        }
    }

    @SuppressWarnings("unchecked")
    private void addKeyToNode(Node node, K key) {
        LeafNode<K> leafNode = (LeafNode<K>) node;

        leafNode.addKey(key);
    }

    private Edge getRightSizedEdge(int pathLen) {
        if (pathLen <= 8) {
            return new Edge8();
        } else if (pathLen <= 16) {
            return new Edge16();
        } else if (pathLen <= 32) {
            return new Edge32();
        } else {
            return new Edge64();
        }
    }
}
