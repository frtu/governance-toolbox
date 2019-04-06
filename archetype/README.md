# Maven artefact generator

Note : The artecfacts below all rely on [base-pom](https://search.maven.org/artifact/com.github.frtu.archetype/base-pom/0.3.4/pom) that helps to normalize all the libraries CVE fixes and version upgrades.

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
#### Generate the project
[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/avro-project-archetype.svg?label=latest%20release%20:%20avro-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22avro-project-archetype%22+g%3A%22com.github.frtu.archetype%22)

> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=avro-project-archetype -DarchetypeVersion=x.y.z

Parameter asked :

* DatamodelClassName : Name of the generate class name inside avsc

#### Avro library version

Avro comes with the version [1.8.2](https://search.maven.org/artifact/org.apache.avro/avro/1.8.2/bundle) but feel free to upgrade it to the latest version. Check at :

* [Search maven - g:org.apache.avro AND a:avro](https://search.maven.org/search?q=g:org.apache.avro%20AND%20a:avro&core=gav)

#### Import project in IntelliJ

When importing to IntelliJ or Eclipse, make sure you generate the sources. In IntelliJ, right click on the project > Maven > Generate Sources and Update Folders.

### Kafka platform

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/plt-kafka-project-archetype.svg?label=latest%20release%20:%20plt-kafka-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22plt-kafka-project-archetype%22+g%3A%22com.github.frtu.archetype%22)


> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=x.y.z

Parameter asked :

* avro-model-artifact-id : Artifact name of the Avro data model project
* DatamodelClassName : Name of the class name of the Avro data model project previously

### Spark platform

#### Generate the project

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.archetype/plt-spark-project-archetype.svg?label=latest%20release%20:%20plt-spark-project-archetype"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22plt-spark-project-archetype%22+g%3A%22com.github.frtu.archetype%22)


> mvn archetype:generate -DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-kafka-project-archetype -DarchetypeVersion=x.y.z

#### System requirements

The generated project uses **net.alchim31.maven:scala-maven-plugin:3.1.0** plugins.

On IntelliJ :

* you may also change your settings to use your system version : **Preferences > Build, Exec > Build Tools > Maven**. 
* Set **Maven home directory** to the one of your system.

#### Run & import the Spark module

In the **sub module** containing the Spark application, you can run the main class using

> mvn clean compile exec:java

In IntelliJ, you can choose to 

- Import directly the **sub module** so that you can run directly the [Starter.scala](https://github.com/frtu/governance-toolbox/blob/master/archetype/plt-spark-project/src/main/resources/archetype-resources/__rootArtifactId__/src/main/scala/Starter.scala). 
- (ADVANCED) Import the parent pom along with the sub modules. Before running the [Starter.scala](https://github.com/frtu/governance-toolbox/blob/master/archetype/plt-spark-project/src/main/resources/archetype-resources/__rootArtifactId__/src/main/scala/Starter.scala), don't forget to Edit Configuration and move the Working directory one folder down.

## Spark platform tutorial

### Generate a Spark app

Generate your Spark app with :

```
mvn archetype:generate -DarchetypeCatalog=local \
-DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=plt-spark-project-archetype -DarchetypeVersion=0.3.3 \
-DgroupId=com.github.frtu -DartifactId=sparkapp -Dversion=0.0.1-SNAPSHOT
```

Copy into :

- the test resource folder /**(ARTIFACT-ID)**/**(ARTIFACT-ID)**/src/test/resources/data
- a data file (csv, json, ...) with each line one record.

Run it with 

```
. mvn-cmds.bash
runspark
```

It will run Spark in the local machine, read the data file and flush every line to the console.

### Generate an Avro data module

Generate your Avro sub module to manipulate **sales** record with :

```
mvn archetype:generate -DarchetypeCatalog=local \
-DarchetypeGroupId=com.github.frtu.archetype -DarchetypeArtifactId=avro-project-archetype -DarchetypeVersion=0.3.3 \
-DgroupId=com.github.frtu -DartifactId=sales -Dversion=0.0.1-SNAPSHOT
```

When asked a **DatamodelClassName**, type **Sales** which will be the *Class name* of the generated object.

To finish to generate all the artifacts, run : 

```
mvn compile
```

### Work with Data Model & Data

What you got is :

#### Avro data model

* sparkapp/sales/src/main/avro/Sample.avsc : Avro schema filled with the record **Sales** with only one attribute **name**
* sparkapp/sales/src/test/java/com/frtu/Main.java : A test main that allow to **illustrate Avro serialize & deserialize mechanism** with : SpecificDatumWriter > GenericDatumReader

Check the different Avro data type at : [schema type](https://avro.apache.org/docs/1.8.1/spec.html#schemas)

#### Spark application with test data

* sparkapp/sparkapp/src/test/resources/data/Sales.csv : Sample test data using the real format
* sparkapp/sparkapp/src/main/scala/com/frtu/Starter.scala : Spark main app
* sparkapp/sparkapp/src/main/resources/logback.xml : Logback log level setting 

#### Spark application with Avro data model

In **pom.xml** of the spark project, uncomment the section "UNCOMMENT for Internal domain dependencies" and replace the **artifactId** tag with your previously created datamodel project.

### Going forward

You can now map each line of your data with the include Avro data model and run it on Spark !!

~ Have fun !
