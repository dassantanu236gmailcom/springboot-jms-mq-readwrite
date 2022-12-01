package com.jmsreadwrite.mq;

import java.util.List;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import com.ibm.msg.client.wmq.common.CommonConstants;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
@EnableJms
public class JmsConfig {
    @Value("${ibm.mq.port}")
    private int port;

    @Bean
    @ConfigurationProperties("ibm.mq")
    public MQConfigurationProperties mqConfigProperties() {
        return new MQConfigurationProperties();
    }

    @Bean
    public MQConnectionFactory mqConnectionFactory(MQConfigurationProperties mqConfigProperties,
                                                   ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers) {
        return new MQConnectionFactoryFactory(mqConfigProperties, factoryCustomizers.getIfAvailable())
                .createConnectionFactory(MQConnectionFactory.class);

    }

    @Bean
    public AbstractJmsListenerContainerFactory<DefaultMessageListenerContainer> jmsListenerFactory(ConnectionFactory mqConnectionFactory,
                                                                                                   DefaultJmsListenerContainerFactoryConfigurer configurer) throws JMSException {

        System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
        MQConnectionFactory mqcf = (MQConnectionFactory) mqConnectionFactory;
        mqcf.setSSLFipsRequired(false);
        mqcf.setPort(port);
        mqcf.setTransportType(CommonConstants.WMQ_CM_CLIENT);
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, mqcf);

        return factory;
    }

}
