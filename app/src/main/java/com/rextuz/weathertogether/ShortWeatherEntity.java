package com.rextuz.weathertogether;

public class ShortWeatherEntity {
    //location
    private String city;
    private String country;
    private String region;

    //units
    private String pressureUnit;
    private String speedUnit;
    private String temperatureUnit;

    //condition
    private String date;
    private String text;
    private int high;
    private int low;

    public ShortWeatherEntity(String city, String country, String region, String pressureUnit, String speedUnit, String temperatureUnit, String date, String text, int high, int low) {
        this.city = city;
        this.country = country;
        this.region = region;
        this.pressureUnit = pressureUnit;
        this.speedUnit = speedUnit;
        this.temperatureUnit = temperatureUnit;
        this.date = date;
        this.text = text;
        this.high = high;
        this.low = low;
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

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int getHigh(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return high;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) high * 9 / 5) + 32;
            //Kelvin
            case "K":
                return high + 273;
            default:
                return high;
        }
    }

    public int getLow(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return low;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) low * 9 / 5) + 32;
            //Kelvin
            case "K":
                return low + 273;
            default:
                return low;
        }

    }
}
