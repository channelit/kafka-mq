package biz.cits.kafka;

import biz.cits.kafka.consumer.FifoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner implements ApplicationRunner {

    @Value("${kafka.startConsumer}")
    private boolean startConsumer;

    private final FifoConsumer fifoConsumer;

    @Autowired
    public KafkaRunner(FifoConsumer fifoConsumer) {
        this.fifoConsumer = fifoConsumer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (startConsumer) {
            fifoConsumer.start();
        }

    }
}
