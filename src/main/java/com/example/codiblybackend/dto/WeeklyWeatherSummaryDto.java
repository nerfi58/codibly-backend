package com.example.codiblybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeeklyWeatherSummaryDto {

    private double averagePressure;
    private double averageSunshineDuration;
    private double maxTemperature;
    private double minTemperature;
    private String weatherSummary;
}
