package com.rextuz.weathertogether;

import java.util.Date;

public class WeatherEntity {
    //location
    private String city;
    private String country;
    private String region;

    //units
    private String pressureUnit;
    private String speedUnit;
    private String temperatureUnit;

    //wind
    private int direction;
    private int speed;

    //atmosphere
    private int humidity;
    private int pressure;

    //astronomy
    private String sunrise;//Need change to Date
    private String sunset;//Need change to Date

    //condition
    private String date;//Need change to Date
    private int temperature;
    private String text;

    public WeatherEntity(String city, String country, String region, String pressureUnit, String speedUnit, String temperatureUnit, int direction, int speed, int humidity, int pressure, String sunrise, String sunset, String date, int temperature, String text) {
        this.city = city;
        this.country = country;
        this.region = region;
        this.pressureUnit = pressureUnit;
        this.speedUnit = speedUnit;
        this.temperatureUnit = temperatureUnit;
        this.direction = direction;
        this.speed = speed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.temperature = temperature;
        this.text = text;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getText() {
        return text;
    }
}
