package ru.ds.education.currency.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ds.education.currency.model.enums.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "curs_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateRequestEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "curs_date")
    private LocalDate date;

    @Column(name = "request_date")
    private Timestamp timestamp;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "status")
    private Status status;
}
