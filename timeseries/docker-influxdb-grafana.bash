echo "== Type 'inst_script' to install bash-fwk locally =="

inst_script() {
	echo "Install bash-fwk that contains Docker & Vagrant utilities."
	echo "$(curl -fsSL https://raw.githubusercontent.com/frtu/bash-fwk/master/autoinstaller4curl.bash)" | bash
}

echo "== Type 'influxdb_start' to start InfluxDB & Grafana =="

influxdb_start() {
	echo "Make sure you have ** docker-compose ** installed !!"
	(cd docker/influxdb && exec docker-compose up)
	dckmport 8086
	dckmport 3000
}
