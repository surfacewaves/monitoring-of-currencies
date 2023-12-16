package ru.ds.education.currency.jms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    private String name;
    private Double curs;
    private short code;
}
