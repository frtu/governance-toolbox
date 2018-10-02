export CONFLUENT_KAFKA_INSTANCE="confluentincschemaregistry_broker_1"
export CONFLUENT_ZK_INSTANCE="confluentincschemaregistry_zookeeper_1"
export CONFLUENT_KAFKA_REST_INSTANCE=confluentincschemaregistry_kafka_rest_1

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
	if [ -n "$SERVICE_SCR_dckkafka" ]; then
		export DCK_SCRIPT=$SERVICE_SCR_dckkafka
	fi

	echo "Make sure you have ** docker-compose ** installed using 2.1 that allow ordering!!"
	echo "=== Topic management ==="
	echo "- http://127.0.0.1:8082/topics for Kafka REST"
	echo "- http://127.0.0.1:8002 for Kafka Topic UI"
	echo "=== Schema management ==="
	echo "- http://127.0.0.1:8081/subjects/[NAME]/versions Schema Registry APIs"
	echo "- http://127.0.0.1:8001 for Schema Registry UI"
	echo "========================="

	echo "export DCK_INSTANCE_NAME_KAFKA=${CONFLUENT_KAFKA_INSTANCE}" >> $DCK_SCRIPT
	echo "export DCK_INSTANCE_NAME_ZK=${CONFLUENT_ZK_INSTANCE}" >> $DCK_SCRIPT
	(cd docker/confluentinc-schema-registry && exec docker-compose up)
	dckmport 8081
	dckmport 8082
	dckmport 8001
	dckmport 8002
}

echo "== Type 'shcrest' to start Confluence Kafka REST bash =="
shcrest() {
	if [ -f "${DCK_SCRIPT}" ]; then
	  source $DCK_SCRIPT
	fi
	echo "dckbash $CONFLUENT_KAFKA_REST_INSTANCE $@"
    dckbash $CONFLUENT_KAFKA_REST_INSTANCE "$@"
}
echo "== Type 'shcrestconfig' to read config for Confluence Kafka REST =="
shcrestconfig() {
	# https://github.com/Landoop/kafka-topics-ui#common-issues
	echo "=== /etc/kafka-rest/kafka-rest.properties ==="
	shcrest "cat /etc/kafka-rest/kafka-rest.properties"
}

echo "== Type 'shckafka' to start Confluence Kafka bash =="
shckafka() {
	if [ -f "${DCK_SCRIPT}" ]; then
	  source $DCK_SCRIPT
	fi
	echo "dckbash $DCK_INSTANCE_NAME_KAFKA $@"
    dckbash $DCK_INSTANCE_NAME_KAFKA "$@"
}
echo "== Type 'shckafkaconfig' to read config for Confluence Kafka =="
shckafkaconfig() {
	echo "=== /etc/kafka/kafka.properties ==="
	shckafka "cat /etc/kafka/kafka.properties"

	echo "=== /etc/kafka/server.properties ==="
	shckafka "cat /etc/kafka/server.properties"
}

echo "== Type 'shczk' to start Confluence Zookeeper bash =="
shczk() {
	if [ -f "${DCK_SCRIPT}" ]; then
	  source $DCK_SCRIPT
	fi
	echo "dckbash ${DCK_INSTANCE_NAME_ZK} $@"
	dckbash ${DCK_INSTANCE_NAME_ZK} "$@"
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