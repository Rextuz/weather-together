package com.rextuz.weathertogether.services;

import android.net.Uri;

import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.misc.WeatherTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YahooWeather extends WeatherService {

    public YahooWeather() {
        super("Yahoo Weather");
    }

    @Override
    public WeatherEntity getCurrentWeather(final String place) {

        try {
            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u = 'c'", place);
            String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
            result = WeatherTask.getWeather(endpoint);
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
            String string = astronomy.optString("sunrise");
            DateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date time = format.parse(string);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            sunrise = sdf.format(time);
            string = astronomy.optString("sunset");
            time = format.parse(string);
            sunset = sdf.format(time);

            //condition
            JSONObject condition = channel.optJSONObject("item").optJSONObject("condition");
            string = condition.optString("date");
            String[] split = string.split(" [0-9]:");
            format = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
            time = format.parse(split[0]);
            sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            date = sdf.format(time);
            temperature = condition.optInt("temp");
            text = condition.optString("text");

            return new WeatherEntity(serviceName, city, country, region, direction, speed, humidity, pressure, sunrise, sunset, date, temperature, text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new WeatherEntity(serviceName);
    }

    @Override
    public List<ShortWeatherEntity> getWeatherForecast(final String place) {

        //list with forecast
        List<ShortWeatherEntity> list = new ArrayList<>();

        try {
            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u = 'c'", place);
            String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
            result = WeatherTask.getWeather(endpoint);
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

            //forecast
            JSONArray forecast = channel.optJSONObject("item").optJSONArray("forecast");
            for (int i = 0; i < forecast.length(); i++) {
                JSONObject day = forecast.optJSONObject(i);
                date = day.optString("date");
                text = day.optString("text");
                high = day.optInt("high");
                low = day.optInt("low");

                list.add(i, new ShortWeatherEntity(city, country, region, date, text, high, low));
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}


