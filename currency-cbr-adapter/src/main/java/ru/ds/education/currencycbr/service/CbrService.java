package ru.ds.education.currencycbr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ds.education.currencycbr.client.CbrClient;
import ru.ds.education.currencycbr.model.Rate;
import ru.ds.education.currencycbr.model.pojo.ValuteCursOnDate;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXML;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXMLResponse;
import ru.ds.education.currencycbr.xsd.ObjectFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CbrService {

    private final CbrClient cbrClient;

    private final ObjectMapper objectMapper;

    private final XmlMapper xmlMapper;

    private final ObjectFactory objectFactory;

    public GetCursOnDateXMLResponse getCursOnDateXMLResponse(LocalDate date) throws DatatypeConfigurationException {
        GregorianCalendar gDate = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        XMLGregorianCalendar xDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDate);

        GetCursOnDateXML request = objectFactory.createGetCursOnDateXML();
        request.setOnDate(xDate);

        return cbrClient.getCursOnDateXMLResponse(request);
    }

    public String convertResponseToXml(GetCursOnDateXMLResponse response) {
        return objectMapper.convertValue(response.getGetCursOnDateXMLResult().getContent().get(0), String.class);
    }

    public List<ValuteCursOnDate> convertXmlToResult(String xml) throws JsonProcessingException {
        return xmlMapper.readValue(xml, new TypeReference<List<ValuteCursOnDate>>() {});
    }

    public List<Rate> getResult(List<ValuteCursOnDate> rates) {
        rates.remove(0);
        List<Rate> result = new ArrayList<>();
        for (ValuteCursOnDate curs : rates) {
            result.add(new Rate(curs.getVchCode(), curs.getVcurs(), (short) curs.getVcode()));
        }
        return result;
    }
}