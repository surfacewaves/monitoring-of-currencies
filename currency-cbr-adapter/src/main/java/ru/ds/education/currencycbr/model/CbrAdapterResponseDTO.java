package ru.ds.education.currencycbr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CbrAdapterResponseDTO {
    private String onDate;
    private List<Rate> rates;
}
