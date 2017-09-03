package com.github.frtu.schemaregistries;

import java.io.File;

/**
 * Publish Schema from the format {@link #getSchemaType()}.
 * 
 * @author fred
 */
public interface SchemaHandler {
	/**
	 * Return the type of schema this publisher handles.
	 * 
	 * @return avro, ...
	 */
	String getSchemaType();

	/**
	 * Return all the schema file extension matching this schema type {@link #getSchemaType()}.
	 * 
	 * @return .avsc, .proto, ...
	 */
	String[] getSchemaFileExtensions();

	/**
	 * Fetch the schema text from Schema Registry. Throws IllegalArgumentException when schema not found.
	 * 
	 * @param schemaIdentifier The unique identifier for this schema into the registry
	 * @return schema payload from the handled type.
	 */
	String fetchSchema(String schemaIdentifier);

	/**
	 * Publish the schema file of format {@link #getSchemaType()}. Return the schemaIdentifier that you can use to
	 * {@link #fetchSchema(String)}
	 * 
	 * @param schemaFile Path to the schema file
	 * @return schemaIdentifier The unique identifier for this schema into the registry
	 */
	String publishSchema(File schemaFile);

	/**
	 * Publish the schema file of format {@link #getSchemaType()} and some description of this particular version (can
	 * come from a build system). Return the schemaIdentifier that you can use to {@link #fetchSchema(String)}
	 * 
	 * @param schemaFile Path to the schema file
	 * @param versionDescription The description for this version
	 * @return schemaIdentifier The unique identifier for this schema into the registry
	 */
	String publishSchema(File schemaFile, String versionDescription);
}
