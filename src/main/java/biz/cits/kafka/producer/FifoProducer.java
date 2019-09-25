package biz.cits.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class FifoProducer implements ApplicationRunner {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    @Autowired
    private KafkaProducer<Integer, String> producer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int messageNo = 1;
        while (true) {
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();

            try {
                producer.send(new ProducerRecord<>(KAFKA_TOPIC,
                        messageNo,
                        messageStr)).get();
                System.out.printf("Sent message: (%d, %s)%n", messageNo, messageStr);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            messageNo++;
        }
    }

}