package io.github.nerfi58.codiblybackend.services;

import io.github.nerfi58.codiblybackend.dtos.WeatherResponse;
import io.github.nerfi58.codiblybackend.exceptions.FailedToGetDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServiceUnavailable;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(WeatherService.class)
class WeatherServiceTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    WeatherService weatherService;

    String serverResponse = """
            {
              "latitude": 0,
              "longitude": 0,
              "generationtime_ms": 0.0751018524169922,
              "utc_offset_seconds": 0,
              "timezone": "GMT",
              "timezone_abbreviation": "GMT",
              "elevation": 0,
              "daily_units": {
                "time": "iso8601",
                "weather_code": "wmo code",
                "temperature_2m_max": "°C",
                "temperature_2m_min": "°C",
                "sunshine_duration": "s"
              },
              "daily": {
                "time": [
                  "2024-05-03",
                  "2024-05-04",
                  "2024-05-05",
                  "2024-05-06",
                  "2024-05-07",
                  "2024-05-08",
                  "2024-05-09"
                ],
                "weather_code": [95, 95, 95, 80, 95, 95, 95],
                "temperature_2m_max": [29.7, 29.5, 29.1, 29, 28.9, 29, 29.5],
                "temperature_2m_min": [28.2, 28.5, 28.5, 28.5, 28.6, 28.5, 28.3],
                "sunshine_duration": [31555.26, 34558.61, 40637.59, 37119.05, 41106.22, 7630.89, 188.15]
              }
            }
            """;

    String requestUrl = "https://api.open-meteo.com/v1/forecast?latitude=0.0&longitude=0.0&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration";

    @Test
    void shouldReturnWeatherForNext7DaysForGivenCoordinates() {
        //given
        double lat = 0;
        double lon = 0;

        //when
        server.expect(requestTo(requestUrl)).andRespond(withSuccess(serverResponse, MediaType.APPLICATION_JSON));
        List<WeatherResponse> weather = weatherService.getWeather(lat, lon);

        //then
        assertThat(weather.size()).isEqualTo(7);
    }

    @Test
    void shouldThrowExceptionServerErrorWhenExternalServiceIsUnavailable() {
        //given
        //when
        server.expect(requestTo(requestUrl)).andRespond(withServiceUnavailable());
        //then
        assertThatExceptionOfType(HttpServerErrorException.class)
                .isThrownBy(() -> weatherService.getWeather(0, 0));
    }

    @Test
    void shouldThrowExceptionServiceUnavailableWhenDataRetrievedIsEmpty() {
        //given
        //when
        server.expect(requestTo(requestUrl)).andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        //then
        assertThatExceptionOfType(FailedToGetDataException.class)
                .isThrownBy(() -> weatherService.getWeather(0, 0));
    }
}