package ru.ds.education.currencycbr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ds.education.currencycbr.model.CbrAdapterResponseDTO;
import ru.ds.education.currencycbr.model.Rate;
import ru.ds.education.currencycbr.model.pojo.ValuteCursOnDate;
import ru.ds.education.currencycbr.service.CbrService;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXMLResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class ConsumerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CbrService cbrService;

	@Test
	void getCursOnDateXMLResponse() throws DatatypeConfigurationException, JsonProcessingException {
        GetCursOnDateXMLResponse responseEntity = cbrService.getCursOnDateXMLResponse(LocalDate.parse("2023-01-01"));
        String xml = cbrService.convertResponseToXml(responseEntity);

        List<ValuteCursOnDate> rates = cbrService.convertXmlToResult(xml);
        List<Rate> result = cbrService.getResult(rates);

        CbrAdapterResponseDTO cbrAdapterResponseDTO = new CbrAdapterResponseDTO("2023-01-01", result);
        String json = objectMapper.writeValueAsString(cbrAdapterResponseDTO);

        Assertions.assertEquals(cbrAdapterResponseDTO.getRates().size(), 34);
        Assertions.assertEquals(cbrAdapterResponseDTO.getOnDate(), "2023-01-01");
	}

}

