package com.github.frtu.dot;

/**
 * From a {@link Graph} object, create a Dot language.
 *
 * @author frtu
 * @see <a href="https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">DOT (graph description language) in WIKIPEDIA</a>
 * @see <a href="https://graphviz.gitlab.io/_pages/doc/info/lang.html">DOT Grammar</a>
 * @since 0.3.6
 */
public class DotRenderer {
    private StringBuilder result;

    public DotRenderer() {
        result = new StringBuilder();
    }

    public String renderGraph(Graph graph, boolean directed) {
        if (directed) {
            result.append("di");
        }
        result.append("graph ").append(graph.getId()).append(" {\n");
        renderComment(graph);

        renderGraphNode(graph.getRootNode(), directed);
        graph.getAllEdges().stream().forEach(graphEdge -> {
            renderStatementEdge(graphEdge, directed);
        });

        result.append("}");
        return result.toString();
    }

    private DotRenderer renderComment(Element element) {
        if (element.hasComment()) {
            indent();
            result.append("/* ").append(element.getComment()).append(" */");
            newline();
        }
        return this;
    }

    private DotRenderer renderGraphNode(GraphNode node, boolean directed) {
        renderComment(node);
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

    private DotRenderer renderStatementEdge(GraphEdge graphEdge, boolean directed) {
        renderStatementEdge(graphEdge.getSourceId(), graphEdge.getTargetId(), directed, false);

        if (graphEdge.hasAttributes()) {
            result.append(" [");
            FieldStream.edge(graphEdge).apply((name, value) -> {
                result.append(name).append('=').append(value).append(',');
            });
            result.deleteCharAt(result.length() - 1).append("]");
        }
        newline();
        return this;
    }

    private DotRenderer renderStatementEdge(String sourceId, String targetId, boolean directed) {
        return renderStatementEdge(sourceId, targetId, directed, true);
    }

    private DotRenderer renderStatementEdge(String sourceId, String targetId, boolean directed, boolean terminateLine) {
        indent();
        result.append(sourceId);
        if (directed) {
            result.append(" -> ");
        } else {
            result.append(" -- ");
        }
        result.append(targetId);
        if (terminateLine) {
            newline();
        }
        return this;
    }

    private DotRenderer renderStatementNode(GraphNode graphNode) {
        indent();
        result.append(graphNode.getId()).append(" [");

        FieldStream.node(graphNode).apply((name, value) -> {
            result.append(name).append('=');
            if (value instanceof String) {
                result.append('"');
            }

            result.append(value);

            if (value instanceof String) {
                result.append('"');
            }
            result.append(',');
        });
        result.deleteCharAt(result.length() - 1).append(']');
        newline();
        return this;
    }

    private void indent() {
        result.append("  ");
    }

    private void newline() {
        result.append("\n");
    }
}
