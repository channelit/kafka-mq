package biz.cits.mq.consumer;

import biz.cits.kafka.producer.FifoProducer;
import com.ibm.mq.jms.MQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;

@Component
public class MqConsumer implements MessageListener {

    private final ConnectionFactory connectionFactory;

    @Value("${mq.queue.name}")
    private String queueName;

    private final JMSContext context;

    private Connection qc;

    @Value("${mq.app.name}")
    private String appName;

    @Value("${mq.qmgr}")
    private String QMGR;

    @Autowired
    FifoProducer fifoProducer;

    private static boolean transacted = true;

    @Autowired
    public MqConsumer(ConnectionFactory connectionFactory) throws JMSException {
        this.connectionFactory = connectionFactory;
        this.context = connectionFactory.createContext();
    }

    @PostConstruct
    public void init() throws Exception {
        MQQueue queue = new MQQueue(QMGR, queueName);
        Session session;
        this.qc = this.connectionFactory.createConnection();
        session = this.qc.createSession(transacted, Session.SESSION_TRANSACTED);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
    }

    public void start() throws JMSException {
        this.qc.start();
    }

    @Override
    public void onMessage(Message message) {
        try {
            fifoProducer.sendMessage(((TextMessage)message).getText());
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        };
    }
}
