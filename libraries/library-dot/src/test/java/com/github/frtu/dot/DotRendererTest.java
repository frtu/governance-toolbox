package com.github.frtu.dot;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DotRendererTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DotRendererTest.class);

    @Test
    public void testRenderGraphDirected() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph("DirectedGraphID");
        GraphTest.buildComplexGraph(graph);
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderGraph(graph, true);

        LOGGER.debug(renderGraph);
        System.out.println(renderGraph);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(renderGraph.contains("->"));
        Assert.assertTrue(!renderGraph.contains("--"));
        Assert.assertTrue(renderGraph.contains("digraph "));
    }

    @Test
    public void testRenderGraphUnirected() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph("UnirectedGraphID");
        GraphTest.buildComplexGraph(graph);
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderGraph(graph, false);

        LOGGER.debug(renderGraph);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(!renderGraph.contains("->"));
        Assert.assertTrue(renderGraph.contains("--"));
        Assert.assertTrue(renderGraph.contains("graph"));
    }
}