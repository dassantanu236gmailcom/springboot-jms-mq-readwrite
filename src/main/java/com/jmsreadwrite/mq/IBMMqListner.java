package com.jmsreadwrite.mq;

import javax.jms.*;
import com.ibm.mq.jms.MQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static java.lang.String.*;

@Component
public class IBMMqListner {
    @Value("${ibm.mq.queue}")
    private String sourceQueue;

    @Autowired
    JmsTemplate jmsTemplate;

    private final Logger logger = LogManager.getLogger(IBMMqListner.class);

    @JmsListener(destination = "${ibm.mq.queue}", containerFactory = "jmsListenerFactory")
    public void receiveMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        final String textMessageBody = textMessage.getText();
        logger.info("message available in source queue " + sourceQueue);
        logger.info("Received message body is " + textMessageBody);
        logger.info("Received message length is " + textMessageBody.length());

        // send response
        MQQueue orderRequestQueue = new MQQueue("ORDER.RESPONSE");
        jmsTemplate.convertAndSend(orderRequestQueue,textMessageBody, responseMessage -> {
            responseMessage.setJMSCorrelationID(textMessage.getJMSCorrelationID());
            logger.info("message sent successfully to target "+ orderRequestQueue);
            logger.info("message JMSCorrelationID is  "+ textMessage.getJMSCorrelationID());
            return responseMessage;
        });
    }

}
