package com.github.frtu.dot;

import java.util.ArrayList;
import java.util.List;

/**
 * Dot Node with Dot attributes for a {@link Graph}.
 * <p>
 * Note : For internal package usage.
 * <p>
 * DO NOT change field order, since reuse the field id as Dot attributes
 *
 * @author frtu
 * @since 0.3.6
 */
public class GraphNode {
    // DO NOT CHANGE THESE FIELDS ORDER
    final static int FIRST_VISIBLE_FIELD_INDEX = 3;
    List<GraphNode> children = new ArrayList<>();
    private String id;
    // DO NOT CHANGE THESE FIELDS ORDER

    String label;
    PolygonShapeDotEnum shape;

    GraphNode(String id, String label, PolygonShapeDotEnum shape) {
        this.id = id;
        this.label = label;
        this.shape = shape;
    }

    void addChild(GraphNode child) {
        this.children.add(child);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setShape(PolygonShapeDotEnum shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", shape=" + shape +
                '}';
    }
}
