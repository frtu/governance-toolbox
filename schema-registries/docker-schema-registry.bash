echo "== Type 'inst_script' to install bash-fwk locally =="

inst_script() {
	echo "Install bash-fwk that contains Docker & Vagrant utilities."
	echo "$(curl -fsSL https://raw.githubusercontent.com/frtu/bash-fwk/master/autoinstaller4curl.bash)" | bash
}

# MANUAL Docker build : https://github.com/hortonworks/registry/blob/master/docker/images/registry/Dockerfile
#
# or Leverage thebookpeople/hortonworks-registry to avoid building manually. Thanks to TheBookPeople.
# https://github.com/TheBookPeople/hortonworks-registry-docker
# https://hub.docker.com/r/thebookpeople/hortonworks-registry/

echo "== Type 'schemaregistry_hortonworks_start' to start Hortonworks Schema Registry =="
schemaregistry_hortonworks_start() {
	echo "Make sure you have ** docker-compose ** installed !!"
	(cd docker/hortonworks-schema-registry && exec docker-compose up)
	dckmport 9090
}

echo "== Type 'schemaregistry_confluentinc_start' to start Confluence Schema Registry =="
schemaregistry_confluentinc_start() {
	echo "Make sure you have ** docker-compose ** installed using 2.1 that allow ordering!!"
	echo "- http://127.0.0.1:8000 for Schema Registry UI"
	echo "- http://127.0.0.1:8081/subjects/[NAME]/versions Schema Registry APIs"
	(cd docker/confluentinc-schema-registry && exec docker-compose up)
	dckmport 8000
	dckmport 8081
}
