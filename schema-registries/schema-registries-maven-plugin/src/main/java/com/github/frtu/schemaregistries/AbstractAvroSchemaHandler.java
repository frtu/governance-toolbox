package com.github.frtu.schemaregistries;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;

/**
 * Publisher for Avro schema Files &amp; Folders
 * 
 * @author fred
 * @since 0.3.0
 */
public abstract class AbstractAvroSchemaHandler implements SchemaHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAvroSchemaHandler.class);
	
	@Override
	public String getSchemaType() {
		return AvroSchemaProvider.TYPE;
	}

	@Override
	public String[] getSchemaFileExtensions() {
		return new String[] { "avsc" };
	}

	@Override
	public String registerSchema(File schemaFile) {
		return this.registerSchema(schemaFile, null);
	}

	@Override
	public String registerSchema(File schemaFile, String versionDescription) {
		Schema schema;
		try {
			schema = new Schema.Parser().parse(schemaFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return registerSchema(schema, versionDescription);
	}

	/**
	 * Register the schema metadata with the provided description of this particular version (can come from a build
	 * system).
	 * 
	 * @param schema The schema object
	 * @param versionDescription The description for this version
	 * @return schemaIdentifier The unique identifier for this schema into the registry
	 */
	public String registerSchema(Schema schema, String versionDescription) {
		String doc = schema.getDoc();
		if (StringUtils.isEmpty(doc)) {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
			doc = schema.getFullName();
		}
	
		if (!StringUtils.isEmpty(versionDescription)) {
			return registerSchema(schema, versionDescription, doc);
		} else {
			return registerSchema(schema, doc, doc);
		}
	}

	protected abstract String registerSchema(Schema schema, String versionDescription, String doc);
}