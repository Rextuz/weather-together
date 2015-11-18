package com.rextuz.weathertogether;

import java.util.List;

public interface WeatherServiceInterface {
    WeatherEntity getCurrentWeather(String place);
    List<ShortWeatherEntity> getWeatherForecast(String place);
}
