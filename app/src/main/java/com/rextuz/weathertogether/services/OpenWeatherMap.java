package com.rextuz.weathertogether.services;

import com.rextuz.weathertogether.Enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.Enitites.WeatherEntity;
import com.rextuz.weathertogether.misc.WeatherTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMap implements WeatherServiceInterface {
    //all data
    private String result;

    @Override
    public WeatherEntity getCurrentWeather(final String place) {
        //location
        String city;
        String country;
        String region;

        //wind
        int direction;
        int speed;

        //atmosphere
        int humidity;
        int pressure;

        //astronomy
        String sunrise;
        String sunset;

        //condition
        String date;
        int temperature;
        String text;

        try {
            String endpoint = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=20b5ef928730077e4626a33fbdf5e355", place);
            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);

            //location
            city = data.optString("name");
            country = data.optJSONObject("sys").optString("country");
            region = null;

            //wind
            direction = (int) Math.round(data.optJSONObject("wind").optDouble("deg"));
            speed = (int) Math.round(data.optJSONObject("wind").optDouble("speed") * 3600 / 1000);

            //atmosphere
            humidity = data.optJSONObject("main").optInt("humidity");
            pressure = (int) Math.round(data.optJSONObject("main").optDouble("pressure"));

            //astronomy
            sunrise = data.optJSONObject("sys").optString("sunrise");
            sunset = data.optJSONObject("sys").optString("sunset");

            //condition
            date = data.optString("dt");
            temperature = (int) Math.round(data.optJSONObject("main").optDouble("temp"));
            text = data.optJSONArray("weather").optJSONObject(0).optString("main");

            /*
            System.out.println(result);
            System.out.println(city);
            System.out.println(country);
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

            return new WeatherEntity(city, country, region, direction, speed, humidity, pressure, sunrise, sunset, date, temperature, text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ShortWeatherEntity> getWeatherForecast(final String place) {
        //location
        String city;
        String country;
        String region;

        //forecast
        String date;
        String text;
        int high;
        int low;

        //list with forecast
        List<ShortWeatherEntity> list = new ArrayList<ShortWeatherEntity>();

        try {
            String endpoint = String.format("http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric&cnt=5&appid=20b5ef928730077e4626a33fbdf5e355", place);
            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);

            //location
            city = data.optJSONObject("city").optString("name");
            country = data.optJSONObject("city").optString("country");
            region = null;

            //forecast
            JSONArray forecast = data.optJSONArray("list");
            for (int i = 0; i < forecast.length(); i++) {
                JSONObject day = forecast.optJSONObject(i);
                date = day.optString("dt");
                text = day.optJSONArray("weather").optJSONObject(0).optString("main");
                high = (int) Math.round(day.optJSONObject("temp").optDouble("max"));
                low = (int) Math.round(day.optJSONObject("temp").optDouble("min"));

                /*
                System.out.println(i + " " + city);
                System.out.println(i + " " + country);
                System.out.println(i + " " + region);
                System.out.println(i + " " + date);
                System.out.println(i + " " + text);
                System.out.println(i + " " + high);
                System.out.println(i + " " + low);
                */

                list.add(i, new ShortWeatherEntity(city, country, region, date, text, high, low));
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
