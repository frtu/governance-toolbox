# Libraries

Note : The artefacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.5/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

## Overview

Provide libraries for :

- Dot notation generator : [Dot notation](https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29) graph generator library.
- Spring Reflection : Library for Class, Annotation reflection and Bean based on spring-context
- Generators : Library for generating objects and populating them
- Standalone project : Avro (Object vs bytes) & JSON serialization / deserialization
- Kafka SerDes project : Serialize & deserialize Avro into Kafka

## Libraries - Metadata

### Dot language library

Implement [DOT Grammar](https://graphviz.gitlab.io/_pages/doc/info/lang.html) description in Java.

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-dot</artifactId>
  <version>${governance-libraries.version}</version>
</dependency>
```

Sample : Allow to generate the two workflow like in [this page](https://dreampuf.github.io/GraphvizOnline/#digraph%20G%20%7B%0A%0A%20%20subgraph%20cluster_0%20%7B%0A%20%20%20%20style%3Dfilled%3B%0A%20%20%20%20color%3Dlightgrey%3B%0A%20%20%20%20node%20%5Bstyle%3Dfilled%2Ccolor%3Dwhite%5D%3B%0A%20%20%20%20a0%20-%3E%20a1%20-%3E%20a2%20-%3E%20a3%3B%0A%20%20%20%20label%20%3D%20%22process%20%231%22%3B%0A%20%20%7D%0A%0A%20%20subgraph%20cluster_1%20%7B%0A%20%20%20%20node%20%5Bstyle%3Dfilled%5D%3B%0A%20%20%20%20b0%20-%3E%20b1%20-%3E%20b2%20-%3E%20b3%3B%0A%20%20%20%20label%20%3D%20%22process%20%232%22%3B%0A%20%20%20%20color%3Dblue%0A%20%20%7D%0A%20%20start%20-%3E%20a0%3B%0A%20%20start%20-%3E%20b0%3B%0A%20%20a1%20-%3E%20b3%3B%0A%20%20b2%20-%3E%20a3%3B%0A%20%20a3%20-%3E%20a0%3B%0A%20%20a3%20-%3E%20end%3B%0A%20%20b3%20-%3E%20end%3B%0A%0A%20%20start%20%5Bshape%3DMdiamond%5D%3B%0A%20%20end%20%5Bshape%3DMsquare%5D%3B%0A%7D).

```Java
SuperGraph superGraph = new SuperGraph("G");
superGraph.setRankdir("LR");

//--------------------------------------
// Subgraph : cluster_0
//--------------------------------------
final Graph cluster_0 = superGraph.newSubgraph("cluster_0");
cluster_0.newGraphAttributes()
        .setStyle("filled")
        .setColor("lightgrey");
cluster_0.newNodeAttributes()
        .setStyle("filled")
        .setColor("white");

// Short syntax
cluster_0.addEdge("a0", "a1", "a3");

//--------------------------------------
// Subgraph : cluster_1
//--------------------------------------
final Graph cluster_1 = superGraph.newSubgraph("cluster_1");
cluster_1.newEdgeAttributes().setColor("red");
cluster_1.addEdge("b0", "b1", "b2", "b3");

//--------------------------------------
// Super graph itself
//--------------------------------------
final GraphNode start = superGraph.addSingleNode("start", PolygonShapeDotEnum.MDIAMOND);
final GraphNode end = superGraph.addSingleNode("end", PolygonShapeDotEnum.MSQUARE);

// Long syntax
superGraph.addEdge(start, "a0").setStyle("dotted");
superGraph.addEdge(start, "b0");
superGraph.addEdge("a1", "b3");
superGraph.addEdge("b2", "a3").setColor("red");
superGraph.addEdge("a3", "a0");
superGraph.addEdge("a3", end);
superGraph.addEdge("b3", end).setStyle("dotted").setColor("blue");

//--------------------------------------
// Render
//--------------------------------------
final DotRenderer dotRenderer = new DotRenderer();
// boolean : directed graphs or undirected graphs
final String renderGraph = dotRenderer.renderGraph(graph, true);
```


See sample usage, generate a dot graph [with only a few lines of Avro Record navigation logic](https://github.com/frtu/governance-toolbox/blob/master/schema-registries/schema-maven-plugin/src/main/java/com/github/frtu/schema/utils/AvroDotGenerator.java#L51-L91).


Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-dot.svg?label=latest%20release%20:%20library-dot"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-dot%22+g%3A%22com.github.frtu.governance%22)

For more details, see [library-dot](library-dot)

## Libraries - Reflection utilities

### Spring Reflection

Library for Class, Annotation reflection and Bean based on spring-context.

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-spring-reflection</artifactId>
  <version>${governance-libraries.version}</version>
</dependency>
```

#### @LightConditional* annotation

[Spring Boot conditional](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/package-frame.html) without Spring Boot.

* LightConditionalOnBean
* LightConditionalOnClass
* LightConditionalOnMissingBean
* LightConditionalOnMissingClass

#### Annotation class based on java Class or Method

* AnnotationMethodScanner : scan Class or Method for specific annotation

```Java
final AnnotationMethodScanner<Class<ExecutionSpan>, Class<ToLog>> scanner = AnnotationMethodScanner.of(ExecutionSpan.class, ToLog.class);

// Scan Class
final Multimap<String, AnnotationMethodScan<Class<? extends Annotation>, Class<? extends Annotation>>> multimap = scanner
        .scan(ExecutionSpanConfiguration.class);

// Scan Method
final Method spanMethod = ExecutionSpanConfiguration.class.getMethod("spanWithTags");
final AnnotationMethodScan spanMethodScan = scanner.scan(spanMethod);
```

#### Reflection and Bean manipulation

* EnumUtil & EnumScanner : Allow to scan & get all Enum inner fields
* BeanGenerator : generate class instance and populate them using field name
* ClassloaderUtil : Classpath refresh based on new file paths

##### EnumUtil & EnumScanner

* EnumScanner : scan package for all Enums #scan() or #scan(EnumInterfaceMarker.class)
* EnumUtil : can directly use static method OR instance using #of("field1", "field2")

```Java
final String packageToScan = "com.github.frtu.samples.enums";
final Class<EnumInterfaceMarker> type = EnumInterfaceMarker.class;

final Set<Class<? extends Enum>> scanEnum = EnumScanner.of(packageToScan).scan(type);
final EnumUtil enumUtil = EnumUtil.of("description");

scanEnum.stream().map(EnumUtil::getEnumValues).forEach(listOfEnumsOfOneKind -> {
            // A list of all the enum of one kind
            LOGGER.debug("========== {} ============", listOfEnumsOfOneKind.get(0).getClass());
            listOfEnumsOfOneKind.stream().forEach(enumsOfOneKind -> {
                final Map<String, Object> values = enumUtil.getSomeValues(enumsOfOneKind);
                LOGGER.debug("* {} -> {}", values.get("name"), values.get("description"));
            });
        }
);
```

## Libraries - Data

### Avro SerDes standalone library

Very lightweight Avro SerDes library (no dependency to Kafka libs) that you can use for :

* Java or Scala
* Kafka or Spark
* Create many level of Avro objects by inserting into binary Avro field

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-serdes</artifactId>
  <version>${library-serdes.version}</version>
</dependency>
```
Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-serdes.svg?label=latest%20release%20:%20library-serdes"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-serdes%22+g%3A%22com.github.frtu.governance%22)


### Kafka SerDes library

Allow to create your own Kafka [Serializer](https://kafka.apache.org/20/javadoc/org/apache/kafka/common/serialization/Serializer.html) / [Deserializer](https://kafka.apache.org/20/javadoc/org/apache/kafka/common/serialization/Deserializer.html) with :

* Using only a Avro schema in the classpath or file using Avro [generic package](http://avro.apache.org/docs/current/api/java/org/apache/avro/generic/package-summary.html).
* Or generate POJO object and pass it to the constructor using [generic package](http://avro.apache.org/docs/current/api/java/org/apache/avro/specific/package-summary.html).

#### Generic package

Kafka **publisher** properties :

```Java
props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializerAvroRecord.class.getName());
```

Kafka **consumer** properties :

```Java
props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAvroRecord.class.getName());
props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "classpath:dummy_data.avsc");
```

#### Specific package

Kafka **publisher** properties :

```Java
props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializerAvroRecord.class.getName());
props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_GENERIC_AVRO_READER, Boolean.FALSE);
```

Kafka **consumer** properties :

```Java
props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAvroRecord.class.getName());
props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_SCHEMA_CLASSPATH_LOCATION, "classpath:dummy_data.avsc");
props.put(BaseKafkaAvroRecordSerdes.CONFIG_KEY_GENERIC_AVRO_READER, Boolean.FALSE);
```
Or by extending _KafkaDeserializerAvroRecord_ and pass the Avro POJO class into the constructor that you can use directly :

```Java
public class DummyDataKafkaDeserializerAvroRecord extends KafkaDeserializerAvroRecord<DummyData> {
    public DummyDataKafkaDeserializerAvroRecord() {
        super(DummyData.getClassSchema());
    }
}
```

#### Integration

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-kafka</artifactId>
  <version>${library-kafka.version}</version>
</dependency>
```

**Notes** : Please do not use the version **0.3.5 of library-kafka**, since some classes has been renamed, to avoid visual confusion with Confluent Avro serdes (Don't have the 2 chances to make things right) ;).


### Kafka publisher / consumer library

Provide basic initialization on top of [spring-kafka](https://spring.io/projects/spring-kafka) to create publisher & consumer (base package com.github.frtu.kafka.config.*) :

* producer.ProducerConfiguration\<K, V> : allow to create [KafkaTemplate](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/KafkaTemplate.html) just by importing it in @ComponentScan.
* consumer.ConsumerConfiguration\<K, V> : allow to bootstrap [@KafkaListener](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/KafkaListener.html) usage just by importing it in @ComponentScan.


Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-kafka</artifactId>
  <version>${library-kafka.version}</version>
</dependency>
```
Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-kafka.svg?label=latest%20release%20:%20library-kafka"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-kafka%22+g%3A%22com.github.frtu.governance%22)

