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

    public SuperGraph(String id) {
        super(id);
    }

    public SuperGraph(String id, String rankdir) {
        super(id);
        this.setRankdir(rankdir);
    }

    /**
     * ATTENTION SIDE EFFECT : Use soft copy so ensure to ONLY call after all modification is done on the original object.
     *
     * @param graph
     */
    protected SuperGraph(Graph graph) {
        super(graph.getId());
        this.allNodes = graph.getAllNodes();
        this.edges = graph.getAllEdges();
        this.primoNodes = graph.getPrimoNodes();
        this.currentParentNode = graph.getCurrentParentNode();
    }

    /**
     * Allow to create and add a subgraph into SuperGraph.
     *
     * @param subgraphName name of the subgraph
     * @return newly built graph
     * @since 0.3.7
     */
    public Graph newSubgraph(String subgraphName) {
        Graph subgraph = new Graph(subgraphName);
        this.addSubgraph(subgraph);
        return subgraph;
    }

    public void addSubgraph(Graph subgraph) {
        this.subgraphs.add(subgraph);
    }

    public List<Graph> getSubgraphs() {
        return subgraphs;
    }

    /**
     * @param rankdir
     * @return itself (since 0.3.7)
     */
    public SuperGraph setRankdir(String rankdir) {
        this.rankdir = rankdir;
        return this;
    }

    public String getRankdir() {
        return rankdir;
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
        this.getPrimoNodes().forEach(primoNode -> {
            stringBuilder.append(primoNode.toString()).append('\n');
            buildChildren(stringBuilder, primoNode, 1);
        });
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
