package biz.cits.kafka.producer;

import biz.cits.message.MsgParser;
import kafka.common.KafkaException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Component
public class FifoProducer {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    @Autowired
    private KafkaProducer<String, String> producer;

    @Autowired
    private Properties producerProperties;

    public void sendMessage(String message) {
//        Producer<String, String> producer = new KafkaProducer<>(producerProperties, new StringSerializer(), new StringSerializer());
//        producer.initTransactions();
        AbstractMap.SimpleEntry<String, String> splitMsg = MsgParser.parse(message);
        try {
//            producer.beginTransaction();
            producer.send(
                    new ProducerRecord<>(KAFKA_TOPIC,
                            splitMsg.getKey(), splitMsg.getValue()));
//            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
//            producer.close();
        } catch (KafkaException e) {
//            producer.abortTransaction();
        }
//        producer.close();
    }
}