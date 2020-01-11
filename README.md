# governance-toolbox
A toolbox for automation & docker images around metadata governance :

Static metdata

* Schema registry : to store schema & versions

Dynamic metadata

* InfluxDB : TSDB to store PiT (Point In Time) based metadata.

## [Archetype](https://github.com/frtu/governance-toolbox/tree/master/archetype)

* avro : generate a base project for avro data model, generate & compile
* plt-kafka : standalone multi modules project to Publish & Consume Kafka
* plt-spark : standalone multi modules project to run Spark

## [Libraries](https://github.com/frtu/governance-toolbox/tree/master/libraries)

Metadata :

* **library-dot** : Dot graph generator library

Data :

* **library-serdes** : Serialization Deserialization of Avro <-> JSON
* **library-kafka** : Wrap Avro SerDes pack into Kafka Publisher & Consumer

## [Maven Plugins](https://github.com/frtu/governance-toolbox/tree/master/schema-registries)

* **schema-maven-plugin:pojo2json** : Allow to generate an Avro schema based on POJO (Interface or Implementation). See [sample project](https://github.com/frtu/governance-toolbox/tree/master/samples/schema-maven-usage).

* **schema-maven-plugin:avro2dot** (since 0.3.6) : Allow to generate an Dot Graph from an Avro schema. See [sample project](https://github.com/frtu/governance-toolbox/tree/master/samples/schema-maven-usage).

* **schema-registries-maven-plugin:register** : Register all Avro schema from a folder into Hortonworks Schema-registry

## SerDes platform

| Characteristics          | [Avro](http://avro.apache.org/docs/current/)                                                                                  | [Protobuf](https://developers.google.com/protocol-buffers/docs/proto3) [(source)](https://github.com/protocolbuffers/protobuf)                                                                    |
|--------------------------|---------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| Dynamic typing           | YES                                                                                   | NO (needs to compile)                                                       |
| Backward compatibilities | Complex                                                                               | [Easy](https://developers.google.com/protocol-buffers/docs/proto3#updating) |
| Support null values      | Medium (Complex data struct)                                                          |                                                                             |
| Performance              |                                                                                       | Very good                                                                   |
| Compactness              |                                                                                       | Excellent                                                                   |
| Current toolbox support  | [Good](https://github.com/frtu/governance-toolbox/tree/master/archetype/avro-project) | [Good](https://github.com/frtu/governance-toolbox/tree/master/ext-protobuf) |
## Docker images
### Confluent Stack (Kafka, REST API, ...)

Here are the different URLs

- Kafka Bootstrap server : broker:9092

In order to make it works, you will need to map 'broker' to 127.0.0.1 in /etc/hosts. You can call the script at [docker-schema-registry.bash](https://github.com/frtu/governance-toolbox/blob/master/schema-registries/docker-schema-registry.bash#L30-L34)

> kafkahost

### Confluent Schema-registry & Landoop UI

- Schema Registry REST API : [http://localhost:8081](http://localhost:8081)
- Schema Registry UI : [http://localhost:8001](http://localhost:8001)

It serves the schema-registry-ui from port 8001.
A live version can be found at [Landoop Demo of schema-registry-ui](https://schema-registry-ui.landoop.com)

* [https://hub.docker.com/r/landoop/schema-registry-ui/](https://hub.docker.com/r/landoop/schema-registry-ui/)
* [https://hub.docker.com/r/confluentinc/cp-schema-registry/](https://hub.docker.com/r/confluentinc/cp-schema-registry/)

FYI find the API specs at : [Confluent Schema Registry APIs](
https://docs.confluent.io/current/schema-registry/docs/api.html)

Attention this require at least Docker Compose 2.1 to allow Docker images start with order & validation.

### Confluent Kafka REST & Landoop Topic UI

- Kafka REST API : [http://localhost:8082](http://localhost:8082)
- Landoop Topic UI : [http://localhost:8002](http://localhost:8002)

### Hortonworks Schema-registry

Using alternative Docker image. For more details : 

* [https://hub.docker.com/r/thebookpeople/hortonworks-registry/](https://hub.docker.com/r/thebookpeople/hortonworks-registry/)

### InfluxDB & Grafana

Using official Docker image. Look at this link for more details on the port : 

* Docker InfluxDB : [https://hub.docker.com/_/influxdb/](https://hub.docker.com/_/influxdb/)
* Docker Grafana : [https://hub.docker.com/r/grafana/grafana/](https://hub.docker.com/r/grafana/grafana/)

## Maven artefact generator

See [Archetype](https://github.com/frtu/governance-toolbox/tree/master/archetype)
