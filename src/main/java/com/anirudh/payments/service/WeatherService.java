package com.anirudh.payments.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class WeatherService {

    private final RestClient restClient;
    private final String apiKey;

    public WeatherService(RestClient.Builder builder, @Value("${openweathermap.api-key}") String apiKey) {
        this.restClient = builder.baseUrl("https://api.openweathermap.org").build();
        this.apiKey = apiKey;
    }

    // MCP tool
    @McpTool(description = "Gets the current temperature in Celsius for a given city",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public String getTemperature(@McpToolParam(description = "City name, e.g. 'London'") String city) {
        WeatherResponse response = fetchWeatherResponse(city);

        if (response == null || response.main() == null) {
            return "Could not retrieve weather for " + city;
        }
        return "Current temperature in " + city + " is " + response.main().temp() + "°C";
    }

    // Rest Endpoint
    public List<String> getCurrentWeather(String city) {
        WeatherResponse response = fetchWeatherResponse(city);

        if (response == null || response.main() == null) {
            throw new IllegalStateException("Weather data unavailable for " + city);
        }
        return List.of(
                String.valueOf(response.weather.getFirst().description()),
                String.valueOf(response.main.temp()),
                String.valueOf(response.main.feelsLike()),
                String.valueOf(response.main.humidity())
        );
    }

    // Helpers
    private WeatherResponse fetchWeatherResponse(String city) {
        return restClient.get()
                .uri("/data/2.5/weather?q={city}&appid={key}&units=metric", city, apiKey)
                .retrieve()
                .body(WeatherResponse.class);
    }

    record WeatherResponse(List<Weather> weather,
                           Main main) {}

    record Main(double temp,
                @JsonProperty("feels_like") double feelsLike,
                @JsonProperty("temp_min") double tempMin,
                @JsonProperty("temp_max") double tempMax,
                double humidity) {}

    record Weather(String main,
                   String description) {}
}
