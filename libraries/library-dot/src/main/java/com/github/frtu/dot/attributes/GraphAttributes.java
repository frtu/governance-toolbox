package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.Graph;

/**
 * Attribute defining ALL the {@link Graph} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class GraphAttributes extends Attributes<Graph> {
    String style;

    String bgcolor;
    String color;

    String fontcolor;
    String fontname;
    Double fontsize;

    Boolean center;

    public void setStyle(String style) {
        this.style = style;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public void setColor(String color) {
        this.color = color;
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

    public void setCenter(Boolean center) {
        this.center = center;
    }
}
