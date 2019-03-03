package com.github.frtu.schema.utils;

import com.github.frtu.dot.DotRenderer;
import com.github.frtu.dot.model.Graph;
import com.github.frtu.dot.model.GraphNode;
import com.github.frtu.dot.model.PolygonShapeDotEnum;
import com.github.frtu.dot.utils.IdUtil;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Generate a Dot graph from Avro schema.
 *
 * @author frtu
 * @since 0.3.6
 */
public class AvroDotGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroDotGenerator.class);

    private final static HashMap<Schema.Type, PolygonShapeDotEnum> typeMappingToShape;

    static {
        typeMappingToShape = new HashMap<>();
        typeMappingToShape.put(Schema.Type.STRING, PolygonShapeDotEnum.BOX);
        typeMappingToShape.put(Schema.Type.BYTES, PolygonShapeDotEnum.BOX3D);
        typeMappingToShape.put(Schema.Type.INT, PolygonShapeDotEnum.PENTAGON);
        typeMappingToShape.put(Schema.Type.LONG, PolygonShapeDotEnum.HEXAGON);
        typeMappingToShape.put(Schema.Type.FLOAT, PolygonShapeDotEnum.SEPTAGON);
        typeMappingToShape.put(Schema.Type.DOUBLE, PolygonShapeDotEnum.OCTAGON);
        typeMappingToShape.put(Schema.Type.BOOLEAN, PolygonShapeDotEnum.DIAMOND);
        typeMappingToShape.put(Schema.Type.RECORD, PolygonShapeDotEnum.FOLDER);
        typeMappingToShape.put(Schema.Type.ARRAY, PolygonShapeDotEnum.COMPONENT);
        typeMappingToShape.put(Schema.Type.MAP, PolygonShapeDotEnum.CYLINDER);
        typeMappingToShape.put(Schema.Type.FIXED, PolygonShapeDotEnum.MSQUARE);
        typeMappingToShape.put(Schema.Type.UNION, PolygonShapeDotEnum.FOLDER);
        typeMappingToShape.put(Schema.Type.ENUM, PolygonShapeDotEnum.TAB);
    }

    private Graph graph;

    public AvroDotGenerator(String graphName) {
        final String filteredGraphName = IdUtil.formatId(graphName);
        LOGGER.debug("Generate graph = {}", filteredGraphName);
        this.graph = new Graph(filteredGraphName);
    }

    public String generateGraph(Schema schema) {
        buildGraphNode(schema, schema.getNamespace(), schema.getName(), null);

        final DotRenderer graphRenderer = new DotRenderer();
        return graphRenderer.renderDirectedGraph(this.graph);
    }

    private void buildGraphNode(Schema schema, String namespace, String name, GraphNode parentNode) {
        final String parentNodeId = (parentNode == null) ? IdUtil.formatId(namespace) : parentNode.getId();
        String nodeId = String.format("%s_%s", parentNodeId, name);
        String nodeLabel = (parentNode == null) ? name : String.format("%s.%s", parentNode.getLabel(), name);
        final PolygonShapeDotEnum polygonShape = (parentNode == null) ? PolygonShapeDotEnum.MDIAMOND : typeMappingToShape.get(schema.getType());

        LOGGER.debug("Add Node ID={} Label={} Shape={}", nodeId, nodeLabel, polygonShape);
        GraphNode graphNode = this.graph.addNodeMoveParent(nodeId, nodeLabel, polygonShape, parentNode);

        if (Schema.Type.UNION.equals(schema.getType())) {
            for (Schema schemaItem : schema.getTypes()) {
                final Schema.Type schemaItemType = schemaItem.getType();

                if (!Schema.Type.NULL.equals(schemaItemType)) {
                    final PolygonShapeDotEnum shape = typeMappingToShape.get(schemaItemType);
                    LOGGER.debug("Replace Node ID={} Shape={} Type={}", nodeId, schemaItemType, shape);

                    graphNode.setShape(shape);
                    buildGraphChildren(schemaItem, namespace, graphNode);
                }
            }
        }
        buildGraphChildren(schema, namespace, graphNode);
    }

    private void buildGraphChildren(Schema schema, String namespace, GraphNode parentNode) {
        if (Schema.Type.RECORD.equals(schema.getType())) {
            parentNode.setShape(typeMappingToShape.get(Schema.Type.RECORD));
            final List<Schema.Field> fields = schema.getFields();
            fields.forEach(field -> {
                buildGraphNode(field.schema(), namespace, field.name(), parentNode);
            });
        }
    }
}
