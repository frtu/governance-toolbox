package com.github.frtu.dot.model;

import com.github.frtu.dot.attributes.EdgeAttributes;
import com.github.frtu.dot.attributes.GraphAttributes;
import com.github.frtu.dot.attributes.NodeAttributes;

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
    protected HashMap<String, GraphNode> allNodes = new HashMap<String, GraphNode>();
    protected List<GraphEdge> edges = new ArrayList<>();

    protected GraphNode rootNode;
    protected GraphNode currentParentNode;

    private GraphAttributes graphAttributes;
    private NodeAttributes nodeAttributes;
    private EdgeAttributes edgeAttributes;

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

    public GraphEdge addEdge(String sourceId, String... targetIds) {
        GraphEdge graphEdge = null;

        String baseId = sourceId;
        for (String targetId : targetIds) {
            graphEdge = new GraphEdge(baseId, targetId);
            edges.add(graphEdge);
            baseId = targetId;
        }
        return graphEdge;
    }

    public GraphEdge addEdge(Element source, Element target) {
        return addEdge(source.getId(), target.getId());
    }

    public GraphEdge addEdge(String sourceId, Element target) {
        return addEdge(sourceId, target.getId());
    }

    public GraphEdge addEdge(Element source, String targetId) {
        return addEdge(source.getId(), targetId);
    }

    public GraphNode addSingleNode(String id, PolygonShapeDotEnum polygonShape) {
        return addSingleNode(id, id, polygonShape);
    }

    public GraphNode addSingleNode(String id, String label, PolygonShapeDotEnum polygonShape) {
        return addNodeToParent(id, label, polygonShape, null);
    }

    public GraphNode addNode(String id, PolygonShapeDotEnum polygonShape) {
        return addNode(id, id, polygonShape);
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

        stringBuilder.append("- graphAttributes=").append(graphAttributes).append("\n");
        stringBuilder.append("- nodeAttributes=").append(nodeAttributes).append("\n");
        stringBuilder.append("- edgeAttributes=").append( edgeAttributes).append("\n");

        if (this.rootNode != null) {
            stringBuilder.append(this.rootNode.toString()).append('\n');
            buildChildren(stringBuilder, this.rootNode, 1);
        }
        edges.forEach(edge -> {
            stringBuilder.append("-> ").append(edge).append("\n");
        });
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

    public GraphAttributes newGraphAttributes() {
        final GraphAttributes graphAttributes = GraphAttributes.build();
        setGraphAttributes(graphAttributes);
        return graphAttributes;
    }

    public GraphAttributes getGraphAttributes() {
        return graphAttributes;
    }

    public void setGraphAttributes(GraphAttributes graphAttributes) {
        this.graphAttributes = graphAttributes;
    }

    public NodeAttributes newNodeAttributes() {
        final NodeAttributes nodeAttributes = NodeAttributes.build();
        setNodeAttributes(nodeAttributes);
        return nodeAttributes;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public void setNodeAttributes(NodeAttributes nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
    }

    public EdgeAttributes newEdgeAttributes() {
        final EdgeAttributes edgeAttributes = EdgeAttributes.build();
        setEdgeAttributes(edgeAttributes);
        return edgeAttributes;
    }

    public EdgeAttributes getEdgeAttributes() {
        return edgeAttributes;
    }

    public void setEdgeAttributes(EdgeAttributes edgeAttributes) {
        this.edgeAttributes = edgeAttributes;
    }
}
