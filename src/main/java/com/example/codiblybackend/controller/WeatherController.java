package com.example.codiblybackend.controller;

import com.example.codiblybackend.dto.DailyWeatherDto;
import com.example.codiblybackend.dto.WeeklyWeatherSummaryDto;
import com.example.codiblybackend.service.WeatherService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
@Validated
@RequiredArgsConstructor
@CrossOrigin("https://codibly-frontend.onrender.com")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<DailyWeatherDto>> getWeather(
            @RequestParam("lat") @Min(value = -90, message = "{lat.min}") @Max(value = 90, message = "{lat.max}") double lat,
            @RequestParam("lon") @Min(value = -180, message = "{lon.min}") @Max(value = 180, message = "{lon.max}") double lon) {

        return ResponseEntity.ok().body(weatherService.getWeatherForecast(lat, lon));
    }

    @GetMapping("/weekly-summary")
    public ResponseEntity<WeeklyWeatherSummaryDto> getWeeklyWeatherSummary(
            @RequestParam("lat") @Min(value = -90, message = "{lat.min}") @Max(value = 90, message = "{lat.max}") double lat,
            @RequestParam("lon") @Min(value = -180, message = "{lon.min}") @Max(value = 180, message = "{lon.max}") double lon) {

        return ResponseEntity.ok().body(weatherService.getWeeklyWeatherSummary(lat, lon));
    }
}
