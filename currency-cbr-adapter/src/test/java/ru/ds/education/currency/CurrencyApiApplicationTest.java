package ru.ds.education.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ds.education.currency.model.dto.CurrencyRateDTO;
import ru.ds.education.currency.model.entity.CurrencyRateRequestEntity;
import ru.ds.education.currency.model.enums.Status;
import ru.ds.education.currency.repository.CurrencyRateRequestRepository;
import ru.ds.education.currency.service.CurrencyRateService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = {CurrencyApiApplicationTest.Initializer.class})
public class CurrencyApiApplicationTest {

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.4");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "sping.flyway.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    CurrencyRateService currencyRateService;

    @Autowired
    CurrencyRateRequestRepository currencyRateRequestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenRates_whenNotValidDate_thenWrongDate() throws Exception {
        //given

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rate").param("date", "2023-01-"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(MockMvcResultMatchers.content().string("Wrong date"));
    }

    @Test
    public void givenRates_whenRatesNotExistAndRequestNotExists_thenRequestAccepted() throws Exception {
        //given

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rate").param("date", "2023-01-01"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated());
        response.andExpect(MockMvcResultMatchers.content().string("Request accepted"));
    }

    @Test
    public void givenRates_whenRatesNotExistAndRequestExists_thenRequestAccepted() throws Exception {
        //given
        CurrencyRateRequestEntity request = new CurrencyRateRequestEntity();
        request.setDate(LocalDate.parse("2023-01-01"));
        request.setTimestamp(new Timestamp((new Date()).getTime()));
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setStatus(Status.SENT);
        currencyRateRequestRepository.save(request);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rate").param("date", "2023-01-01"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isAccepted());
        response.andExpect(MockMvcResultMatchers.content().string("Wait"));
    }

    @Test
    public void givenRates_whenOnlyDateAndRequestStatusIsSuccessful_thenListOfRates() throws Exception {

        //given
        CurrencyRateDTO rate1 = new CurrencyRateDTO();
        rate1.setCurrency("TST");
        rate1.setCurs(11.11F);
        rate1.setDate(LocalDate.parse("2023-01-01"));
        rate1.setCode((short) 111);
        currencyRateService.save(rate1);

        CurrencyRateDTO rate2 = new CurrencyRateDTO();
        rate2.setCurrency("TST");
        rate2.setCurs(22.22F);
        rate2.setDate(LocalDate.parse("2023-01-01"));
        rate2.setCode((short) 222);
        currencyRateService.save(rate2);

        CurrencyRateRequestEntity request = new CurrencyRateRequestEntity();
        request.setDate(LocalDate.parse("2023-01-01"));
        request.setTimestamp(new Timestamp((new Date()).getTime()));
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setStatus(Status.SUCCESSFUL);
        currencyRateRequestRepository.save(request);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rate").param("date", "2023-01-01"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    public void givenRates_whenOnlyDateAndRequestStatusIsNotSuccessful_thenWait() throws Exception {
        //given
        CurrencyRateDTO rate1 = new CurrencyRateDTO();
        rate1.setCurrency("TST");
        rate1.setCurs(11.11F);
        rate1.setDate(LocalDate.parse("2023-01-01"));
        rate1.setCode((short) 111);
        currencyRateService.save(rate1);

        CurrencyRateDTO rate2 = new CurrencyRateDTO();
        rate2.setCurrency("TST");
        rate2.setCurs(22.22F);
        rate2.setDate(LocalDate.parse("2023-01-01"));
        rate2.setCode((short) 222);
        currencyRateService.save(rate2);

        CurrencyRateRequestEntity request = new CurrencyRateRequestEntity();
        request.setDate(LocalDate.parse("2023-01-01"));
        request.setTimestamp(new Timestamp((new Date()).getTime()));
        request.setCorrelationId(UUID.randomUUID().toString());
        request.setStatus(Status.PROCESSED);
        currencyRateRequestRepository.save(request);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rate").param("date", "2023-01-01"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isAccepted());
        response.andExpect(MockMvcResultMatchers.content().string("Wait"));
    }

    @Test
    public void addRate_whenValidBody_thenNewRecord() throws Exception {
        //given
        CurrencyRateDTO dto = new CurrencyRateDTO();
        dto.setDate(LocalDate.parse("2023-01-01"));
        dto.setId(1);
        dto.setCurrency("TST");
        dto.setCode((short) 111);
        dto.setCurs(11.11F);

        //when
        ResultActions response = mockMvc.perform(post("/rate/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated());
        response.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("date").value("2023-01-01"));
    }

    @Test
    public void addRate_whenNotValidBody_thenBadRequest() throws Exception {
        //given
        CurrencyRateDTO dto = new CurrencyRateDTO();
        dto.setDate(LocalDate.parse("2023-01-01"));
        dto.setId(1);
        dto.setCurrency("TST TST TST");
        dto.setCode((short) 111);
        dto.setCurs(11.11F);

        //when
        ResultActions response = mockMvc.perform(post("/rate/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteRate_whenValidDate_thenOK() throws Exception {
        //given
        CurrencyRateDTO rate1 = new CurrencyRateDTO();
        rate1.setCurrency("TST");
        rate1.setCurs(11.11F);
        rate1.setDate(LocalDate.parse("2023-01-01"));
        rate1.setCode((short) 111);
        currencyRateService.save(rate1);

        //when
        ResultActions response = mockMvc.perform(post("/rate/delete").param("date", "2023-01-01"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteRate_whenNotValidDate_thenOK() throws Exception {
        //given

        //when
        ResultActions response = mockMvc.perform(post("/rate/delete").param("date", "2023-01-"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
