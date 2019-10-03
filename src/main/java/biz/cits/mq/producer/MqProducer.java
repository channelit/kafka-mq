package biz.cits.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class MqProducer {

    private final ConnectionFactory connectionFactory;

    private final Logger logger = LoggerFactory.getLogger(MqProducer.class);

    @Value("${mq.queue.name}")
    private String queueName;

    private final JMSContext context;

    @Autowired
    public MqProducer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.context = connectionFactory.createContext();
    }

    @Bean
    public JMSProducer jmsProducer() {
        JMSContext context = connectionFactory.createContext();
        return context.createProducer();
    }

    @Bean
    public Destination destination() {
        return context.createQueue("queue:///" + queueName);
    }

    public void sendMessage(Destination destination, JMSProducer jmsProducer, String message) {
        TextMessage textMessage = this.context.createTextMessage(message);
        jmsProducer.send(destination, textMessage);
    }
}
