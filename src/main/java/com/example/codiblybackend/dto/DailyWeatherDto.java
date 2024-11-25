package com.example.codiblybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DailyWeatherDto {
    private LocalDate date;
    private Integer weatherCode;
    private Double minTemperature;
    private Double maxTemperature;
    private Double energy;
}
