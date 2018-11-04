# Maven artefact generator

Note : The artecfacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.2/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

Feel free to inline some of the dependencies or all dependencies & remove it.

## Overview

Allow to generate a base project for 

- Avro & Schema registry, 
- Kafka pub/sub project
- Spark platform

## Catalog

A **Module** is a standalone project with one single pom OR can be hosted in a Platform projet.

A **Platform** means that it comes with a parent pom, hosting a sub project. When you generate another module at its root folder, the new module is append next to the others.

Generate archetypes with the below commands :

- Replace **x.y.z** with the latest version

### Avro module
[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/avro-project-archetype.svg?label=latest%20release%20:%20avro-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22avro-project-archetype%22+g%3A%22com.github.frtu.archetype%22)

> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=avro-project-archetype -DarchetypeVersion=x.y.z

### Kafka platform

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/plt-kafka-project-archetype.svg?label=latest%20release%20:%20plt-kafka-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22plt-kafka-project-archetype%22+g%3A%22com.github.frtu.archetype%22)


> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=x.y.z

### Spark platform

#### Generate the project

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/plt-spark-project-archetype.svg?label=latest%20release%20:%20plt-spark-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22plt-spark-project-archetype%22+g%3A%22com.github.frtu.archetype%22)


> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=x.y.z

#### Run & import the Spark module

In the **sub module** containing the Spark application, you can run the main class using

> mvn clean compile exec:java

In IntelliJ, you can choose to 

- Import directly the **sub module** so that you can run directly the [Starter.scala](https://github.com/frtu/governance-toolbox/blob/master/archetype/plt-spark-project/src/main/resources/archetype-resources/__rootArtifactId__/src/main/scala/Starter.scala). 
- (ADVANCED) Import the parent pom along with the sub modules. Before running the [Starter.scala](https://github.com/frtu/governance-toolbox/blob/master/archetype/plt-spark-project/src/main/resources/archetype-resources/__rootArtifactId__/src/main/scala/Starter.scala), don't forget to Edit Configuration and move the Working directory one folder down.