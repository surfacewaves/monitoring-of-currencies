package ru.ds.education.currency.service.implementation;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import ru.ds.education.currency.model.dto.CurrencyRateRequestDTO;
import ru.ds.education.currency.model.entity.CurrencyRateRequestEntity;
import ru.ds.education.currency.model.enums.Status;
import ru.ds.education.currency.repository.CurrencyRateRequestRepository;
import ru.ds.education.currency.service.CurrencyRateRequestService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyRateRequestServiceImpl implements CurrencyRateRequestService {
    private final CurrencyRateRequestRepository currencyRateRequestRepository;
    private final MapperFacade modelMapper;

    @Override
    public List<CurrencyRateRequestDTO> findByDate(LocalDate localDate) {
        return currencyRateRequestRepository.findByDate(localDate).stream()
                .map(currencyRateRequestEntity -> modelMapper.map(currencyRateRequestEntity, CurrencyRateRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyRateRequestDTO findByCurrencyDateAndMaxRequestDate(List<CurrencyRateRequestDTO> dtos) {
        if (dtos.isEmpty()) {
            return null;
        }

        CurrencyRateRequestDTO currencyRateRequestDTO = dtos.stream().max(Comparator.comparing(CurrencyRateRequestDTO::getTimestamp)).get();

        if (currencyRateRequestDTO.getStatus() == Status.FAILED)
            return null;

        return currencyRateRequestDTO;
    }

    @Override
    public void save(LocalDate localDate) {
        CurrencyRateRequestEntity request = new CurrencyRateRequestEntity();
        request.setDate(localDate);
        request.setTimestamp(new Timestamp((new Date()).getTime()));
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setStatus(Status.CREATED);

        currencyRateRequestRepository.save(request);
    }

    @Override
    public void updateStatusByCorrelationId(String correlationId, Status status) {
        CurrencyRateRequestEntity entity = currencyRateRequestRepository.findByCorrelationId(correlationId).get();
        entity.setStatus(status);
        currencyRateRequestRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByDateLessThanEqual(LocalDate localDate) {
        currencyRateRequestRepository.deleteByDateLessThanEqual(localDate);
    }
}

