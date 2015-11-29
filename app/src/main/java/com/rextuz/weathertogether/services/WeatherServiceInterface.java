package com.rextuz.weathertogether.services;

import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.enitites.ShortWeatherEntity;

import java.util.List;

public interface WeatherServiceInterface {
    WeatherEntity getCurrentWeather(String place);
    List<ShortWeatherEntity> getWeatherForecast(String place);
}
