package ru.ds.education.currency.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Сущность курса валюты")
@Getter
@Setter
public class CurrencyRateDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Наименование валюты", example = "USD")
    @NotNull
    @Pattern(regexp = "^[A-Z][A-Z][A-Z]$")
    private String currency;

    @Schema(description = "Код валюты")
    @NotNull
    @Min(0)
    @Max(999)
    private short code;

    @Schema(description = "Курс валюты")
    @NotNull
    private float curs;

    @Schema(description = "Дата курса валюты", example = "2023-10-30")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

}
