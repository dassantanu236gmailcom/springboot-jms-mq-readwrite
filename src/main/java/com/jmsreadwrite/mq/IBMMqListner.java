package com.jmsreadwrite.mq;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.jms.*;

import com.ibm.mq.jms.MQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class IBMMqListner {

    @Autowired
    JmsTemplate jmsTemplate;

    private Logger LOGGER = LogManager.getLogger(MessageListener.class);

    @JmsListener(destination = "${ibm.mq.queue}", containerFactory = "jmsListenerFactory")
    public void receiveMessage(Message message) throws JMSException {
        String logMethod = "receiveMessage(Message message)";
        TextMessage textMessage = (TextMessage) message;
        final String textMessageBody = textMessage.getText();

        // send response
        MQQueue orderRequestQueue = new MQQueue("ORDER.RESPONSE");
        jmsTemplate.convertAndSend(orderRequestQueue,textMessageBody, responseMessage -> {
            responseMessage.setJMSCorrelationID(textMessage.getJMSCorrelationID());
            return responseMessage;
        });
    }

}
