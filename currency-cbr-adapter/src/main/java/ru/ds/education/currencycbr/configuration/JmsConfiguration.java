package ru.ds.education.currencycbr.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ds.education.currencycbr.configuration.properties.JMSProperties;

import javax.jms.Queue;

@Configuration
@RequiredArgsConstructor
public class JmsConfiguration {
    private final JMSProperties jmsProperties;

    @Bean
    public Queue queue(){
        return new ActiveMQQueue(jmsProperties.getResponseQueue());
    }
}