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
	public String publishSchema(File schemaFile) {
		return this.publishSchema(schemaFile, null);
	}

	@Override
	public String publishSchema(File schemaFile, String versionDescription) {
		Schema schema;
		try {
			schema = new Schema.Parser().parse(schemaFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return publishSchema(schema, versionDescription);
	}
	
	/**
	 * Publish the schema metadata with the provided description of this particular version (can come from a build
	 * system).
	 * 
	 * @param schema The schema object
	 * @param versionDescription The description for this version
	 * @return schemaIdentifier The unique identifier for this schema into the registry
	 */
	public String publishSchema(Schema schema, String versionDescription) {
		String doc = schema.getDoc();
		if (StringUtils.isEmpty(doc)) {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
			doc = schema.getFullName();
		}

		if (!StringUtils.isEmpty(versionDescription)) {
			return publishSchema(schema, versionDescription, doc);
		} else {
			return publishSchema(schema, doc, doc);
		}
	}

	/**
	 * Publish the schema metadata with its description and some description of this particular version (can come from a
	 * build system).
	 * 
	 * @param schema The schema object
	 * @param versionDescription The description for this version
	 * @param schemaDescription The description for this schema
	 * @return schemaIdentifier The unique identifier for this schema into the registry
	 */
	public String publishSchema(Schema schema, String versionDescription, String schemaDescription) {
		String schemaIdentifier = schema.getFullName();
		SchemaCompatibility schemaCompatibility = SchemaCompatibility.BACKWARD;

		try {
			SchemaVersionInfo schemaVersionInfo = getSchema(schemaIdentifier);

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
			LOGGER.info("No previous version of schema='{}", schemaIdentifier);
		}

		try {
			SchemaMetadata schemaMetadata = new SchemaMetadata.Builder(schemaIdentifier).type(getSchemaType())
			        .schemaGroup(schema.getNamespace()).description(schemaDescription)
			        .compatibility(schemaCompatibility).build();

			SchemaIdVersion version = schemaRegistryClient.addSchemaVersion(schemaMetadata,
			        new SchemaVersion(schema.toString(), versionDescription));

			LOGGER.info("Registered schema metadata [{}] and returned version [{}] for schemaIdentifier [{}]",
			        schemaMetadata, version, schemaIdentifier);
			return schemaIdentifier;
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

	@Override
	public String fetchSchema(String schemaIdentifier) {
		try {
			SchemaVersionInfo schemaVersionInfo = getSchema(schemaIdentifier);
			String schemaText = schemaVersionInfo.getSchemaText();
			return schemaText;
		} catch (SchemaNotFoundException e) {
			throw new IllegalArgumentException("No schema found for schemaFullName=" + schemaIdentifier, e);
		}
	}

	private SchemaVersionInfo getSchema(String schemaIdentifier) throws SchemaNotFoundException {
		SchemaVersionInfo schemaVersionInfo = schemaRegistryClient.getLatestSchemaVersionInfo(schemaIdentifier);

		LOGGER.info("Fetched schema using schemaIdentifier={} version={} timestamp={} description={}", schemaIdentifier,
		        schemaVersionInfo.getVersion(), schemaVersionInfo.getTimestamp(), schemaVersionInfo.getDescription());
		LOGGER.debug("SchemaText={}", schemaVersionInfo.getSchemaText());
		return schemaVersionInfo;
	}
}
