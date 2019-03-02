package com.github.frtu.dot.model;

/**
 * Dot Edge with Dot attributes for a {@link Graph}.
 * <p>
 * Note : For internal package usage.
 * <p>
 * DO NOT change field order, since reuse the field id as Dot attributes
 *
 * @author frtu
 * @since 0.3.6
 */
public class GraphEdge {
    // DO NOT CHANGE THESE FIELDS ORDER
    public final static int FIRST_VISIBLE_FIELD_INDEX = 3;
    private Element target;
    private Element source;
    // DO NOT CHANGE THESE FIELDS ORDER

    private String color;
    private String style;

    GraphEdge(Element source, Element target) {
        this.source = source;
        this.target = target;
    }

    public boolean hasAttributes() {
        return color != null || style != null;
    }

    public Element getSource() {
        return source;
    }

    public String getSourceId() {
        return getSource().getId();
    }

    public Element getTarget() {
        return target;
    }

    public String getTargetId() {
        return getTarget().getId();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "GraphEdge{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", color='" + color + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}
