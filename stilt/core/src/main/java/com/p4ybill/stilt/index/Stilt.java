package com.p4ybill.stilt.index;

import com.p4ybill.stilt.parser.DimensionMapper;
import com.p4ybill.stilt.parser.PathScheduler;
import com.p4ybill.stilt.store.LSMStorage;
import com.p4ybill.stilt.utils.BinaryUtils;

public class Stilt<K> {
    private Node root;
    private int numberOfDimensions;
    private int length;
    private LSMStorage<String> lsmStorage;
    private DimensionMapper dimensionMapper;
    private int bitPerDimension;
    private PathScheduler<K> pathScheduler;

    public Stilt(int length, int numberOfDimensions) {
        this.numberOfDimensions = numberOfDimensions;
        this.length = length;
        this.bitPerDimension = length / numberOfDimensions;
        this.root = new NonLeafNode();
    }

    public Stilt(int length, int numberOfDimensions, LSMStorage<String> lsmStorage, PathScheduler<K> pathScheduler) {
        this.numberOfDimensions = numberOfDimensions;
        this.length = length;
        this.bitPerDimension = length / numberOfDimensions;
        this.root = new NonLeafNode();
        this.lsmStorage = lsmStorage;
        this.pathScheduler = pathScheduler;
    }


    private long pathOf(K key) {
        return pathScheduler.getKey(key);
    }

    public Node getRoot() {
        return root;
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


    private Node split(Edge edge, long path, int pathLen) {
        Node nodeO = edge.getChild();
        int lengthO = edge.getLength();
        long pathO = edge.getLength();

        int lengthC = BinaryUtils.clzBounded((path >> (pathLen - lengthO)) ^ pathO, lengthO);

        Node nodeC = new NonLeafNode();

        edge.setChild(nodeC);
        edge.setLength(lengthC);
        edge.setPath(pathO >> (lengthO - lengthC));

        int lengthNew = lengthO - lengthC;

        Edge edgeNew = getRightSizedEdge(lengthNew);
        int maskC = (1 << lengthNew) - 1;

        long edgeNewPath = pathO & maskC;
        edgeNew.setLength(lengthNew);
        edgeNew.setPath(edgeNewPath);
        edgeNew.setChild(nodeO);

        edge.setChild(nodeC);

        // setEdge
        if (BinaryUtils.isOneAtPosition(edgeNewPath, lengthNew - 1)) { // right
            nodeC.setRightEdge(edgeNew);
        } else {
            nodeC.setLeftEdge(edgeNew);
        }

        return nodeC;
    }

    private Edge pickEdge(Node node, long path, int pathLen) {
        return BinaryUtils.isOneAtPosition(path, pathLen - 1) ? node.getRightEdge() : node.getLeftEdge();
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
        if (edge.getPath() == path && pathLen == edge.getLength()) {
            return edge.getChild();
        }

        return null;
    }

    private void putEdge(Node node, long path, int pathLen, Node nodeNext) {
        Edge edge = getRightSizedEdge(pathLen);
        edge.setChild(nodeNext);


        if (BinaryUtils.isOneAtPosition(path, pathLen - 1)) {
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
        double w = (float) pathLen / bitPerDimension;

        if (w <= 1) {
            return new Edge8();
        } else if (w <= 2) {
            return new Edge16();
        } else if (w <= 3) {
            return new Edge32();
        } else {
            return new Edge64();
        }
    }
}
