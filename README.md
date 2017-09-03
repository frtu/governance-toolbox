# governance-toolbox
A toolbox for automation & docker images around metadata governance :

Static metdata

* Schema registry : to store schema & versions

Dynamic metadata

* InfluxDB : TSDB to store PiT (Point In Time) based metadata.

## Docker images
### Hortonworks Schema-registry

Using alternative Docker image. For more details : 

* https://hub.docker.com/r/thebookpeople/hortonworks-registry/

### InfluxDB & Grafana

Using official Docker image. Look at this link for more details on the port : 

* Docker InfluxDB : https://hub.docker.com/_/influxdb/
* Docker Grafana : https://hub.docker.com/r/grafana/grafana/