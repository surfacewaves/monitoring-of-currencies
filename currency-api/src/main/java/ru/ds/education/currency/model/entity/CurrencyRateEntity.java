package ru.ds.education.currency.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "curs_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "code")
    private short code;

    @Column(name = "curs")
    private float curs;

    @Column(name = "curs_date")
    private LocalDate date;
}
