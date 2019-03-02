package com.github.frtu.dot;

import java.lang.reflect.Field;

import static com.github.frtu.dot.GraphNode.FIRST_VISIBLE_FIELD_INDEX;

/**
 * From a {@link Graph} object, create a Dot language.
 *
 * @author frtu
 * @see <a href="https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">DOT (graph description language) in WIKIPEDIA</a>
 * @see <a href="https://graphviz.gitlab.io/_pages/doc/info/lang.html">DOT Grammar</a>
 * @since 0.3.6
 */
public class DotRenderer {
    private static Field[] nodeFields = GraphNode.class.getDeclaredFields();

    private StringBuilder result;

    public DotRenderer() {
        result = new StringBuilder();
    }

    public String renderGraph(Graph graph, boolean directed) {
        if (directed) {
            result.append("di");
        }
        result.append("graph ").append(graph.getGraphID()).append(" {\n");
        renderGraphNode(graph.getRootNode(), directed);
        result.append("}");
        return result.toString();
    }

    private DotRenderer renderGraphNode(GraphNode node, boolean directed) {
        // statement : node_stmt
        renderStatementNode(node);
        node.children.forEach(childNode -> {
                    renderGraphNode(childNode, directed);
                    // statement : edge_stmt
                    renderStatementEdge(node.getId(), childNode.getId(), directed);
                }
        );
        return this;
    }

    private DotRenderer renderStatementEdge(String sourceId, String targetId, boolean directed) {
        result.append("  ").append(sourceId);
        if (directed) {
            result.append(" -> ");
        } else {
            result.append(" -- ");
        }
        result.append(targetId).append(";\n");
        return this;
    }

    private DotRenderer renderStatementNode(GraphNode node) {
        result.append("  ").append(node.getId()).append(" [");
        for (int i = FIRST_VISIBLE_FIELD_INDEX; i < nodeFields.length; i++) {
            if (i != FIRST_VISIBLE_FIELD_INDEX) result.append(", ");

            final Field nodeField = nodeFields[i];

            final Object value;
            try {
                nodeField.setAccessible(true);
                value = nodeField.get(node);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("SHOULD NEVER HAPPEN");
            }
            result.append(nodeField.getName()).append('=');
            if (value instanceof String) {
                result.append('"');
            }
            result.append(value);
            if (value instanceof String) {
                result.append('"');
            }
        }
        result.append("];\n");
        return this;
    }
}
