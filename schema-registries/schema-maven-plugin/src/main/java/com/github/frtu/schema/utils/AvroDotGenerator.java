package com.github.frtu.schema.utils;

import com.github.frtu.dot.DotRenderer;
import com.github.frtu.dot.model.Graph;
import com.github.frtu.dot.model.GraphNode;
import com.github.frtu.dot.model.PolygonShapeDotEnum;
import com.github.frtu.dot.utils.IdUtil;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AvroDotGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroDotGenerator.class);
    private Graph graph;

    public AvroDotGenerator(String graphName) {
        final String filteredGraphName = IdUtil.formatId(graphName);
        this.graph = new Graph(filteredGraphName);
    }

    public String generateGraph(Schema schema) {
        buildGraphNode(schema, schema.getNamespace(), schema.getName(), null);

        final DotRenderer graphRenderer = new DotRenderer();
        return graphRenderer.renderDirectedGraph(graph);
    }

    private void buildGraphNode(Schema schema, String namespace, String name, GraphNode parentNode) {
        final String parentNodeId = (parentNode == null) ? IdUtil.formatId(namespace) : parentNode.getId();
        String nodeId = String.format("%s_%s", parentNodeId, name);
        String nodeLabel = (parentNode == null) ? name : String.format("%s.%s", parentNode.getLabel(), name);
        final PolygonShapeDotEnum polygonShape = (parentNode == null) ? PolygonShapeDotEnum.MDIAMOND : PolygonShapeDotEnum.PLAIN;

        GraphNode graphNode = graph.addNodeMoveParent(nodeId, nodeLabel, polygonShape, parentNode);

        if (Schema.Type.UNION.equals(schema.getType())) {
            for (Schema schemaItem : schema.getTypes()) {
                buildGraphChildren(schemaItem, namespace, graphNode);
            }
        }
        buildGraphChildren(schema, namespace, graphNode);
    }

    private void buildGraphChildren(Schema schema, String namespace, GraphNode parentNode) {
        if (Schema.Type.RECORD.equals(schema.getType())) {
            parentNode.setShape(PolygonShapeDotEnum.ELLIPSE);
            final List<Schema.Field> fields = schema.getFields();
            fields.forEach(field -> {
                buildGraphNode(field.schema(), namespace, field.name(), parentNode);
            });
        }
    }
}
