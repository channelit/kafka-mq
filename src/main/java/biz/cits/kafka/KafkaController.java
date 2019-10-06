package biz.cits.kafka;

import biz.cits.kafka.consumer.FifoConsumer;
import biz.cits.kafka.stream.FifoStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class KafkaController {

    private final FifoConsumer fifoConsumer;

    private final FifoStream fifoStream;

    @Autowired
    public KafkaController(FifoConsumer fifoConsumer, FifoStream fifoStream) {
        this.fifoConsumer = fifoConsumer;
        this.fifoStream = fifoStream;
    }


    @GetMapping(path = "consumer/recv", produces = "application/json")
    public String consumerRecv()  {
        fifoConsumer.start();
        return "done";
    }

    @GetMapping(path = "stream/recv", produces = "application/json")
    public String streamRecv()  {
        fifoStream.start();
        return "done";
    }

}
