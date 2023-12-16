package ru.ds.education.currencycbr.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.ds.education.currencycbr.model.CbrAdapterRequestDTO;
import ru.ds.education.currencycbr.model.CbrAdapterResponseDTO;
import ru.ds.education.currencycbr.model.Rate;
import ru.ds.education.currencycbr.model.pojo.ValuteCursOnDate;
import ru.ds.education.currencycbr.service.CbrService;
import ru.ds.education.currencycbr.util.DateUtil;
import ru.ds.education.currencycbr.xsd.GetCursOnDateXMLResponse;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;
import java.util.List;

@Component
@EnableJms
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {
    private final Queue queue;
    private final JmsTemplate jmsTemplate;
    private final CbrService cbrService;
    private final ObjectMapper objectMapper;
    private final DateUtil dateUtil;

    @JmsListener(destination = "${queue.request-queue}")
    public void listener(Message messageFromApi) throws DatatypeConfigurationException, JsonProcessingException, JMSException {

        CbrAdapterRequestDTO cbrAdapterRequestDTO = objectMapper.readValue(((TextMessage) messageFromApi).getText(), CbrAdapterRequestDTO.class);

        LocalDate date = dateUtil.formatDate(cbrAdapterRequestDTO.getOnDate());
        GetCursOnDateXMLResponse responseEntity = cbrService.getCursOnDateXMLResponse(date);
        String xml = cbrService.convertResponseToXml(responseEntity);

        List<ValuteCursOnDate> rates = cbrService.convertXmlToResult(xml);
        List<Rate> result = cbrService.getResult(rates);

        CbrAdapterResponseDTO cbrAdapterResponseDTO = new CbrAdapterResponseDTO(cbrAdapterRequestDTO.getOnDate(), result);
        String json = objectMapper.writeValueAsString(cbrAdapterResponseDTO);

        TextMessage message = new ActiveMQTextMessage();
        message.setJMSCorrelationID(messageFromApi.getJMSCorrelationID());
        message.setText(json);

        log.info("CORRELATION_ID: " + messageFromApi.getJMSCorrelationID());
        log.info("MESSAGE FROM API: " + ((TextMessage) messageFromApi).getText());

        log.info("CORRELATION_ID: " + messageFromApi.getJMSCorrelationID());
        log.info("MESSAGE TO API: " + ((TextMessage) message).getText());

        jmsTemplate.convertAndSend(queue, message);
    }
}
