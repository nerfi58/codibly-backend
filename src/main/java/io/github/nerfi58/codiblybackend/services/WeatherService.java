package io.github.nerfi58.codiblybackend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.nerfi58.codiblybackend.dtos.WeatherResponse;
import io.github.nerfi58.codiblybackend.exceptions.FailedToGetDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast";
    private static final float PHOTOVOLTAIC_POWER = 2.5f;
    private static final float PANELS_EFFICIENCY = 0.2f;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public WeatherService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(OPEN_METEO_URL).build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<WeatherResponse> getWeather(double lat, double lon) {

        ResponseEntity<JsonNode> response = restClient.get().uri(
                uriBuilder -> uriBuilder.path("")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration")
                        .build()
        ).retrieve().toEntity(JsonNode.class);

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new FailedToGetDataException("Could not get weather data");
        }

        JsonNode daily = response.getBody().get("daily");

        List<WeatherResponse> weatherResponseList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            WeatherResponse weatherResponse = new WeatherResponse(
                    objectMapper.convertValue(daily.get("time").get(i), LocalDate.class),
                    objectMapper.convertValue(daily.get("weather_code").get(i), Integer.class),
                    objectMapper.convertValue(daily.get("temperature_2m_max").get(i), Float.class),
                    objectMapper.convertValue(daily.get("temperature_2m_min").get(i), Float.class),
                    calculateEnergy(objectMapper.convertValue(daily.get("sunshine_duration").get(i), Float.class))
            );

            weatherResponseList.add(weatherResponse);
        }

        return weatherResponseList;
    }

    private float calculateEnergy(float sunshineDurationInSeconds) {
        return PHOTOVOLTAIC_POWER * (sunshineDurationInSeconds / 3600) * PANELS_EFFICIENCY;
    }

}
