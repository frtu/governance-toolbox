package com.github.frtu.schemaregistries.hortonworks;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.frtu.schemaregistries.SchemaHandler;
import com.hortonworks.registries.schemaregistry.SchemaCompatibility;
import com.hortonworks.registries.schemaregistry.SchemaIdVersion;
import com.hortonworks.registries.schemaregistry.SchemaMetadata;
import com.hortonworks.registries.schemaregistry.SchemaVersion;
import com.hortonworks.registries.schemaregistry.SchemaVersionInfo;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaValidator;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaValidator.SchemaCompatibilityResult;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaValidator.SchemaIncompatibilityType;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaValidator.SchemaPairCompatibility;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.errors.IncompatibleSchemaException;
import com.hortonworks.registries.schemaregistry.errors.InvalidSchemaException;
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;

/**
 * Publisher for Avro schema Files &amp; Folders
 * 
 * @author fred
 */
public class AvroSchemaHandler implements SchemaHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AvroSchemaHandler.class);

	private SchemaRegistryClient schemaRegistryClient;

	public AvroSchemaHandler(SchemaRegistryClient schemaRegistryClient) {
		super();
		this.schemaRegistryClient = schemaRegistryClient;
	}

	@Override
	public String getSchemaType() {
		return AvroSchemaProvider.TYPE;
	}

	@Override
	public String[] getSchemaFileExtensions() {
		return new String[] { "avsc" };
	}

	@Override
	public void publishSchema(File schemaFile) {
		this.publishSchema(schemaFile, null);
	}

	@Override
	public void publishSchema(File schemaFile, String versionDescription) {
		Schema schema;
		try {
			schema = new Schema.Parser().parse(schemaFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		publishSchema(schema, versionDescription);
	}

	/**
	 * Publish the schema metadata with the provided description of this particular version (can come from a build
	 * system).
	 * 
	 * @param schema
	 * @param versionDescription
	 */
	public void publishSchema(Schema schema, String versionDescription) {
		String doc = schema.getDoc();
		if (StringUtils.isEmpty(doc)) {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
			doc = schema.getFullName();
		}

		if (!StringUtils.isEmpty(versionDescription)) {
			publishSchema(schema, versionDescription, doc);
		} else {
			publishSchema(schema, doc, doc);
		}
	}

	/**
	 * Publish the schema metadata with its description and some description of this particular version (can come from a
	 * build system).
	 * 
	 * @param schema
	 * @param versionDescription
	 * @param schemaDescription
	 */
	public void publishSchema(Schema schema, String versionDescription, String schemaDescription) {
		String schemaFullName = schema.getFullName();
		SchemaCompatibility schemaCompatibility = SchemaCompatibility.BACKWARD;

		try {
			SchemaVersionInfo schemaVersionInfo = getSchema(schemaFullName);

			Schema previousSchema = new Schema.Parser().parse(schemaVersionInfo.getSchemaText());
			try {
				schemaCompatibility = checkCompability(schema, previousSchema);
			} catch (IllegalArgumentException e) {
				String errorMessage = String.format(
				        "New Schema (schemaDescription=%s & versionDescription=%s) is INCOMPATIBLE with Old Schema schemaVersionInfo=%s. => For how to fix it sees the inner Exception message below this stack.",
				        schemaDescription, versionDescription, schemaVersionInfo.getDescription());
				throw new IllegalArgumentException(errorMessage, e);
			}
		} catch (SchemaNotFoundException | javax.ws.rs.NotFoundException e) {
			LOGGER.info("No previous version of schema='{}", schemaFullName);
		}

		try {
			SchemaMetadata schemaMetadata = new SchemaMetadata.Builder(schemaFullName).type(getSchemaType())
			        .schemaGroup(schema.getNamespace()).description(schemaDescription)
			        .compatibility(schemaCompatibility).build();

			SchemaIdVersion version = schemaRegistryClient.addSchemaVersion(schemaMetadata,
			        new SchemaVersion(schema.toString(), versionDescription));

			LOGGER.info("Registered schema metadata [{}] and returned version [{}]", schemaMetadata, version);
		} catch (InvalidSchemaException | IncompatibleSchemaException | SchemaNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public SchemaCompatibility checkCompability(Schema newSchema, Schema existingSchema) {
		SchemaPairCompatibility compatibility = AvroSchemaValidator.checkReaderWriterCompatibility(newSchema,
		        existingSchema);

		SchemaCompatibilityResult schemaCompatibilityResult = compatibility.getResult();
		SchemaIncompatibilityType schemaIncompatibilityType = schemaCompatibilityResult.getIncompatibility();

		if (schemaIncompatibilityType != null) {
			throw new IllegalArgumentException(schemaCompatibilityResult.getMessage());
		}
		return SchemaCompatibility.BACKWARD;
	}

	/**
	 * Fetch the schema text from Schema Registry. Throws IllegalArgumentException when schema not found.
	 * 
	 * @param schemaFullName
	 * @return
	 */
	@Override
	public String fetchSchema(String schemaFullName) {
		try {
			SchemaVersionInfo schemaVersionInfo = getSchema(schemaFullName);
			String schemaText = schemaVersionInfo.getSchemaText();
			return schemaText;
		} catch (SchemaNotFoundException e) {
			throw new IllegalArgumentException("No schema found for schemaFullName=" + schemaFullName, e);
		}
	}

	private SchemaVersionInfo getSchema(String schemaFullName) throws SchemaNotFoundException {
		SchemaVersionInfo schemaVersionInfo = schemaRegistryClient.getLatestSchemaVersionInfo(schemaFullName);

		LOGGER.info("Fetched schema using schemaFullName={} version={} timestamp={} description={}", schemaFullName,
		        schemaVersionInfo.getVersion(), schemaVersionInfo.getTimestamp(), schemaVersionInfo.getDescription());
		LOGGER.debug("SchemaText={}", schemaVersionInfo.getSchemaText());
		return schemaVersionInfo;
	}
}
