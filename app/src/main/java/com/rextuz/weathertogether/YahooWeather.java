package com.rextuz.weathertogether;

import android.net.Uri;

import org.json.JSONObject;

public class YahooWeather implements WeatherServiceInterface {
    //all data
    private String result;

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

    @Override
    public WeatherEntity getCurrentWeather(final String place) {
        try {
            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u = 'c'", place);
            String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);
            JSONObject channel = data.optJSONObject("query").optJSONObject("results").optJSONObject("channel");

            //location
            JSONObject location = channel.optJSONObject("location");
            city = location.optString("city");
            country = location.optString("country");
            region = location.optString("region");

            //units
            JSONObject units = channel.optJSONObject("units");
            pressureUnit = units.getString("pressure");
            speedUnit = units.getString("speed");
            temperatureUnit = units.getString("temperature");

            //wind
            JSONObject wind = channel.optJSONObject("wind");
            direction = wind.optInt("direction");
            speed = wind.optInt("speed");

            //atmosphere
            JSONObject atmosphere = channel.optJSONObject("atmosphere");
            humidity = atmosphere.optInt("humidity");
            pressure = atmosphere.optInt("pressure");

            //astronomy
            JSONObject astronomy = channel.optJSONObject("astronomy");
            sunrise = astronomy.optString("sunrise");//Need change to Date
            sunset = astronomy.optString("sunset");//Need change to Date

            //condition
            JSONObject condition = channel.optJSONObject("item").optJSONObject("condition");
            date = condition.optString("date");//Need change to Date
            temperature = condition.optInt("temp");
            text = condition.optString("text");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        System.out.println(result);

        System.out.println(city);
        System.out.println(country);
        System.out.println(region);
        System.out.println(pressureUnit);
        System.out.println(speedUnit);
        System.out.println(temperatureUnit);
        System.out.println(direction);
        System.out.println(speed);
        System.out.println(humidity);
        System.out.println(pressure);
        System.out.println(sunrise);
        System.out.println(sunset);
        System.out.println(date);
        System.out.println(temperature);
        System.out.println(text);
        */

        return new WeatherEntity(city, country, region, pressureUnit, speedUnit, temperatureUnit, direction, speed, humidity, pressure, sunrise, sunset, date, temperature, text);
    }
}
