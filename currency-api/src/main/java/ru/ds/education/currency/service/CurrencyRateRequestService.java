package ru.ds.education.currency.service;

import ru.ds.education.currency.model.dto.CurrencyRateRequestDTO;
import ru.ds.education.currency.model.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateRequestService {
    public List<CurrencyRateRequestDTO> findByDate(LocalDate localDate);

    public CurrencyRateRequestDTO findByCurrencyDateAndMaxRequestDate(List<CurrencyRateRequestDTO> dtos);

    public void save(LocalDate localDate);

    public void updateStatusByCorrelationId(String correlationId ,Status status);

    public void deleteByDateLessThanEqual(LocalDate localDate);
}
