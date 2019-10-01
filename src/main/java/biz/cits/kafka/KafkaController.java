package biz.cits.kafka;

import biz.cits.kafka.consumer.FifoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private FifoConsumer fifoConsumer;


    @GetMapping(path = "recv", produces = "application/json")
    public String recvMessages()  {
        fifoConsumer.start();
        return "done";
    }

}
