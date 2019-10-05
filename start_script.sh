#!/bin/bash

# Post messsages to MQ
curl http://localhost:6081/mq/send?numMessage=100

# Consume messages from MQ and post to Kafka (2 instances)
curl http://localhost:6081/mq/recv
curl http://localhost:6082/mq/recv