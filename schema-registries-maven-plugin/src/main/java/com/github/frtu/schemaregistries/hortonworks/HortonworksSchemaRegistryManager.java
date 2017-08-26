package com.github.frtu.schemaregistries.hortonworks;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.frtu.schemaregistries.SchemaTypePublisher;
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;

/**
 * Manage Connection to Hortonworks Schema Registry
 * 
 * @author fred
 */
public class HortonworksSchemaRegistryManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(HortonworksSchemaRegistryManager.class);

	private URL schemaRegistryBaseUrl;

	private SchemaRegistryClient schemaRegistryClient;

	private Map<String, SchemaTypePublisher> publisherMap = new HashMap<>();

	public HortonworksSchemaRegistryManager(URL schemaRegistryUrl) {
		super();
		this.schemaRegistryBaseUrl = schemaRegistryUrl;
	}

	public void initSchemaRegistry() {
		HttpURLConnection connection = null;
		int code = -1;
		try {
			URL url = new URL(schemaRegistryBaseUrl, "/api/swagger/");

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			LOGGER.debug("Connecting using GET {}", url);
			connection.connect();

			code = connection.getResponseCode();
			LOGGER.debug("HTTP code:{} for GET {}", code, url);
			if (code == 200) {
				// ONLY PATH TO true
				this.schemaRegistryClient = createSchemaRegistryClient(schemaRegistryBaseUrl);
				populatePublisherMap();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Ping server failed, please check URL or if server is up! schemaRegistryBaseUrl="
							+ schemaRegistryBaseUrl + " HTTP code=" + code,
					e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static SchemaRegistryClient createSchemaRegistryClient(URL schemaRegistryBaseUrl)
			throws MalformedURLException {
		String apiUrl = new URL(schemaRegistryBaseUrl, "/api/v1").toString();
		LOGGER.info("Using URL={} to create connection to Schema Registry !", apiUrl);

		Map<String, Object> config = createConfig(apiUrl);
		return new SchemaRegistryClient(config);
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

	private void populatePublisherMap() {
		// TODO Can use reflection to instantiate all SchemaTypePublisher when
		// many will come for Protobuf or ...
		SchemaTypePublisher schemaTypePublisher = new AvroSchemaPublisher(schemaRegistryClient);

		publisherMap.put(schemaTypePublisher.getSchemaType(), schemaTypePublisher);
	}

	public SchemaTypePublisher getSchemaTypePublisher(String schemaType) {
		return publisherMap.get(schemaType);
	}

	public static void main(String[] args) throws MalformedURLException {
		URL schemaRegistryUrl = new URL("http://localhost:9090");

		HortonworksSchemaRegistryManager schemaRegistryPublisher = new HortonworksSchemaRegistryManager(schemaRegistryUrl);
		schemaRegistryPublisher.initSchemaRegistry();

		schemaRegistryPublisher.getSchemaTypePublisher(AvroSchemaProvider.TYPE)
				.publishSchema(new File("src/test/resources/user.avsc"));
	}
}
