package biz.cits;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class App {

    @Value("${kafka.topic.id}")
    private String KAFKA_TOPIC;

    @Value("${kafka.server.url}")
    private String KAFKA_SERVER_URL;

    @Value("${kafka.client.id}")
    private String KAFKA_CLIENT_ID;

    @Value("${kafka.consumer.group}")
    private String KAFKA_CONSUMER_GROUP;

    @Value("${db.mongo.host}")
    private String DB_MONGO_HOST;

    @Value("${db.mongo.port}")
    private Integer DB_MONGO_PORT;

    @Value("${db.mongo.name}")
    private String DB_MONGO_NAME;

    @Value("${db.mongo.user}")
    private String DB_MONGO_USER;

    @Value("${db.mongo.pswd}")
    private String DB_MONGO_PSWD;

    @Value("${kafka.partition.count}")
    private Integer KAFKA_PARTITION_COUNT;

    @Bean
    public KafkaProducer<String, String> producer(@Qualifier("producerProperties") Properties producerProperties) {
        createTopic();
        producerProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, KAFKA_CLIENT_ID);
        Producer<String, String> producer = new KafkaProducer<>(producerProperties, new StringSerializer(), new StringSerializer());
        return new KafkaProducer<>(producerProperties);
    }

    @Bean
    public KafkaConsumer consumer(@Qualifier("consumerProperties") Properties consumerProperties) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        return consumer;
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Properties producerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 2);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10000);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, KAFKA_CLIENT_ID);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 1000);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    @Bean
    public Properties consumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_CONSUMER_GROUP);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public Properties streamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, KAFKA_CONSUMER_GROUP);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10);
        return props;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    @Bean
    public Properties adminProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
        return properties;
    }


    @Bean
    public MongoClient mongoClient() {
        MongoCredential mongoCredential = MongoCredential.createCredential(DB_MONGO_USER, "admin", DB_MONGO_PSWD.toCharArray());
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(DB_MONGO_HOST, DB_MONGO_PORT))))
                        .credential(mongoCredential)
                        .build());
        return mongoClient;
    }

    @Bean
    @Autowired
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(DB_MONGO_NAME);
    }

    public void createTopic() {
        AdminClient adminClient = AdminClient.create(adminProperties());
        NewTopic newTopic = new NewTopic(KAFKA_TOPIC, KAFKA_PARTITION_COUNT, (short) 2);
        List<NewTopic> newTopics = new ArrayList<>();
        newTopics.add(newTopic);
        adminClient.createTopics(newTopics);
    }
}