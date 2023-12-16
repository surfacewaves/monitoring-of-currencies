package ru.ds.education.currency.jms.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.ds.education.currency.jms.model.CbrAdapterRequestDTO;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MessageProducer {
    private final Queue queue;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void produce(LocalDate localDate, String correlationId) throws JsonProcessingException, JMSException {
        CbrAdapterRequestDTO cbrAdapterRequestDTO = new CbrAdapterRequestDTO(localDate.toString());
        String json = objectMapper.writeValueAsString(cbrAdapterRequestDTO);

        TextMessage message = new ActiveMQTextMessage();
        message.setJMSCorrelationID(correlationId);
        message.setText(json);

        jmsTemplate.convertAndSend(queue, message);
    }
}
