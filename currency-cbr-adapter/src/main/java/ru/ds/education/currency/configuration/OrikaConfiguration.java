package ru.ds.education.currency.configuration;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.context.annotation.Configuration;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;
import ru.ds.education.currency.model.dto.CurrencyRateRequestDTO;
import ru.ds.education.currency.model.entity.CurrencyRateEntity;
import ru.ds.education.currency.model.entity.CurrencyRateRequestEntity;

@Configuration
public class OrikaConfiguration extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(CurrencyRateEntity.class, CurrencyRateDTO.class)
                .byDefault()
                .register();
        factory.classMap(CurrencyRateRequestEntity.class, CurrencyRateRequestDTO.class)
                .byDefault()
                .register();
    }
}
