package com.rextuz.weathertogether;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class YahooWeather implements WeatherServiceInterface {

    String result;

    @Override
    public WeatherEntity getCurrentWeather(final String place) {
        try {
            String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", place);
            String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

            result = new WeatherTask(endpoint).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO: parse result and fill the class with data
        return new WeatherEntity(null, null, 0, 0, null);
    }


}
