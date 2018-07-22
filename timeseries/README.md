# timeseries

You can use create shortcuts and port mapping utils by loading :
```
. docker-influxdb-grafana.bash
```

Just launch ```influxdb_start``` to start docker-compose with the correct settlements.

## Initialization

Log into the influxdb docker instance using :

```
> dckps : find influxdb in all the docker instance running
mine is [influxdb_influxdb_1]

> dckbash [instance like influxdb_influxdb_1]
>root@influxdb> influx : to have influx cmd line inside the docker instance
```

In InfluxDB : 

* Database = Database : ```SHOW DATABASES```
* Measurement = Table : ```SHOW MEASUREMENTS```

Create a database with :

```
CREATE DATABASE [YOUR DB NAME]
SHOW DATABASES
```

List tables with :

```
USE [YOUR DB NAME]
SHOW MEASUREMENTS
SELECT * FROM [MEASUREMENT]
```

## Configuration

When configuring Grafana in DataSource, set 

* Type : ```InfluxDB```
* URL : ```http://influxdb:8086```
* Database name : [YOUR DB NAME] or ```_internal``` by default
