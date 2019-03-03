# Maven Plugins

## Schema maven plugins

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.governance/schema-maven-plugin.svg?label=latest%20release%20:%20schema-maven-plugin"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22schema-maven-plugin%22+g%3A%22com.github.frtu.governance%22)

#### Mojo :pojo2json
Allow to generate an Avro schema based on POJO (Interface or Implementation)

* includePackage : which package to scan
* subtypesOf : filter all the classes that extends the specified interface or parent class
* outputDirectory : specify the folder into which plugin should generate the .avsc files (One per filtered class found).

Import into your pom.xml with :

```XML
<plugin>
    <groupId>com.github.frtu.governance</groupId>
    <artifactId>schema-maven-plugin</artifactId>
    <version>x.y.z</version>
    <executions>
        <execution>
            <phase>process-resources</phase>
            <goals>
                <goal>pojo2avro</goal>
            </goals>
            <configuration>
                <includePackage>tests</includePackage>
                <subtypesOf>tests.pojo.UserInterface</subtypesOf>
                <outputDirectory>${basedir}/target/generated-sources/</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```
See [sample project](https://github.com/frtu/governance-toolbox/tree/master/samples/schema-maven-usage).

* Run with :
```
mvn -Pgenerate process-resources
```

#### Mojo :avro2dot (since 0.3.6)

Allow to generate an Dot Graph from an Avro schema

* schemaPath : source folder of all the Avro schema files (*.avsc)
* outputDirectory : specify the folder into which plugin should generate the .dot files (One per avsc found).

Import into your pom.xml with :

```XML
<plugin>
    <groupId>com.github.frtu.governance</groupId>
    <artifactId>schema-maven-plugin</artifactId>
    <version>x.y.z</version>
    <executions>
        <execution>
            <phase>process-resources</phase>
            <goals>
                <goal>avro2dot</goal>
            </goals>
            <configuration>
                <schemaPath>${project.basedir}/src/main/resources/</schemaPath>
                <outputDirectory>${basedir}/target/generated-sources/</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

See [sample project](https://github.com/frtu/governance-toolbox/tree/master/samples/schema-maven-usage).

* Run with :
```
mvn -PgenerateDot process-resources
```

## Hortonworks Schema-registry

This plugin allow to scan all Avro schema in the project folder /src/main/avro/*.avsc and register it into 'schemaregistry.url'

```XML
<properties>
	<governance-maven-plugin.version>0.3.0</governance-maven-plugin.version>
	<schemaregistry.url>http://localhost:9090</schemaregistry.url>
</properties>

<build>
	<plugins>
		<plugin>
			<groupId>com.github.frtu.governance</groupId>
			<artifactId>schema-registries-maven-plugin</artifactId>
			<version>${governance-maven-plugin.version}</version>
		</plugin>
	</plugins>
</build>
```

## Confluent Schema-registry

This plugin allow to read ONLY specified  Avro schema in the project folder /src/main/avro/*.avsc and register it into 'schemaRegistryUrls'

```XML
<plugin>
    <groupId>io.confluent</groupId>
    <artifactId>kafka-schema-registry-maven-plugin</artifactId>
    <version>4.0.0</version>
    <configuration>
        <schemaRegistryUrls>
            <param>http://192.168.99.100:8081</param>
        </schemaRegistryUrls>
        <subjects>
            <TestSubject000-key>src/main/avro/TestSubject000-Key.avsc</TestSubject000-key>
            <TestSubject000-value>src/main/avro/TestSubject000-Value.avsc</TestSubject000-value>
        </subjects>
    </configuration>
    <goals>
        <goal>register</goal>
    </goals>
</plugin>
```


* [Confluent Maven plugin docs : schema-registry:register](
https://docs.confluent.io/current/schema-registry/docs/maven-plugin.html#schema-registry-register)
* [Confluent Maven plugin java source](
https://github.com/confluentinc/schema-registry/tree/master/maven-plugin/src/main/java/io/confluent/kafka/schemaregistry/maven)
