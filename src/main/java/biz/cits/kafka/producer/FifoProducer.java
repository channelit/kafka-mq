package biz.cits.kafka.producer;

import biz.cits.db.DataStore;
import biz.cits.message.MsgParser;
import kafka.common.KafkaException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class FifoProducer {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    private final KafkaProducer producer;

    private final KafkaConsumer consumer;

    private final DataStore dataStore;

    @Value("${kafka.consumer.group}")
    private String KAFKA_CONSUMER_GROUP;

    @Autowired
    public FifoProducer(KafkaProducer producer, KafkaConsumer consumer, DataStore dataStore) {
        this.producer = producer;
        this.consumer = consumer;
        this.dataStore = dataStore;
        this.producer.initTransactions();
    }

    private void sendMessageEos(String message) {
        this.consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        AbstractMap.SimpleEntry<String, String> splitMsg = MsgParser.parse(message);
        producer.initTransactions();
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(200));
            consumerRecords.forEach(record -> {
                HashMap<String, String> records = new HashMap<>();
                records.put(record.key(), record.value());
                dataStore.storeData(record.key(), records);
                System.out.println("Record Key " + record.key());
                System.out.println("Record value " + record.value());
                System.out.println("Record partition " + record.partition());
                System.out.println("Record offset " + record.offset());
            });
            producer.beginTransaction();
            producer.send(
                    new ProducerRecord<>(KAFKA_TOPIC,
                            splitMsg.getKey(), splitMsg.getValue()));
            producer.sendOffsetsToTransaction(offsetsToCommit(consumerRecords), KAFKA_CONSUMER_GROUP);
            producer.commitTransaction();
        }

    }


    private Map<TopicPartition, OffsetAndMetadata> offsetsToCommit(ConsumerRecords<String, String> records) {
        Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> partitionedRecords = records.records(partition);
            long offset = partitionedRecords.get(partitionedRecords.size() - 1).offset();
            offsetsToCommit.put(partition, new OffsetAndMetadata(offset + 1));
        }
        return offsetsToCommit;
    }

    private void sendMessageNonConsumed(String message) {
        AbstractMap.SimpleEntry<String, String> splitMsg = MsgParser.parse(message);
        try {
            producer.beginTransaction();
            producer.send(
                    new ProducerRecord<>(KAFKA_TOPIC,
                            splitMsg.getKey(), splitMsg.getValue()));
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            e.printStackTrace();
            producer.close();
        } catch (KafkaException e) {
            e.printStackTrace();
            producer.abortTransaction();
        }
    }

    public void sendMessage(String message) {
//        sendMessageEos(message);
        sendMessageNonConsumed(message);
    }
}