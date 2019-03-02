package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.GraphNode;
import com.github.frtu.dot.model.PolygonShapeDotEnum;

/**
 * Attribute defining ALL the {@link GraphNode} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class NodeAttributes extends Attributes<GraphNode> {
    String style;

    String bgcolor;
    String color;
    String fillcolor;

    String fontcolor;
    String fontname;
    Double fontsize;

    PolygonShapeDotEnum shape;

    public void setStyle(String style) {
        this.style = style;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor;
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

    public void setShape(PolygonShapeDotEnum shape) {
        this.shape = shape;
    }
}
