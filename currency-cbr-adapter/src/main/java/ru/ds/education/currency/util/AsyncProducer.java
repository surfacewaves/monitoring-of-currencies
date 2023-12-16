package ru.ds.education.currency.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ds.education.currency.jms.configuration.properties.JMSProperties;
import ru.ds.education.currency.jms.producer.MessageProducer;
import ru.ds.education.currency.model.dto.CurrencyRateRequestDTO;
import ru.ds.education.currency.model.enums.Status;
import ru.ds.education.currency.service.CurrencyRateRequestService;

import javax.jms.JMSException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncProducer {

    private final MessageProducer messageProducer;

    private final CurrencyRateRequestService currencyRateRequestService;

    private final JMSProperties jmsProperties;

    @Async
    public void produce(CurrencyRateRequestDTO dto) throws JMSException, JsonProcessingException, InterruptedException {
        messageProducer.produce(dto.getDate(), dto.getCorrelationId());
        currencyRateRequestService.updateStatusByCorrelationId(dto.getCorrelationId(), Status.SENT);

        log.info("#####");
        log.info(Thread.currentThread().getName() + " start");
        log.info("#####");

        Thread.sleep(jmsProperties.getTimeout());

        log.info("#####");
        log.info(Thread.currentThread().getName() + " end");
        log.info("#####");

        if (currencyRateRequestService.findByCurrencyDateAndMaxRequestDate(currencyRateRequestService.findByDate(dto.getDate())).getStatus() != Status.SUCCESSFUL) {
            currencyRateRequestService.updateStatusByCorrelationId(dto.getCorrelationId(), Status.FAILED);
        }
    }
}
