package biz.cits.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Component
public class FifoConsumer {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    private Properties consumerProperties;

    public FifoConsumer(Properties consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    public void start() {
        Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        int noMessageFound = 0;
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1000));
            if (consumerRecords.count() == 0) {
                noMessageFound++;
                if (noMessageFound > 1)
                    break;
                else
                    continue;
            }
            consumerRecords.forEach(record -> {
                System.out.println("Record Key " + record.key());
                System.out.println("Record value " + record.value());
                System.out.println("Record partition " + record.partition());
                System.out.println("Record offset " + record.offset());
            });
            consumer.commitAsync();
        }
    }

}