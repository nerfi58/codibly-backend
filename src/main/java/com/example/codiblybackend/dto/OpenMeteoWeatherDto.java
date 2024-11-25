package com.example.codiblybackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OpenMeteoWeatherDto {

    @JsonProperty("daily")
    private DailyDto daily;

    @JsonProperty("hourly")
    private HourlyDto hourly;

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class DailyDto {
        @JsonProperty("time")
        private List<LocalDate> date;

        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        @JsonProperty("temperature_2m_min")
        private List<Double> minTemperature;

        @JsonProperty("temperature_2m_max")
        private List<Double> maxTemperature;

        @JsonProperty("sunshine_duration")
        private List<Double> sunshineDuration;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class HourlyDto {
        @JsonProperty("time")
        private List<LocalDateTime> dateTime;

        @JsonProperty("surface_pressure")
        private List<Double> pressure;
    }
}
