package ru.ds.education.currency.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;
import ru.ds.education.currency.model.dto.CurrencyRateRequestDTO;
import ru.ds.education.currency.model.entity.CurrencyRateEntity;
import ru.ds.education.currency.model.enums.Status;
import ru.ds.education.currency.repository.CurrencyRateRepository;
import ru.ds.education.currency.service.CurrencyRateRequestService;
import ru.ds.education.currency.service.CurrencyRateService;
import ru.ds.education.currency.util.AsyncProducer;
import ru.ds.education.currency.util.DateUtil;

import javax.jms.JMSException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;

    private final MapperFacade modelMapper;

    private final DateUtil dateUtil;

    private final CurrencyRateRequestService currencyRateRequestService;

    private final AsyncProducer asyncProducer;


    @Override
    public List<CurrencyRateDTO> findByDate(LocalDate localDate) {
        return currencyRateRepository.findByDate(localDate).stream()
                .map(currencyRateEntity -> modelMapper.map(currencyRateEntity, CurrencyRateDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyRateDTO findByCodeAndDate(short code, LocalDate localDate) {
        return currencyRateRepository.findByCodeAndDate(code, localDate)
                .map(value -> modelMapper.map(value, CurrencyRateDTO.class)).orElse(null);
    }

    @Override
    @Transactional
    public void save(CurrencyRateDTO dto) {
        CurrencyRateEntity currencyRateEntity = modelMapper.map(dto, CurrencyRateEntity.class);
        currencyRateRepository.save(currencyRateEntity);
    }

    @Override
    @Transactional
    public void deleteByDateLessThanEqual(LocalDate localDate) {
        currencyRateRepository.deleteByDateLessThanEqual(localDate);
    }

    @Override
    public Boolean existsByDate(LocalDate localDate) {
        return currencyRateRepository.existsByDate(localDate);
    }

    @Override
    public List<CurrencyRateDTO> getRates(LocalDate localDate, Short code) {
        List<CurrencyRateDTO> dtos = new ArrayList<>();
        if (code != null) {
            CurrencyRateDTO temp = findByCodeAndDate(code, localDate);
            if (temp != null) {
                dtos.add(temp);
            }
        } else {
            dtos = findByDate(localDate);
        }
        return dtos;
    }

    @Override
    public ResponseEntity<List<CurrencyRateDTO>> getRatesIfTheyExistOrCreate(LocalDate date, Short code) throws JMSException, JsonProcessingException, InterruptedException {
        if (!existsByDate(date)) {
            CurrencyRateRequestDTO currencyRateRequestDTO = currencyRateRequestService.findByCurrencyDateAndMaxRequestDate(currencyRateRequestService.findByDate(date));

            if (currencyRateRequestDTO == null) {
                currencyRateRequestService.save(date);
                asyncProducer.produce(currencyRateRequestService.findByCurrencyDateAndMaxRequestDate(currencyRateRequestService.findByDate(date)));
                return new ResponseEntity<>(null, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
            }
        } else {
            if (currencyRateRequestService.findByCurrencyDateAndMaxRequestDate(currencyRateRequestService.findByDate(date)).getStatus() == Status.SUCCESSFUL) {
                return new ResponseEntity<>(getRates(date, code), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
            }
        }
    }
}
