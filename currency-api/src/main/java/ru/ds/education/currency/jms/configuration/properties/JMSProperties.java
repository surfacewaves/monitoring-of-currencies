package ru.ds.education.currency.jms.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "queue")
@Getter
@Setter
public class JMSProperties {
    String requestQueue;
    String responseQueue;
    int timeout;
}
