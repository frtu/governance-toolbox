package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.GraphEdge;

/**
 * Attribute defining ALL the {@link GraphEdge} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class EdgeAttributes extends Attributes<GraphEdge> {
    String style;
    String color;
    Double arrowsize;

    String fontcolor;
    String fontname;
    Double fontsize;

    public void setStyle(String style) {
        this.style = style;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setArrowsize(Double arrowsize) {
        this.arrowsize = arrowsize;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public void setFontsize(Double fontsize) {
        this.fontsize = fontsize;
    }
}
