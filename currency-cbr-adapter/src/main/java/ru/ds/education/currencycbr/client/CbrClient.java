package ru.ds.education.currencycbr.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.ds.education.currencycbr.configuration.properties.CbrProperties;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXML;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXMLResponse;

@Component
public class CbrClient {

    private final WebServiceTemplate webServiceTemplate;

    private final CbrProperties cbrProperties;

    @Autowired
    public CbrClient(Jaxb2Marshaller marshaller, CbrProperties cbrProperties) {
        this.webServiceTemplate = new WebServiceTemplate(marshaller);
        this.cbrProperties = cbrProperties;
        this.webServiceTemplate.setDefaultUri(cbrProperties.getUri());
    }

    public GetCursOnDateXMLResponse getCursOnDateXMLResponse(GetCursOnDateXML request) {
        return (GetCursOnDateXMLResponse) webServiceTemplate.
                marshalSendAndReceive(request, new SoapActionCallback(cbrProperties.getAction()));
    }
}
