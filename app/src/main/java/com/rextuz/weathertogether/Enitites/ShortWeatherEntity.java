package com.rextuz.weathertogether.Enitites;

public class ShortWeatherEntity {
    //location
    private String city;
    private String country;
    private String region;

    //condition
    private String date;
    private String text;
    private int high;
    private int low;

    public ShortWeatherEntity(String city, String country, String region, String date, String text, int high, int low) {
        this.city = city;
        this.country = country;
        this.region = region;
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
