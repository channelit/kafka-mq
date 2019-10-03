### Kafka - MQ 

#### Steps:
##### Build the app:
```shell script
docker build . -t kafka-mq
docker tag kafka-mq kafka-mq:v1
```

##### Startup the environment:
```shell script
docker -compose up -d zoo1 zoo2 zoo3
docker-compose up -d
```
- Create topic FIFO_TOPIC with replication=2, partitions=3 using UI


##### Cleanup:
```shell script
docker-compose kill
docker-compose rm
rm -rf data
```

#### URLs:
- MQ Admin: http://localhost:9991/
- Kafka UI: http://localhost:3030
- Service:
    - Post Messages: http://localhost:8080/mq/send?numMessage=100
    - MQ Consumer: http://localhost:8080/mq/recv
    - Kafka Receiver: http://localhost:8080/kafka/recv

#### URLs for Confluent docker-compose (under /confluent, copied from Confluent Docker):
- UI: http://localhost:9021/
