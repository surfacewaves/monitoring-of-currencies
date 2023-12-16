package ru.ds.education.currency.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ds.education.currency.model.entity.CurrencyRateRequestEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRequestRepository extends CrudRepository<CurrencyRateRequestEntity, String> {
    List<CurrencyRateRequestEntity> findByDate(LocalDate localDate);

    Optional<CurrencyRateRequestEntity> findByCorrelationId(String correlationId);

    void deleteByDateLessThanEqual(LocalDate localDate);
}