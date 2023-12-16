package ru.ds.education.currency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import ru.ds.education.currency.exception.InvalidDateException;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;

import javax.jms.JMSException;
import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateService {

    public List<CurrencyRateDTO> findByDate(LocalDate localDate);

    public CurrencyRateDTO findByCodeAndDate(short code, LocalDate localDate);

    public void save(CurrencyRateDTO currencyRateDTO);

    public void deleteByDateLessThanEqual(LocalDate localDate);

    public Boolean existsByDate(LocalDate localDate);

    public List<CurrencyRateDTO> getRates(LocalDate localDate, Short code);

    public ResponseEntity<List<CurrencyRateDTO>> getRatesIfTheyExistOrCreate(LocalDate date, Short code) throws InvalidDateException, JMSException, JsonProcessingException, InterruptedException;
}
