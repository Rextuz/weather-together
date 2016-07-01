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

public class WorldWeatherOnline extends WeatherService {

    public WorldWeatherOnline() {
        super("World Weather Online");
    }

    @Override
    public WeatherEntity getCurrentWeather(final String place) {

        try {
            String endpoint = String.format("http://api.worldweatheronline.com/free/v2/weather.ashx?key=7beb0ffc04cd0825e2dd7da093fd8&q=%s&num_of_days=1&tp=24&format=json", Uri.encode(place));
            result = WeatherTask.getWeather(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);
            data = data.optJSONObject("data");

            // Location
            String location = data.optJSONArray("request").optJSONObject(0).optString("query");
            String[] split = location.split(", ");
            city = split[0];
            country = split[1];
            region = null;

            // Wind
            direction = data.optJSONArray("current_condition").optJSONObject(0).optInt("winddirDegree");
            speed = data.optJSONArray("current_condition").optJSONObject(0).optInt("windspeedKmph");

            // Atmosphere
            humidity = data.optJSONArray("current_condition").optJSONObject(0).optInt("humidity");
            pressure = data.optJSONArray("current_condition").optJSONObject(0).optInt("pressure");

            // Astronomy
            String string = data.optJSONArray("weather").optJSONObject(0).optJSONArray("astronomy").optJSONObject(0).optString("sunrise");
            DateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date time = format.parse(string);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            sunrise = sdf.format(time);
            string = data.optJSONArray("weather").optJSONObject(0).optJSONArray("astronomy").optJSONObject(0).optString("sunset");
            time = format.parse(string);
            sunset = sdf.format(time);

            // Condition
            string = data.optJSONArray("weather").optJSONObject(0).optString("date");
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            time = format.parse(string);
            sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            date = sdf.format(time);
            temperature = data.optJSONArray("current_condition").optJSONObject(0).optInt("temp_C");
            text = data.optJSONArray("current_condition").optJSONObject(0).optJSONArray("weatherDesc").optJSONObject(0).optString("value");

            return new WeatherEntity(serviceName, city, country, region, direction, speed, humidity, pressure, sunrise, sunset, date, temperature, text);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new WeatherEntity(serviceName);
    }

    @Override
    public List<ShortWeatherEntity> getWeatherForecast(final String place) {

        // List with forecast
        List<ShortWeatherEntity> list = new ArrayList<>();

        try {
            String endpoint = String.format("http://api.worldweatheronline.com/free/v2/weather.ashx?key=7beb0ffc04cd0825e2dd7da093fd8&q=%s&num_of_days=5&tp=24&cc=no&format=json", Uri.encode(place));
            // result = new WeatherTask().execute(endpoint).get();
            String request = endpoint;
            URL url = new URL(request);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            result = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject data = new JSONObject(result);
            data = data.optJSONObject("data");

            // Location
            String location = data.optJSONArray("request").optJSONObject(0).optString("query");
            String[] split = location.split(", ");
            city = split[0];
            country = split[1];
            region = null;

            // Forecast
            JSONArray forecast = data.optJSONArray("weather");
            for (int i = 0; i < forecast.length(); i++) {
                JSONObject day = forecast.optJSONObject(i);
                String string = day.optString("date");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date time = format.parse(string);
                SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                date = sdf.format(time);
                text = day.optJSONArray("hourly").optJSONObject(0).optJSONArray("weatherDesc").optJSONObject(0).optString("value");
                high = day.optInt("maxtempC");
                low = day.optInt("mintempC");

                list.add(i, new ShortWeatherEntity(city, country, region, date, text, high, low));
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

