package com.github.frtu.dot;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GraphTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphTest.class);

    public static final String GRAPH_ID = "GraphID";

    @Test
    public void createBaseGraph() {
        Graph graph = new Graph(GRAPH_ID);
        Assert.assertEquals(GRAPH_ID, graph.getGraphID());
        Assert.assertNotNull("List should not be null", graph.getAllNodes());
        Assert.assertNull("Not initialized", graph.getRootNode());
        Assert.assertNull("Not initialized", graph.getCurrentParentNode());
        Assert.assertTrue("Not initialized", graph.getAllNodes().isEmpty());
    }

    @Test
    public void testComplexGraph() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph(GRAPH_ID);
        final GraphNode expectedRootNode = buildComplexGraph(graph);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        final GraphNode actualRootNode = graph.getRootNode();
        Assert.assertEquals(expectedRootNode, actualRootNode);
        Assert.assertEquals(3, actualRootNode.children.size());
        Assert.assertTrue(actualRootNode.children.stream().allMatch(node -> node.label.startsWith("label")));

        // ORDERED ADD
        final List<GraphNode> node3Children = graph.getNode("id3").children;
        Assert.assertEquals(2, node3Children.size());
        Assert.assertTrue(node3Children.stream().allMatch(node -> node.label.startsWith("childLabel3-")));

        // UNORDERED ADD
        final List<GraphNode> node2Children = graph.getNode("id2").children;
        Assert.assertEquals(2, node2Children.size());
        Assert.assertTrue(node2Children.stream().allMatch(node -> node.label.startsWith("childLabel2-")));

        final List<GraphNode> children32 = graph.getNode("child3_2").children;
        Assert.assertTrue(children32.stream().allMatch(node -> node.label.startsWith("subchildLabel3-2-")));
    }

    static GraphNode buildComplexGraph(Graph graph) {
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        // First node is always the root & latest known current
        final String rootId = "root";
        final String rootLabel = "rootLabel";
        final GraphNode expectedRootNode = graph.addNode(rootId, rootLabel, PolygonShapeDotEnum.POLYGON);

        // Adding child node happen on the latest known
        graph.addNode("id1", "label1", PolygonShapeDotEnum.BOX);
        final GraphNode node2 = graph.addNode("id2", "label2", PolygonShapeDotEnum.BOX);

        // Adding child node and set it as the current -> create level 2
        graph.addNodeMoveToChild("id3", "label3", PolygonShapeDotEnum.BOX);
        graph.addNode("child3_1", "childLabel3-1", PolygonShapeDotEnum.CIRCLE);

        // Adding child node and set it as the current -> create level 3
        graph.addNodeMoveToChild("child3_2", "childLabel3-2", PolygonShapeDotEnum.CIRCLE);
        graph.addNode("subchild3_2_1", "subchildLabel3-2-1", PolygonShapeDotEnum.CIRCLE);

        // Adding child node to a previously known parent
        graph.addNodeMoveParent("child2_2", "childLabel2-2", PolygonShapeDotEnum.ELLIPSE, node2);
        graph.addNode("child2_3", "childLabel2-3", PolygonShapeDotEnum.CIRCLE);


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(graph.toString());
        }
        return expectedRootNode;
    }
}