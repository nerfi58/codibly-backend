package com.example.codiblybackend.service;

import com.example.codiblybackend.client.OpenMeteoClient;
import com.example.codiblybackend.dto.DailyWeatherDto;
import com.example.codiblybackend.dto.OpenMeteoWeatherDto;
import com.example.codiblybackend.dto.WeeklyWeatherSummaryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private OpenMeteoClient openMeteoClient;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(openMeteoClient);
    }

    @Test
    void shouldReturnWeatherForecast() {

        double lat = 0;
        double lon = 0;

        OpenMeteoWeatherDto mockWeatherData = new OpenMeteoWeatherDto();

        mockWeatherData.setDaily(new OpenMeteoWeatherDto.DailyDto(
                List.of(LocalDate.parse("2024-11-23"), LocalDate.parse("2024-11-24")),
                List.of(51, 53),
                List.of(2.0, 3.6),
                List.of(12.4, 13.2),
                List.of(1418.61, 17120.99)
        ));

        when(openMeteoClient.getWeatherData(lat, lon)).thenReturn(mockWeatherData);
        List<DailyWeatherDto> result = weatherService.getWeatherForecast(lat, lon);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        DailyWeatherDto day1 = result.get(0);
        assertThat(day1.getDate()).isEqualTo("2024-11-23");
        assertThat(day1.getWeatherCode()).isEqualTo(51);
        assertThat(day1.getMinTemperature()).isCloseTo(2.0, offset(0.01));
        assertThat(day1.getMaxTemperature()).isCloseTo(12.4, offset(0.01));
        assertThat(day1.getEnergy()).isCloseTo(0.197, offset(0.001));

        DailyWeatherDto day2 = result.get(1);
        assertThat(day2.getDate()).isEqualTo("2024-11-24");
        assertThat(day2.getWeatherCode()).isEqualTo(53);
        assertThat(day2.getMinTemperature()).isCloseTo(3.6, offset(0.01));
        assertThat(day2.getMaxTemperature()).isCloseTo(13.2, offset(0.01));
        assertThat(day2.getEnergy()).isCloseTo(2.378, offset(0.001));
    }

    @Test
    void shouldReturnWeeklySummary() {

        double lat = 0;
        double lon = 0;

        OpenMeteoWeatherDto mockWeatherData = new OpenMeteoWeatherDto();
        mockWeatherData.setDaily(new OpenMeteoWeatherDto.DailyDto(
                List.of(LocalDate.parse("2024-11-23"), LocalDate.parse("2024-11-24")),
                List.of(51, 53),
                List.of(2.0, 3.6),
                List.of(12.4, 13.2),
                List.of(1418.61, 17120.99)
        ));
        mockWeatherData.setHourly(new OpenMeteoWeatherDto.HourlyDto(
                List.of(LocalDateTime.parse("2024-11-24T00:00"), LocalDateTime.parse("2024-11-24T01:00"), LocalDateTime.parse("2024-11-24T02:00")),
                List.of(1007.3, 1006.7, 1005.6)
        ));

        when(openMeteoClient.getWeatherData(lat, lon)).thenReturn(mockWeatherData);
        WeeklyWeatherSummaryDto weeklySummary = weatherService.getWeeklyWeatherSummary(lat, lon);

        assertThat(weeklySummary).isNotNull();
        assertThat(weeklySummary.getMinTemperature()).isCloseTo(2.0, offset(0.01));
        assertThat(weeklySummary.getMaxTemperature()).isCloseTo(13.2, offset(0.01));
        assertThat(weeklySummary.getAverageSunshineDuration()).isCloseTo(9269.8, offset(0.01));
        assertThat(weeklySummary.getAveragePressure()).isCloseTo(1006.53, offset(0.01));
        assertThat(weeklySummary.getWeatherSummary()).isEqualTo("With precipitation");
    }
}
