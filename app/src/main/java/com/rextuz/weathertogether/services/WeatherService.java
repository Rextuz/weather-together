package com.rextuz.weathertogether.services;

import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;

import java.util.List;

public abstract class WeatherService {
    // All data
    String result;
    String serviceName;

    // Location
    String city;
    String country;
    String region;

    // Wind
    int direction;
    int speed;

    // Atmosphere
    int humidity;
    int pressure;

    // Astronomy
    String sunrise;
    String sunset;

    // Condition
    String date;
    int temperature;
    String text;

    // Forecast
    int high;
    int low;

    abstract public WeatherEntity getCurrentWeather(String place);

    abstract public List<ShortWeatherEntity> getWeatherForecast(String place);

    public WeatherService(String serviceName) {
        this.serviceName = serviceName;
    }
}
