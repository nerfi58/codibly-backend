package io.github.nerfi58.codiblybackend.dtos;

import java.time.LocalDate;

public record WeatherResponse(
        LocalDate day,
        int weatherCode,
        float tempMax,
        float tempMin,
        float estimatedEnergy) {

}
