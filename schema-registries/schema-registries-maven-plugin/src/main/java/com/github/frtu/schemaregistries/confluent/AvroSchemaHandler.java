package com.github.frtu.schemaregistries.confluent;

import java.io.IOException;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.frtu.schemaregistries.AbstractAvroSchemaHandler;
import com.github.frtu.schemaregistries.SchemaHandler;

import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

/**
 * Publisher for Confluent Avro schema Files &amp; Folders
 * 
 * @author fred
 */
public class AvroSchemaHandler extends AbstractAvroSchemaHandler implements SchemaHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AvroSchemaHandler.class);

	private SchemaRegistryClient schemaRegistryClient;

	public AvroSchemaHandler(SchemaRegistryClient schemaRegistryClient) {
		super();
		this.schemaRegistryClient = schemaRegistryClient;
	}

	@Override
	public String downloadSchema(String schemaIdentifier) {
		try {
			SchemaMetadata schemaMetadata = schemaRegistryClient.getLatestSchemaMetadata(schemaIdentifier);
			return schemaMetadata.getSchema();
		} catch (IOException | RestClientException e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalArgumentException("Error with schemaFullName=" + schemaIdentifier, e);
		}
	}

	@Override
	protected String registerSchema(Schema schema, String versionDescription, String doc) {
		try {
			int register = schemaRegistryClient.register(schema.getFullName(), schema);
			return String.valueOf(register);
		} catch (IOException | RestClientException e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalArgumentException("Error with schemaFullName=" + schema.getFullName(), e);
		}
	}
}
