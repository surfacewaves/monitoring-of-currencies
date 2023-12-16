package ru.ds.education.currency.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Exchange Rates API",
                description = "Exchange Rates", version = "1.0.0",
                contact = @Contact(
                        name = "Andreev Nicolay",
                        email = "nandreev@digital-spirit.ru",
                        url = "https://gitlab.digital-spirit.ru/nandreev"
                )
        )
)
public class OpenApiConfiguration {
}
