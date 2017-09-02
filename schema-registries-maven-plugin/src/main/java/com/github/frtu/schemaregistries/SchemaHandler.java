package com.github.frtu.schemaregistries;

import java.io.File;

import com.hortonworks.registries.schemaregistry.SchemaVersionInfo;
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;

/**
 * Publish Schema from the format {@link #getSchemaType()}.
 * 
 * @author fred
 */
public interface SchemaHandler {
	/**
	 * Type of schema this publisher handles : avro, ...
	 * 
	 * @return
	 */
	String getSchemaType();

	/**
	 * Schema File extension matching this schema type {@link #getSchemaType()}.
	 * 
	 * @return
	 */
	String[] getSchemaFileExtensions();

	/**
	 * Publish the schema file of format {@link #getSchemaType()}.
	 * 
	 * @param schemaFile Path to the schema file
	 */
	void publishSchema(File schemaFile);

	/**
	 * Publish the schema file of format {@link #getSchemaType()} and some description of this particular version (can
	 * come from a build system).
	 * 
	 * @param schemaFile Path to the schema file
	 * @param versionDescription
	 */
	void publishSchema(File schemaFile, String versionDescription);

	String fetchSchema(String schemaFullName);
}
