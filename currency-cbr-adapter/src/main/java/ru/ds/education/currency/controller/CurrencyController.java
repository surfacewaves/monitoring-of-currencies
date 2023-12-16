package ru.ds.education.currency.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ds.education.currency.exception.InvalidDateException;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;
import ru.ds.education.currency.service.CurrencyRateRequestService;
import ru.ds.education.currency.service.CurrencyRateService;
import ru.ds.education.currency.util.DateUtil;

import javax.jms.JMSException;
import javax.validation.Valid;

@Tag(name = "Контроллер курсов валют", description = "Взаимодействие с курсами валютами")
@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyRateService currencyRateService;

    private final DateUtil dateUtil;

    private final CurrencyRateRequestService currencyRateRequestService;

    @Operation(
            summary = "Получить курс(-ы) валюты",
            description = "Позволяет получить либо список курсов валют по введенной датей (yyyy-mm-dd), " +
                    "либо один курс валюты по введенной дате и коду"
    )
    @GetMapping()
    public ResponseEntity<?> getCurrencyRates(
            @RequestParam(value = "code", required = false) @Parameter(description = "Код валюты") Short code,
            @RequestParam(value = "date") @Parameter(description = "Дата курса валюты") String notFormattedDate)
            throws JMSException, JsonProcessingException, InterruptedException, InvalidDateException {
        return currencyRateService.getRatesIfTheyExistOrCreate(dateUtil.convertDate(notFormattedDate), code);
    }

    @Operation(
            summary = "Добавить курс валюты",
            description = "Позволяет добавить курс валюты"
    )
    @PostMapping("/add")
    public HttpStatus addCurrencyRate(@Valid @RequestBody CurrencyRateDTO currencyRateDTO) {
        currencyRateService.save(currencyRateDTO);
        return HttpStatus.CREATED;
    }

    @Operation(
            summary = "Удалить курс(-ы) валюты",
            description = "Позволяет удалить курсы валют, дата которых меньше введенной"
    )
    @PostMapping("/delete")
    public HttpStatus deleteByDate(@RequestParam(value = "date")
                                             @Parameter(description = "Дата, меньшей-равной которой курсы удаляются") String notFormattedDate) throws InvalidDateException {
        currencyRateService.deleteByDateLessThanEqual(dateUtil.convertDate(notFormattedDate));
        currencyRateRequestService.deleteByDateLessThanEqual(dateUtil.convertDate(notFormattedDate));
        return HttpStatus.OK;
    }
}
