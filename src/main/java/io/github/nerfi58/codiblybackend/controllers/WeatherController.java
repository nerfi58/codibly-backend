package io.github.nerfi58.codiblybackend.controllers;

import io.github.nerfi58.codiblybackend.dtos.WeatherResponse;
import io.github.nerfi58.codiblybackend.services.WeatherService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
@CrossOrigin("https://codibly-frontend.onrender.com")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    ResponseEntity<List<WeatherResponse>> getWeather(@RequestParam("lat") @Min(-90) @Max(90) double lat,
                                                     @RequestParam("lon") @Min(-180) @Max(180) double lon) {
        return ResponseEntity.ok(weatherService.getWeather(lat, lon));
    }
}
