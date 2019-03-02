package com.github.frtu.dot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class FieldStream<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldStream.class);

    private static Field[] nodeFields = GraphNode.class.getDeclaredFields();
    private static Field[] edgeFields = GraphEdge.class.getDeclaredFields();

    static {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("== nodeFields ==");
            Arrays.stream(nodeFields).forEach(field -> LOGGER.debug(field.getName()));
            LOGGER.debug("== edgeFields ==");
            Arrays.stream(edgeFields).forEach(field -> LOGGER.debug(field.getName()));
        }
    }

    private T value;
    private Field[] fields;
    private int firstVisibleField;

    FieldStream(GraphNode graphNode) {
        this.value = (T) graphNode;
        this.fields = nodeFields;
        this.firstVisibleField = GraphNode.FIRST_VISIBLE_FIELD_INDEX;
    }

    FieldStream(GraphEdge graphEdge) {
        this.value = (T) graphEdge;
        this.fields = edgeFields;
        this.firstVisibleField = GraphEdge.FIRST_VISIBLE_FIELD_INDEX;
    }

    public static FieldStream<GraphNode> node(GraphNode graphNode) {
        return new FieldStream(graphNode);
    }

    public static FieldStream<GraphEdge> edge(GraphEdge graphEdge) {
        return new FieldStream(graphEdge);
    }

    public void apply(BiConsumer<String, Object> consumer) {
        for (int i = this.firstVisibleField; i < fields.length; i++) {
            final Object value;
            final Field nodeField = fields[i];
            try {
                nodeField.setAccessible(true);
                value = nodeField.get(this.value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("SHOULD NEVER HAPPEN");
            }
            consumer.accept(nodeField.getName(), value);
        }
    }
}
