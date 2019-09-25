package biz.cits.mq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;


@Configuration
public class MQProperties {

    @Value("${mq.host}")
    private String host;

    private Integer port=1414;

    @Value("${mq.channel}")
    private String channel;

    @Value("${mq.qmgr}")
    private String QMGR;

    @Value("${mq.app.user}")
    private String appUser;

    @Value("${mq.app.pswd}")
    private String appPswd;

    @Value("${mq.app.name}")
    private String appName;

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
        cf.setIntProperty(WMQConstants.WMQ_PORT, port);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
        cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, appName);
        cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
//        cf.setStringProperty(WMQConstants.USERID, appUser);
//        cf.setStringProperty(WMQConstants.PASSWORD, appPswd);
        return cf;
    }

}
