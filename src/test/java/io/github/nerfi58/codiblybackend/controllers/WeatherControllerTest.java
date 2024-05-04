package io.github.nerfi58.codiblybackend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnBadRequestWhenParameterIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/weather").param(
                "daily",
                "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration"
        ).param("lat", "-100").param("lon", "50")).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldResponseWithTemperaturesWWhenParameterIsValid() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/weather").param(
                "daily",
                "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration"
        ).param("lat", "0").param("lon", "0")).andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).isNotEmpty();
        assertThat(response.size()).isEqualTo(7);

    }
}