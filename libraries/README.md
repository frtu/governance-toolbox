# Libraries

Note : The artecfacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.5/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

## Overview

Provide libraries for :

- Standalone project : Avro (Object vs bytes) & JSON serialization / deserialization
- Kafka SerDes project : Serialize & deserialize Avro into Kafka

## Libraries

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

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-serdes.svg?label=latest%20release%20:%20library-kafka"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-serdes%22+g%3A%22com.github.frtu.governance%22)


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

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-kafka.svg?label=latest%20release%20:%20library-kafka"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-kafka%22+g%3A%22com.github.frtu.governance%22)
