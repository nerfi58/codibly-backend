package com.example.codiblybackend.controller;

import com.example.codiblybackend.dto.DailyWeatherDto;
import com.example.codiblybackend.dto.WeeklyWeatherSummaryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnBadRequestWhenParameterIsInvalid() throws Exception {
        mockMvc.perform(get("/weather")
                                .param("lat", "-100")
                                .param("lon", "50")
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldResponseWithWeatherForecastWhenParametersAreValid() throws Exception {
        MvcResult result = mockMvc.perform(get("/weather")
                                                   .param("lat", "0")
                                                   .param("lon", "0"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        List<DailyWeatherDto> weatherForecast = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(weatherForecast).isNotNull();
        assertThat(weatherForecast.size()).isEqualTo(7);
    }

    @Test
    void shouldResponseWithWeeklyWeatherSummaryWhenParametersAreValid() throws Exception {

        MvcResult result = mockMvc.perform(get("/weather/weekly-summary")
                                                   .param("lat", "0")
                                                   .param("lon", "0"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        WeeklyWeatherSummaryDto weeklyWeatherSummaryDto = objectMapper.readValue(response, WeeklyWeatherSummaryDto.class);

        assertThat(weeklyWeatherSummaryDto).isNotNull();
    }
}
