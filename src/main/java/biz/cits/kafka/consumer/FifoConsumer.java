package biz.cits.kafka.consumer;

import biz.cits.db.DataStore;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@Component
public class FifoConsumer {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    private final DataStore dataStore;

    private Properties consumerProperties;

    private final KafkaConsumer consumer;

    private final KafkaProducer producer;

    @Value("${kafka.consumer.group}")
    private String KAFKA_CONSUMER_GROUP;

    @Autowired
    public FifoConsumer(Properties consumerProperties, DataStore dataStore, KafkaConsumer consumer, KafkaProducer producer) {
        this.consumerProperties = consumerProperties;
        this.dataStore = dataStore;
        this.consumer = consumer;
        this.producer = producer;
    }

    public void start() {
        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1000));
            consumerRecords.forEach(record -> {
                HashMap<String, String> records = new HashMap<>();
                records.put(record.key(), record.value());
                dataStore.storeData(record.key(), records);
                System.out.println("Record Key " + record.key());
                System.out.println("Record value " + record.value());
                System.out.println("Record partition " + record.partition());
                System.out.println("Record offset " + record.offset());
            });
            consumer.commitSync();
        }
    }

}
