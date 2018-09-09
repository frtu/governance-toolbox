export CONFLUENT_KAFKA_INSTANCE="confluentincschemaregistry_broker_1"
export CONFLUENT_ZK_INSTANCE="confluentincschemaregistry_zookeeper_1"

export DCK_SCRIPT=~/scr-local/env-docker-kafka.bash
if [ -f "${DCK_SCRIPT}" ]; then
  source $DCK_SCRIPT
fi

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

  	echo "export DCK_KAFKA_INSTANCE=${CONFLUENT_KAFKA_INSTANCE}" > $DCK_SCRIPT
  	echo "export DCK_ZK_INSTANCE=${CONFLUENT_ZK_INSTANCE}" >> $DCK_SCRIPT
	(cd docker/confluentinc-schema-registry && exec docker-compose up)
	dckmport 8000
	dckmport 8081
}

echo "== Type 'shckafka' to start Confluence Kafka bash =="
shckafka() {
	if [ -f "${DCK_SCRIPT}" ]; then
	  source $DCK_SCRIPT
	fi
	echo "dckbash $DCK_KAFKA_INSTANCE $@"
    dckbash $DCK_KAFKA_INSTANCE "$@"
}
echo "== Type 'shczk' to start Confluence Zookeeper bash =="
shczk() {
	if [ -f "${DCK_SCRIPT}" ]; then
	  source $DCK_SCRIPT
	fi
	echo "dckbash ${DCK_ZK_INSTANCE} $@"
	dckbash ${DCK_ZK_INSTANCE} "$@"
}
dckbash() {
  usage $# "IMAGE_NAME" "[COMMANDS]"
  ## Display Usage and exit if insufficient parameters. Parameters prefix with [ are OPTIONAL.
  if [[ "$?" -ne 0 ]]; then 
    echo "If you don't know any names run 'dckps' and look at the last column NAMES" >&2
    dckls
    return -1
  fi

  local IMAGE_NAME=$1

  # run in LOGIN MODE : https://github.com/rbenv/rbenv/wiki/Unix-shell-initialization#bash
  if [ -z "$2" ]
    then
      echo "Login into a Bash docker images : $IMAGE_NAME"
      docker exec -it $IMAGE_NAME bash -l
    else
      local COMMANDS=${@:2}
      echo "CALL : root@$IMAGE_NAME> ${COMMANDS}"
      echo "${COMMANDS}" | docker exec -i $IMAGE_NAME bash -l
  fi
}