package com.rextuz.weathertogether;

public class WeatherEntity {
    //location
    private String city;
    private String country;
    private String region;

    //wind
    private int direction;
    private int speed;

    //atmosphere
    private int humidity;
    private int pressure;

    //astronomy
    private String sunrise;
    private String sunset;

    //condition
    private String date;
    private int temperature;
    private String text;

    public WeatherEntity(String city, String country, String region, int direction, int speed, int humidity, int pressure, String sunrise, String sunset, String date, int temperature, String text) {
        this.city = city;
        this.country = country;
        this.region = region;
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

    public String getDirection() {
        String windDirection;
        if (((direction >= 0) && (direction <= 11.25)) || ((direction > 348.75) && (direction <= 360)));
            windDirection = "S";
        if ((direction > 11.25) && (direction <= 33.75))
            windDirection = "SSW";
        if ((direction > 33.75) && (direction <= 56.25))
            windDirection = "SW";
        if ((direction > 56.25) && (direction <= 78.75))
            windDirection = "WSW";
        if ((direction > 78.75) && (direction <= 101.25))
            windDirection = "W";
        if ((direction > 101.25) && (direction <= 123.75))
            windDirection = "WNW";
        if ((direction > 123.75) && (direction <= 146.25))
            windDirection = "NW";
        if ((direction > 146.25) && (direction <= 168.75))
            windDirection = "NNW";
        if ((direction > 168.75) && (direction <= 191.25))
            windDirection = "N";
        if ((direction > 191.25) && (direction <= 213.75))
            windDirection = "NNE";
        if ((direction > 213.75) && (direction <= 236.25))
            windDirection = "NE";
        if ((direction > 236.25) && (direction <= 258.75))
            windDirection = "ENE";
        if ((direction > 258.75) && (direction <= 281.25))
            windDirection = "E";
        if ((direction > 281.25) && (direction <= 303.75))
            windDirection = "ESE";
        if ((direction > 303.75) && (direction <= 326.25))
            windDirection = "SE";
        if ((direction > 326.25) && (direction <= 348.75))
            windDirection = "SSE";
        return windDirection;
    }

    public int getSpeed(String unit) {
        switch(unit) {
            //Kilometers per hour
            case "km/h":
                return speed;
            //Miles per hour
            case "mph":
                return (int) Math.round((double) speed * 0.621371);
            //Meters per second
            case "m/s":
                return (int) Math.round((double) speed * 1000 / 3600);
            default:
                return speed;
        }
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure(String unit) {
        switch(unit) {
            //Millibars
            case "mb":
                return pressure;
            //Inches of mercury
            case "in":
                return (int) Math.round((double) pressure * 0.02953);
            //Hectopascals(same as mb)
            case "hpa":
                return pressure;
            //Millimeters of mercury
            case "mmHg":
                return (int) Math.round((double) pressure * 0.750062);
            default:
                return pressure;
        }
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

    public int getTemperature(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return temperature;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) temperature * 9 / 5) + 32;
            //Kelvin
            case "K":
                return temperature + 273;
            default:
                return temperature;
        }
    }

    public String getText() {
        return text;
    }
}
