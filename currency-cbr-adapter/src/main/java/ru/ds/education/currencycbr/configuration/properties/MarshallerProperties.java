package ru.ds.education.currencycbr.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jaxb2.marshaller.xsd")
@Getter
@Setter
public class MarshallerProperties {
    String location;
}
