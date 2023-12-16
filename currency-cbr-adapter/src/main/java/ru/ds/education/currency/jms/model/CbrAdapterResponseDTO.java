package ru.ds.education.currency.jms.model;

import lombok.Data;

import java.util.List;

@Data
public class CbrAdapterResponseDTO {
    private String onDate;
    private List<Rate> rates;

    public CbrAdapterResponseDTO() {}

    public CbrAdapterResponseDTO(String onDate, List<Rate> rates) {
        this.onDate = onDate;
        this.rates = rates;
    }
}
