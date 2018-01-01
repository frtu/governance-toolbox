package com.github.frtu.schemaregistries.confluent;

import java.io.IOException;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

public class ConfluentSchemaRegistryManager {

	public static void main(String[] args) throws IOException, RestClientException {
	    CachedSchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient("http://localhost:8081", 20);
	    AvroSchemaHandler avroSchemaHandler = new AvroSchemaHandler(schemaRegistryClient);
	    
	    schemaRegistryClient.getAllSubjects().forEach(System.out::println);
	    
	    SchemaMetadata latestSchemaMetadata = schemaRegistryClient.getLatestSchemaMetadata("toto");
	    System.out.println(latestSchemaMetadata.getSchema());
	}
}
