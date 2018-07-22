# Time series DataBase

TimeSeries DB are database organized by the mandatory dimension of time.
Every INSERT or SELECT are organized by time index that allow exact or range queries.

Highly recommend this book to know more about the internals of TSDBs :
http://shop.oreilly.com/product/0636920035435.do


## Starting up InfluxDB & Grafana Docker

You can use create shortcuts and port mapping utils by loading :
```
. docker-influxdb-grafana.bash
```

Just launch ```influxdb_start``` to start docker-compose with the correct settlements.

### Initialization

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

### Configuration

When configuring Grafana in DataSource, set 

* Type : ```InfluxDB```
* URL : ```http://influxdb:8086```
* Database name : [YOUR DB NAME] or ```_internal``` by default

## Jupyter

You can find some python scripts for Jupyter at the subfolder /jupyter

[Visualize with Jupyter - NbViewer](https://nbviewer.jupyter.org/github/frtu/governance-toolbox/blob/master/timeseries/jupyter/Crawling%20%26%20Store%20toolbox.ipynb)
