package biz.cits.mq;

import biz.cits.mq.producer.MqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSProducer;
import java.util.stream.IntStream;

@RestController
@RequestMapping("mq")
public class MqController {

    @Autowired
    MqProducer mqProducer;

    @Autowired
    Destination destination;

    @Autowired
    JMSProducer jmsProducer;

    @GetMapping(path = "post", produces = "application/json")
    public String postMessages(@RequestParam int numMessage) {
        IntStream.range(0, numMessage)
                .peek(i -> {
                    if (i + 1 % 100 == 0) {
                        System.out.println("Test number " + i + " started.");
                    }
                }).forEach(i -> {
            mqProducer.sendMessage(destination, jmsProducer, String.valueOf(i));
        });
        return "done";
    }
}
