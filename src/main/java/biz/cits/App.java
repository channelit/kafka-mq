package biz.cits;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public KafkaProducer<String, String> producer() {
        Properties props = producerProperties();
//        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "bean-transactional-id");
        return new KafkaProducer<>(props);
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public Properties producerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_URL);
//        properties.put(ProducerConfig.ACKS_CONFIG, "all");
//        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10000);
//        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, KAFKA_CLIENT_ID);
//        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transactional-id");
//        properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG,1000);
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
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
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

}