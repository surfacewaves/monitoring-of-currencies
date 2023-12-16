package ru.ds.education.currency.controller.viewController;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ds.education.currency.exception.InvalidDateException;
import ru.ds.education.currency.service.CurrencyRateRequestService;
import ru.ds.education.currency.service.CurrencyRateService;
import ru.ds.education.currency.util.DateUtil;

import javax.jms.JMSException;
import java.time.LocalDate;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ViewController {
    private final CurrencyRateService currencyRateService;

    private final DateUtil dateUtil;

    private final CurrencyRateRequestService currencyRateRequestService;

    @GetMapping("/view/rate")
    public String getListOfRatesAndView(
            @RequestParam(value = "code", required = false) Short code,
            @RequestParam(value = "date") String notFormattedDate,
            Model model)
            throws JMSException, JsonProcessingException, InterruptedException, InvalidDateException {
        model.addAttribute("rates", currencyRateService.getRatesIfTheyExistOrCreate(
                dateUtil.convertDate(notFormattedDate), null).getBody());
        model.addAttribute("date", notFormattedDate);
        return "list";
    }


    @GetMapping("/about")
    public String getViewOfAboutInfo() {
        return "about";
    }

    @GetMapping("/start")
    public String getViewOfAboutInfo(Model model) {
        return "start";
    }
}
