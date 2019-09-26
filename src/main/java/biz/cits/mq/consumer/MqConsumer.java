package biz.cits.mq.consumer;

import javax.jms.Message;
import javax.jms.MessageListener;

public class MqConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println(message);
    }
}
