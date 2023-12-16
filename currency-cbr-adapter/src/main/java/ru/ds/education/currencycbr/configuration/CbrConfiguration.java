package ru.ds.education.currencycbr.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import ru.ds.education.currencycbr.configuration.properties.MarshallerProperties;
import ru.ds.education.currencycbr.xsd.ObjectFactory;

@Configuration
@RequiredArgsConstructor
public class CbrConfiguration {

    private final MarshallerProperties marshallerProperties;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(marshallerProperties.getLocation());
        return marshaller;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper;
    }

    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }
}
