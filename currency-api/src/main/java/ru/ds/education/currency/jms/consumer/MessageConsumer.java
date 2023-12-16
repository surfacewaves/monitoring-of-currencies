package ru.ds.education.currency.jms.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.ds.education.currency.jms.model.CbrAdapterResponseDTO;
import ru.ds.education.currency.jms.model.Rate;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;
import ru.ds.education.currency.model.enums.Status;
import ru.ds.education.currency.service.CurrencyRateRequestService;
import ru.ds.education.currency.service.CurrencyRateService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDate;
import java.util.List;

@Component
@EnableJms
@RequiredArgsConstructor
public class MessageConsumer {
    private final ObjectMapper objectMapper;

    private final CurrencyRateService currencyRateService;

    private final CurrencyRateRequestService currencyRateRequestService;

    @JmsListener(destination = "${queue.response-queue}")
    public void listen(Message messageFromAdapter) throws JMSException, JsonProcessingException {
        CbrAdapterResponseDTO cbrAdapterResponseDTO = objectMapper.readValue(((TextMessage) messageFromAdapter).getText(), CbrAdapterResponseDTO.class);

        currencyRateRequestService.updateStatusByCorrelationId(messageFromAdapter.getJMSCorrelationID(), Status.PROCESSED);

        List<Rate> rates = cbrAdapterResponseDTO.getRates();
        CurrencyRateDTO dto = new CurrencyRateDTO();
        for (Rate rate : rates) {
            dto = initDTO(rate, LocalDate.parse(cbrAdapterResponseDTO.getOnDate()));
            currencyRateService.save(dto);
        }

        currencyRateRequestService.updateStatusByCorrelationId(messageFromAdapter.getJMSCorrelationID(), Status.SUCCESSFUL);
    }

    public CurrencyRateDTO initDTO(Rate rate, LocalDate localDate) {
        CurrencyRateDTO dto = new CurrencyRateDTO();

        dto.setDate(localDate);
        dto.setCode(rate.getCode());
        dto.setCurs(formatFloat(rate.getCurs()));
        dto.setCurrency(rate.getName());

        return dto;
    }

    public float formatFloat(double d) {
        int res1 = (int) d;
        double res2 = d - res1;

        res2 = res2 % 100;

        return (float) (res1 + res2);
    }
}
