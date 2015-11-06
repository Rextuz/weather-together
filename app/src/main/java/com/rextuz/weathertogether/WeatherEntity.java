package com.rextuz.weathertogether;

import java.util.Date;

public class WeatherEntity {
    private String place;
    private Date date;
    private int temperature;
    private int wind_speed;
    private String wind_direction;

    WeatherEntity(String place, Date date, int temperature, int wind_speed, String wind_direction) {
        this.place = place;
        this.date = date;
        this.temperature = temperature;
        this.wind_speed = wind_speed;
        this.wind_direction = wind_direction;
    }

    public String getPlace() {
        return place;
    }

    public Date getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getWind_speed() {
        return wind_speed;
    }

    public String getWind_direction() {
        return wind_direction;
    }
}
