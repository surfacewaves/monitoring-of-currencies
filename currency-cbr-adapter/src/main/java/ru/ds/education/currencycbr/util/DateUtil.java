package ru.ds.education.currencycbr.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class DateUtil {

    public LocalDate formatDate(String notFormattedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        return LocalDate.parse(notFormattedDate, formatter);
    }
}
