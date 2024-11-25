package com.example.codiblybackend.client;

import com.example.codiblybackend.dto.OpenMeteoWeatherDto;
import com.example.codiblybackend.exception.FailedToGetDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OpenMeteoClient {

    private static final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestClient restClient;

    @Autowired
    public OpenMeteoClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(OPEN_METEO_URL)
                .build();
    }

    public OpenMeteoWeatherDto getWeatherData(double lat, double lon) {
        String url = UriComponentsBuilder.fromUriString("")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration")
                .queryParam("hourly", "surface_pressure")
                .build().toUriString();

        try {
            ResponseEntity<OpenMeteoWeatherDto> response = restClient.get().uri(url).retrieve().toEntity(OpenMeteoWeatherDto.class);
            return response.getBody();
        } catch (HttpServerErrorException e) {
            throw new FailedToGetDataException("Failed to get data from Open Meteo API");
        }
    }
}
