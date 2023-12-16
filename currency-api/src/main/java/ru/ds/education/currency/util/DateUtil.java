package ru.ds.education.currency.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ds.education.currency.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

@Component
@NoArgsConstructor
public class DateUtil {

    public LocalDate convertDate(String notFormattedDate) throws InvalidDateException {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\d{4}-((0\\d)|(1[012]))-(([012]\\d)|3[01])$");
        Matcher matcher = pattern.matcher(notFormattedDate);
        if (matcher.find()) {
            return LocalDate.parse(notFormattedDate, DateTimeFormatter.ofPattern("yyyy-MM-d"));
        } else {
            throw new InvalidDateException("Invalid Date");
        }
    }
}
