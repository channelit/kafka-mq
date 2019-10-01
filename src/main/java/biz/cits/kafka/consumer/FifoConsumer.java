package biz.cits.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class FifoConsumer {

    private Consumer consumer;

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    @Autowired
    public FifoConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void start(){
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
            // commits the offset of record to broker.
            consumer.commitAsync();
        }
    }

}
