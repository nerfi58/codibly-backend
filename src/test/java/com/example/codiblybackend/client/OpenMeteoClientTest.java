package com.example.codiblybackend.client;

import com.example.codiblybackend.dto.OpenMeteoWeatherDto;
import com.example.codiblybackend.exception.FailedToGetDataException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServiceUnavailable;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OpenMeteoClient.class)
class OpenMeteoClientTest {

    @Autowired
    private OpenMeteoClient openMeteoClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFetchWeatherData() throws JsonProcessingException {

        double lat = 0;
        double lon = 0;

        OpenMeteoWeatherDto expectedResponse = getExpectedResponse();
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=0.0&longitude=0.0&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration&hourly=surface_pressure";

        mockRestServiceServer.expect(requestTo(apiUrl)).andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));
        OpenMeteoWeatherDto actualResponse = openMeteoClient.getWeatherData(lat, lon);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getDaily()).isNotNull();
        assertThat(actualResponse.getHourly()).isNotNull();
        assertThat(actualResponse.getDaily()).isEqualTo(expectedResponse.getDaily());
        assertThat(actualResponse.getHourly()).isEqualTo(expectedResponse.getHourly());
    }

    private static OpenMeteoWeatherDto getExpectedResponse() {
        OpenMeteoWeatherDto expectedResponse = new OpenMeteoWeatherDto();
        expectedResponse.setDaily(new OpenMeteoWeatherDto.DailyDto(
                List.of(LocalDate.parse("2024-11-23"), LocalDate.parse("2024-11-24")),
                List.of(51, 53),
                List.of(2.0, 3.6),
                List.of(12.4, 13.2),
                List.of(1418.61, 17120.99)
        ));
        expectedResponse.setHourly(new OpenMeteoWeatherDto.HourlyDto(
                List.of(LocalDateTime.parse("2024-11-24T00:00"), LocalDateTime.parse("2024-11-24T01:00"), LocalDateTime.parse("2024-11-24T02:00")),
                List.of(1007.3, 1006.7, 1005.6)
        ));
        return expectedResponse;
    }

    @Test
    void shouldThrowExceptionWhenApiFails() {

        double lat = 0;
        double lon = 0;

        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=0.0&longitude=0.0&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration&hourly=surface_pressure";

        mockRestServiceServer.expect(requestTo(apiUrl)).andRespond(withServiceUnavailable());
        assertThatExceptionOfType(FailedToGetDataException.class).isThrownBy(() -> openMeteoClient.getWeatherData(lat, lon));
    }
}