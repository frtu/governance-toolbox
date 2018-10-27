# Maven artefact generator

Note : The artecfacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.2/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

Feel free to inline dependencies and remove it.

## Avro & Schema-registry

Allow to generate a base project for Avro, Schema registry, publishing Kafka or Spark project.

Generate using :

- Avro

> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=avro-project-archetype -DarchetypeVersion=0.3.3

## Kafka

> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=0.3.3

## Spark

### Generate the project

> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=0.3.3

### Run & import the Spark module

In the **sub module** containing the Spark application, you can run the main class using

> mvn clean compile exec:java

In IntelliJ, Import the **sub module** if you want the file path to be consistent. 