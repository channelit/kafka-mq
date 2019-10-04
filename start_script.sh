#!/bin/bash

# Post messsages to MQ
curl http://localhost:6081/mq/send?numMessage=100

# Consume messages from MQ and post to Kafka (2 instances)
curl http://localhost:6081/mq/recv
curl http://localhost:6082/mq/recv

# Consume messages from Kafka and store in db (3 instances)
curl http://localhost:7081/kafka/recv
curl http://localhost:7082/kafka/recv
curl http://localhost:7083/kafka/recv