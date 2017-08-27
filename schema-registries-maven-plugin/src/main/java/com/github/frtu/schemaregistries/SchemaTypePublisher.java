package com.github.frtu.schemaregistries;

import java.io.File;

/**
 * Publish Schema from the format {@link #getSchemaType()}.
 * 
 * @author fred
 */
public interface SchemaTypePublisher {
	/**
	 * Type of schema this publisher handles : avro, ...
	 * 
	 * @return
	 */
	String getSchemaType();

	/**
	 * Publish the schema file of format {@link #getSchemaType()}.
	 * 
	 * @param schemaFile Path to the schema file
	 */
	void publishSchema(File schemaFile);

	/**
	 * Scan the whole folder for matching schema file extension of format
	 * {@link #getSchemaType()}.
	 * 
	 * @param schemaPath Path to the schema folder
	 */
	void publishSchemaFolder(File schemaPath);
}
