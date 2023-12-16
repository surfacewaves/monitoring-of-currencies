package ru.ds.education.currency.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.ds.education.currency.model.enums.Status;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;

@Schema(description = "Сущность запроса курса валют")
@Getter
@Setter
public class CurrencyRateRequestDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Дата курса валюты", example = "2023-10-30")
    @NotNull
    private LocalDate date;

    @Schema(description = "Дата и время создания запроса", example = "2023-10-30 10:09:21.61")
    @NotNull
    private Timestamp timestamp;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String correlationId;

    @Schema(description = "Статус запроса", example = "CREATED/SENT/PROCESSED/FAILED/SUCCESSFUL")
    @NotNull
    private Status status;
}
