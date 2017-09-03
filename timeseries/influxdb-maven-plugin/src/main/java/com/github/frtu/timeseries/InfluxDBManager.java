package com.github.frtu.timeseries;

import java.io.Closeable;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class InfluxDBManager implements Closeable {
	private URL influxDbURL;
	
	private String login;

	private String password;

	private String database;

	private InfluxDB influxDB;
	
	public InfluxDBManager(URL influxDbURL, String login, String password, String database) {
		super();
		this.influxDbURL = influxDbURL;
		this.login = login;
		this.password = password;
		this.database = database;
	}
	
	public void connect() {
        influxDB = InfluxDBFactory.connect(influxDbURL.toString(), login, password);
        influxDB.enableBatch(10, 50, TimeUnit.SECONDS);
	}
	
	public boolean isDatabaseExists() {
      boolean checkDbExistence = influxDB.databaseExists(database);
//      if (!checkDbExistence) {
//          influxDB.createDatabase(database);
//      }
      return checkDbExistence;
	}
	
	public void close() {
        influxDB.disableBatch();
        influxDB.close();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
