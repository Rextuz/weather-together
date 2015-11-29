package com.rextuz.weathertogether.services;

import android.net.Uri;

import com.rextuz.weathertogether.Enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.Enitites.WeatherEntity;
import com.rextuz.weathertogether.misc.WeatherTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorldWeatherOnline implements WeatherServiceInterface {
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
            String endpoint = String.format("http://api.worldweatheronline.com/free/v2/weather.ashx?key=7beb0ffc04cd0825e2dd7da093fd8&q=%s&num_of_days=1&tp=24&format=json", Uri.encode(place));
            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);
            data = data.optJSONObject("data");

            //location
            String location = data.optJSONArray("request").optJSONObject(0).optString("query");
            String[] split = location.split(", ");
            city = split[0];
            country = split[1];
            region = null;

            //wind
            direction = data.optJSONArray("current_condition").optJSONObject(0).optInt("winddirDegree");
            speed = data.optJSONArray("current_condition").optJSONObject(0).optInt("windspeedKmph");

            //atmosphere
            humidity = data.optJSONArray("current_condition").optJSONObject(0).optInt("humidity");
            pressure = data.optJSONArray("current_condition").optJSONObject(0).optInt("pressure");

            //astronomy
            String string = data.optJSONArray("weather").optJSONObject(0).optJSONArray("astronomy").optJSONObject(0).optString("sunrise");
            DateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date time = format.parse(string);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            sunrise = sdf.format(time);
            string = data.optJSONArray("weather").optJSONObject(0).optJSONArray("astronomy").optJSONObject(0).optString("sunset");
            time = format.parse(string);
            sunset = sdf.format(time);

            //condition
            string = data.optJSONArray("weather").optJSONObject(0).optString("date");
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            time = format.parse(string);
            sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            date = sdf.format(time);
            temperature = data.optJSONArray("current_condition").optJSONObject(0).optInt("temp_C");
            text = data.optJSONArray("current_condition").optJSONObject(0).optJSONArray("weatherDesc").optJSONObject(0).optString("value");

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
            String endpoint = String.format("http://api.worldweatheronline.com/free/v2/weather.ashx?key=7beb0ffc04cd0825e2dd7da093fd8&q=%s&num_of_days=5&tp=24&cc=no&format=json", Uri.encode(place));
            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);
            data = data.optJSONObject("data");

            //location
            String location = data.optJSONArray("request").optJSONObject(0).optString("query");
            String[] split = location.split(", ");
            city = split[0];
            country = split[1];
            region = null;

            //forecast
            JSONArray forecast = data.optJSONArray("weather");
            for (int i = 0; i < forecast.length(); i++) {
                JSONObject day = forecast.optJSONObject(i);
                String string = day.optString("date");;
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date time = format.parse(string);
                SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                date = sdf.format(time);
                text = day.optJSONArray("hourly").optJSONObject(0).optJSONArray("weatherDesc").optJSONObject(0).optString("value");
                high = day.optInt("maxtempC");
                low = day.optInt("mintempC");

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

