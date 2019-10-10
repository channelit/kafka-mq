### Kafka - MQ

#### Steps:
##### Build the app:
```shell script
docker build . -t kafka-mq
docker tag kafka-mq kafka-mq:v1
```

##### Build volume image with configuration (to work without physical mount):
```shell script
docker build . -t conf -f Dockerfile-config
docker tag conf conf:v1
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

#### URLs for Confluent Kakfa Setup:
- MQ Admin: http://localhost:9991/
- Kafka UI: http://localhost:3030
- Service:
    - Post Messages: http://localhost:8080/mq/send?numMessage=100
    - MQ Consumer: http://localhost:8080/mq/recv
    - Kafka Receiver: http://localhost:8080/kafka/recv
    
#### URLs for Apache Kakfa:
- MQ Admin: https://localhost:3443/
- Kafka UI: http://localhost:9991
- Service:
    - Post Messages: http://localhost:6081/mq/send?numMessage=100
    - MQ Consumer: http://localhost:6081/mq/recv
    - Kafka Consumer Receiver: http://localhost:7081/kafka/consumer/recv
    - Kafka Stream Receiver: http://localhost:7081/kafka/stream/recv
    
#### URLs for Confluent docker-compose (under /confluent, copied from Confluent Docker):
- UI: http://localhost:9021/
