package biz.cits.kafka.stream;

import biz.cits.db.DataStore;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Properties;

@Component
public class FifoStream {

    private final Properties streamProperties;

    private final DataStore dataStore;

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    @Autowired
    public FifoStream(Properties streamProperties, DataStore dataStore) {
        this.streamProperties = streamProperties;
        this.dataStore = dataStore;
    }

    public void streamProcessor() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(KAFKA_TOPIC);
        source.foreach((k, v) -> {
            HashMap<String, String> records = new HashMap<>();
            records.put(k, v);
            dataStore.storeData(k, records);
        });
        KafkaStreams streams = new KafkaStreams(builder.build(), streamProperties);
        streams.start();
    }
}
