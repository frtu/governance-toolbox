# Libraries

Note : The artecfacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.5/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

## Overview

Provide libraries for :

- Standalone project : Avro (Object vs bytes) & JSON serialization / deserialization
- Kafka SerDes project : Serialize & deserialize Avro into Kafka

## Libraries

### Avro SerDes standalone library

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-serdes.svg?label=latest%20release%20:%20library-kafka"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-serdes%22+g%3A%22com.github.frtu.governance%22)

Useful when embedded into Publishing to Kafka (see module below) or Consuming in Streaming application without Kafka.

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-serdes</artifactId>
  <version>0.3.5</version>
</dependency>
```


### Kafka SerDes library

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/library-kafka.svg?label=latest%20release%20:%20library-kafka"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22library-kafka%22+g%3A%22com.github.frtu.governance%22)

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-kafka</artifactId>
  <version>0.3.5</version>
</dependency>
```
