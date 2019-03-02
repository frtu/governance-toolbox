package com.github.frtu.dot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Object representing a Graph in memory that contains {@link GraphNode}.
 * <p>
 * Note : Many graph implementation exist and the goal here is to have {@link GraphNode} POJO have the same Dot attributes.
 *
 * @author frtu
 * @since 0.3.6
 */
public class Graph extends Element {
    private HashMap<String, GraphNode> allNodes = new HashMap<String, GraphNode>();
    private List<GraphEdge> edges = new ArrayList<>();

    private GraphNode rootNode;
    private GraphNode currentParentNode;

    public Graph(String id) {
        super(id);
    }

    public HashMap<String, GraphNode> getAllNodes() {
        return allNodes;
    }

    public List<GraphEdge> getAllEdges() {
        return edges;
    }

    public GraphNode getNode(String id) {
        return allNodes.get(id);
    }

    public GraphNode getRootNode() {
        return rootNode;
    }

    public GraphNode getCurrentParentNode() {
        return currentParentNode;
    }

    public GraphEdge addEdge(String sourceId, String targetId) {
        return addEdge(getNode(sourceId), getNode(targetId));
    }

    public GraphEdge addEdge(Element source, Element target) {
        final GraphEdge graphEdge = new GraphEdge(source, target);
        edges.add(graphEdge);
        return graphEdge;
    }

    public GraphNode addNode(String id, String label, PolygonShapeDotEnum polygonShape) {
        return addNodeToParent(id, label, polygonShape, this.currentParentNode);
    }

    public GraphNode addNodeMoveToChild(String id, String label, PolygonShapeDotEnum polygonShape) {
        final GraphNode graphNode = addNodeToParent(id, label, polygonShape, this.currentParentNode);
        this.currentParentNode = graphNode;
        return graphNode;
    }

    public GraphNode addNodeMoveParent(String id, String label, PolygonShapeDotEnum polygonShape, GraphNode parentNode) {
        this.currentParentNode = parentNode;
        final GraphNode graphNode = addNodeToParent(id, label, polygonShape, parentNode);
        return graphNode;
    }

    private GraphNode addNodeToParent(String id, String label, PolygonShapeDotEnum polygonShape, GraphNode parentNode) {
        final GraphNode graphNode = buildGraphNode(id, label, polygonShape);
        if (parentNode != null && !parentNode.getId().equals(id)) {
            parentNode.addChild(graphNode);
        }
        return graphNode;
    }

    private GraphNode buildGraphNode(String id, String label, PolygonShapeDotEnum polygonShape) {
        final GraphNode graphNode = new GraphNode(id, label, polygonShape);
        if (rootNode == null) {
            rootNode = graphNode;
        }
        if (currentParentNode == null) {
            currentParentNode = graphNode;
        }
        allNodes.put(graphNode.getId(), graphNode);
        return graphNode;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[id='").append(getId()).append("\']\n");
        stringBuilder.append(this.rootNode.toString()).append('\n');
        buildChildren(stringBuilder, this.rootNode, 1);
        return stringBuilder.toString();
    }

    private void buildChildren(StringBuilder stringBuilder, GraphNode currentNode, int level) {
        currentNode.children.forEach(child -> {
            stringBuilder.append("|-");
            for (int i = 1; i < level; i++) {
                stringBuilder.append('-');
            }
            stringBuilder.append(' ').append(child).append('\n');
            if (!child.children.isEmpty()) {
                buildChildren(stringBuilder, child, level + 1);
            }
        });
    }
}
