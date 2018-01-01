# governance-toolbox
A toolbox for automation & docker images around metadata governance :

Static metdata

* Schema registry : to store schema & versions

Dynamic metadata

* InfluxDB : TSDB to store PiT (Point In Time) based metadata.


## Maven client
### Hortonworks Schema-registry

This plugin allow to scan all Avro schema in the project folder /src/main/avro/*.avsc and register it into 'schemaregistry.url'

```XML
	<properties>
		<governance-maven-plugin.version>0.2.0</governance-maven-plugin.version>
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

### Confluent Schema-registry

Already exist at :
[Confluent Maven plugin : schema-registry:register](
https://docs.confluent.io/current/schema-registry/docs/maven-plugin.html#schema-registry-register)

## Docker images
### Hortonworks Schema-registry

Using alternative Docker image. For more details : 

* https://hub.docker.com/r/thebookpeople/hortonworks-registry/

### Confluent Schema-registry & Landoop UI

It serves the schema-registry-ui from port 8000.
A live version can be found at [Landoop Demo of schema-registry-ui](https://schema-registry-ui.landoop.com)

* https://hub.docker.com/r/confluentinc/cp-schema-registry/
* https://hub.docker.com/r/landoop/schema-registry-ui/

Attention this require at least Docker Compose 2.1 to allow Docker images start with order & validation.

### InfluxDB & Grafana

Using official Docker image. Look at this link for more details on the port : 

* Docker InfluxDB : https://hub.docker.com/_/influxdb/
* Docker Grafana : https://hub.docker.com/r/grafana/grafana/

