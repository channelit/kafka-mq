### Kafka - MQ 

#### Steps:
##### Startup:
```shell script
docker -compose up --no-start
docker-compose up -d
```
##### Cleanup:
```shell script
docker-compose kill
docker-compose rm
rm -rf data
```

#### URLs:
- MQ Admin: https://localhost:9443
- Kafka UI: http://localhost:3030

