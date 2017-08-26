package com.github.frtu.schemaregistries;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hortonworks.registries.schemaregistry.SchemaCompatibility;
import com.hortonworks.registries.schemaregistry.SchemaIdVersion;
import com.hortonworks.registries.schemaregistry.SchemaMetadata;
import com.hortonworks.registries.schemaregistry.SchemaVersion;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.errors.IncompatibleSchemaException;
import com.hortonworks.registries.schemaregistry.errors.InvalidSchemaException;
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;

public class SchemaRegistryPublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaRegistryPublisher.class);

	private URL schemaRegistryUrl;
	private SchemaRegistryClient schemaRegistryClient;

	public SchemaRegistryPublisher(URL schemaRegistryUrl) {
		super();
		this.schemaRegistryUrl = schemaRegistryUrl;
	}

	public void initSchemaRegistry() throws IOException {
		URL url = new URL(schemaRegistryUrl, "/api/swagger/");

		HttpURLConnection connection = null;
		int code = -1;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			code = connection.getResponseCode();
			if (code == 200) {
				// ONLY PATH TO true
				init();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Ping server failed, please check URL or if server is up! URL=" + url + " HTTP code=" + code, e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void init() throws MalformedURLException {
		String apiUrl = new URL(schemaRegistryUrl, "/api/v1").toString();
		LOGGER.info("Using URL={} to create connection to Schema Registry", apiUrl);
		Map<String, Object> config = createConfig(apiUrl);
		schemaRegistryClient = new SchemaRegistryClient(config);
	}

	public void registerSchema(File file)
			throws IOException, InvalidSchemaException, IncompatibleSchemaException, SchemaNotFoundException {
		Schema schema = new Schema.Parser().parse(file);
		String doc = schema.getDoc();
		if (StringUtils.isEmpty(doc)) {
			LOGGER.warn("Attention, it's a best practice to add a 'doc' section to your avro schema");
		}
		
		registerSchema(schema);
	}

	public void registerSchema(Schema schema)
			throws InvalidSchemaException, IncompatibleSchemaException, SchemaNotFoundException {
		String versionDescription = schema.getDoc();

		registerSchema(schema, versionDescription);
	}

	public void registerSchema(Schema schema, String versionDescription)
			throws InvalidSchemaException, IncompatibleSchemaException, SchemaNotFoundException {
		String schemaDescription = schema.getDoc();

		registerSchema(schema, versionDescription, schemaDescription);
	}

	public void registerSchema(Schema schema, String versionDescription, String schemaDescription)
			throws InvalidSchemaException, IncompatibleSchemaException, SchemaNotFoundException {
		SchemaMetadata schemaMetadata = new SchemaMetadata.Builder(schema.getFullName()).type(AvroSchemaProvider.TYPE)
				.schemaGroup(schema.getNamespace()).description(schemaDescription)
				.compatibility(SchemaCompatibility.BACKWARD).build();

		SchemaIdVersion version = schemaRegistryClient.addSchemaVersion(schemaMetadata,
				new SchemaVersion(schema.toString(), versionDescription));

		LOGGER.info("Registered schema metadata [{}] and returned version [{}]", schemaMetadata, version);
	}

	public static Map<String, Object> createConfig(String schemaRegistryUrl) {
		Map<String, Object> config = new HashMap<>();
		config.put(SchemaRegistryClient.Configuration.SCHEMA_REGISTRY_URL.name(), schemaRegistryUrl);
		config.put(SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_SIZE.name(), 10L);
		config.put(SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_EXPIRY_INTERVAL_SECS.name(), 5000L);
		config.put(SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_SIZE.name(), 1000L);
		config.put(SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS.name(),
				60 * 60 * 1000L);
		return config;
	}

	public static void main(String[] args)
			throws InvalidSchemaException, IncompatibleSchemaException, SchemaNotFoundException, IOException {
		URL schemaRegistryUrl = new URL("http://localhost:9090");
		SchemaRegistryPublisher schemaRegistryAvroPublisher = new SchemaRegistryPublisher(schemaRegistryUrl);
		schemaRegistryAvroPublisher.initSchemaRegistry();

		// register
		File file = new File("src/test/resources/user.avsc");

		schemaRegistryAvroPublisher.registerSchema(file);
	}
}
