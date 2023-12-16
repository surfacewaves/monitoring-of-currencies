package ru.ds.education.currencycbr.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cbr.request")
@Getter
@Setter
public class CbrProperties {
    String uri;
    String action;
}
