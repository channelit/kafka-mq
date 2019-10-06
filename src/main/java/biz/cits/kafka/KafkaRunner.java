package biz.cits.kafka;

import biz.cits.kafka.consumer.FifoConsumer;
import biz.cits.kafka.stream.FifoStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner implements ApplicationRunner {

    @Value("${kafka.startConsumer}")
    private boolean startConsumer;

    @Value("${kafka.startStream}")
    private boolean startStream;

    private final FifoConsumer fifoConsumer;

    private final FifoStream fifoStream;

    @Autowired
    public KafkaRunner(FifoConsumer fifoConsumer, FifoStream fifoStream) {
        this.fifoConsumer = fifoConsumer;
        this.fifoStream = fifoStream;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (startConsumer) {
            fifoConsumer.start();
        }

        if (startStream) {
            fifoStream.start();
        }

    }
}
