package com.github.frtu.dot;

public class GraphEdge {
    // DO NOT CHANGE THESE FIELDS ORDER
    final static int FIRST_VISIBLE_FIELD_INDEX = 3;
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
}
