package ru.ds.education.currency.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ds.education.currency.model.entity.CurrencyRateEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends CrudRepository<CurrencyRateEntity, Long> {
    List<CurrencyRateEntity> findByDate(LocalDate localDate);

    Optional<CurrencyRateEntity> findByCodeAndDate(short code, LocalDate localDate);

    void deleteByDateLessThanEqual(LocalDate localDate);

    Boolean existsByDate(LocalDate localDate);
}