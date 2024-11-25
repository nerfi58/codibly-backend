package com.example.codiblybackend.service;

import com.example.codiblybackend.client.OpenMeteoClient;
import com.example.codiblybackend.dto.DailyWeatherDto;
import com.example.codiblybackend.dto.OpenMeteoWeatherDto;
import com.example.codiblybackend.dto.WeeklyWeatherSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private static final float PHOTOVOLTAIC_POWER = 2.5f;
    private static final float PANELS_EFFICIENCY = 0.2f;

    private final OpenMeteoClient openMeteoClient;

    public List<DailyWeatherDto> getWeatherForecast(double lat, double lon) {

        OpenMeteoWeatherDto weatherData = openMeteoClient.getWeatherData(lat, lon);
        return mapToDailyWeatherDto(weatherData);
    }

    public WeeklyWeatherSummaryDto getWeeklyWeatherSummary(double lat, double lon) {

        OpenMeteoWeatherDto weatherData = openMeteoClient.getWeatherData(lat, lon);

        double averagePressure = weatherData.getHourly().getPressure().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double averageSunshineDuration = weatherData.getDaily().getSunshineDuration().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxTemperature = weatherData.getDaily().getMaxTemperature().stream().max(Double::compareTo).orElse(0.0);
        double minTemperature = weatherData.getDaily().getMinTemperature().stream().min(Double::compareTo).orElse(0.0);
        String weatherSummary = determineWeatherSummary(weatherData.getDaily().getWeatherCode());

        return new WeeklyWeatherSummaryDto(averagePressure, averageSunshineDuration, maxTemperature, minTemperature, weatherSummary);
    }

    private String determineWeatherSummary(List<Integer> weatherCodes) {
        int precipitationDays = 0;

        for (int weatherCode : weatherCodes) {
            //all precipitation codes
            if (List.of(51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 71, 73, 75, 77, 80, 81, 82, 85, 86, 95, 96, 99).contains(weatherCode)) {
                precipitationDays++;
            }
        }

        return precipitationDays > (weatherCodes.size() / 2) ? "With precipitation" : "No precipitation";
    }

    private List<DailyWeatherDto> mapToDailyWeatherDto(OpenMeteoWeatherDto weatherData) {

        List<LocalDate> dates = weatherData.getDaily().getDate();
        List<Integer> weatherCodes = weatherData.getDaily().getWeatherCode();
        List<Double> minTemps = weatherData.getDaily().getMinTemperature();
        List<Double> maxTemps = weatherData.getDaily().getMaxTemperature();
        List<Double> sunshineDurations = weatherData.getDaily().getSunshineDuration();

        return IntStream.range(0, dates.size())
                .mapToObj(index -> new DailyWeatherDto(
                        dates.get(index),
                        weatherCodes.get(index),
                        minTemps.get(index),
                        maxTemps.get(index),
                        calculateEstimatedEnergy(sunshineDurations.get(index))
                ))
                .toList();
    }

    private double calculateEstimatedEnergy(double sunshineDurationInSeconds) {
        return PHOTOVOLTAIC_POWER * (sunshineDurationInSeconds / 3600) * PANELS_EFFICIENCY;
    }
}
