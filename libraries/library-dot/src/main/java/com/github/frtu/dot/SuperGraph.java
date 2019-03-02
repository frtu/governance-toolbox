package com.github.frtu.dot;

import com.github.frtu.dot.model.Graph;
import com.github.frtu.dot.model.GraphNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Util method to auto parse all class fields for rendering.
 *
 * @author frtu
 * @since 0.3.6
 */
public class SuperGraph extends Graph {
    private List<Graph> subgraphs = new ArrayList<>();
    private String rankdir;

    /**
     * ATTENTION SIDE EFFECT : Use soft copy so ensure to ONLY call after all modification is done on the original object.
     *
     * @param graph
     */
    public SuperGraph(Graph graph) {
        super(graph.getId());
        this.allNodes = graph.getAllNodes();
        this.edges = graph.getAllEdges();
        this.rootNode = graph.getRootNode();
        this.currentParentNode = graph.getCurrentParentNode();
    }

    public SuperGraph(String id) {
        super(id);
    }

    public SuperGraph(String id, String rankdir) {
        super(id);
        this.rankdir = rankdir;
    }

    public void addSubgraph(Graph subgraph) {
        this.subgraphs.add(subgraph);
    }

    public List<Graph> getSubgraphs() {
        return subgraphs;
    }

    public String getRankdir() {
        return rankdir;
    }

    public void setRankdir(String rankdir) {
        this.rankdir = rankdir;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[id='").append(getId()).append("\']\n");
        stringBuilder.append("* rankdir='").append(rankdir).append("\'\n");
        for (Graph graph : subgraphs) {
            stringBuilder.append("---------------------\n");
            stringBuilder.append(graph.toString());
            stringBuilder.append("---------------------\n");
        }
        final GraphNode rootNode = this.getRootNode();
        if (rootNode != null) {
            stringBuilder.append(rootNode.toString()).append('\n');
            buildChildren(stringBuilder, rootNode, 1);
        }
        return stringBuilder.toString();
    }

    private void buildChildren(StringBuilder stringBuilder, GraphNode currentNode, int level) {
        currentNode.getChildren().forEach(child -> {
            stringBuilder.append("|-");
            for (int i = 1; i < level; i++) {
                stringBuilder.append('-');
            }
            stringBuilder.append(' ').append(child).append('\n');
            if (!child.getChildren().isEmpty()) {
                buildChildren(stringBuilder, child, level + 1);
            }
        });
    }
}
