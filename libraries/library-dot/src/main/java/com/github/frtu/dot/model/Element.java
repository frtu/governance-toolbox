package com.github.frtu.dot.model;

import java.util.regex.Pattern;

/**
 * Dot Element containing all common fields like ID, comment.
 *
 * @author frtu
 * @since 0.3.6
 */
public abstract class Element {
    public static final String ID_PATTERN_STR = "[_a-zA-Z\\\\200-\\\\377][0-9_a-zA-Z\\\\200-\\\\377]*";
    private Pattern idPattern = Pattern.compile(ID_PATTERN_STR);

    private String id;
    private String comment;

    protected Element(String id) {
        assertFormatId(id);
        this.id = id;
    }

    private void assertFormatId(String id) {
        if (!idPattern.matcher(id).matches()) {
            throw new IllegalStateException("IDs MUST match pattern " + ID_PATTERN_STR + " parameter passed " + id);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean hasComment() {
        return comment != null && !"".equals(comment);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.trim();
    }

    @Override
    public String toString() {
        return "Element { id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}