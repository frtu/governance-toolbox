package com.github.frtu.schemaregistries.hortonworks;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.frtu.schemaregistries.SchemaTypePublisher;
import com.github.frtu.simple.scan.DirectoryScanner;
import com.hortonworks.registries.schemaregistry.SchemaCompatibility;
import com.hortonworks.registries.schemaregistry.SchemaIdVersion;
import com.hortonworks.registries.schemaregistry.SchemaMetadata;
import com.hortonworks.registries.schemaregistry.SchemaVersion;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.errors.IncompatibleSchemaException;
import com.hortonworks.registries.schemaregistry.errors.InvalidSchemaException;
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;

/**
 * Publisher for Avro schema Files & Folders
 * 
 * @author fred
 */
public class AvroSchemaPublisher implements SchemaTypePublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(AvroSchemaPublisher.class);

	private SchemaRegistryClient schemaRegistryClient;

	public AvroSchemaPublisher(SchemaRegistryClient schemaRegistryClient) {
		super();
		this.schemaRegistryClient = schemaRegistryClient;
	}

	@Override
	public String getSchemaType() {
		return AvroSchemaProvider.TYPE;
	}

	@Override
	public void publishSchemaFolder(File schemaPath) {
		DirectoryScanner directoryScanner = new DirectoryScanner(file -> {
			publishSchema(file);
		});
		// Only Avro schema
		directoryScanner.setFileExtensionToFilter("avsc");

		LOGGER.info("Scanning directory {}", schemaPath);
		directoryScanner.scanDirectory(schemaPath);
	}

	@Override
	public void publishSchema(File file) {
		try {
			Schema schema = new Schema.Parser().parse(file);
			publishSchema(schema);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public void publishSchema(Schema schema) {
		String doc = schema.getDoc();
		if (!StringUtils.isEmpty(doc)) {
			publishSchema(schema, doc);
		}
		else {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
			publishSchema(schema, schema.getFullName());
		}
	}

	public void publishSchema(Schema schema, String versionDescription) {
		String doc = schema.getDoc();
		if (!StringUtils.isEmpty(doc)) {
			publishSchema(schema, versionDescription, doc);
		}
		else {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
			publishSchema(schema, versionDescription, schema.getFullName());
		}
	}

	public void publishSchema(Schema schema, String versionDescription, String schemaDescription) {
		try {
			SchemaMetadata schemaMetadata = new SchemaMetadata.Builder(schema.getFullName()).type(getSchemaType())
					.schemaGroup(schema.getNamespace()).description(schemaDescription)
					.compatibility(SchemaCompatibility.BACKWARD).build();

			SchemaIdVersion version = schemaRegistryClient.addSchemaVersion(schemaMetadata,
					new SchemaVersion(schema.toString(), versionDescription));

			LOGGER.info("Registered schema metadata [{}] and returned version [{}]", schemaMetadata, version);
		} catch (InvalidSchemaException | IncompatibleSchemaException | SchemaNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
