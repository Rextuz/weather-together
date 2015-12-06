package com.rextuz.weathertogether;

import com.rextuz.weathertogether.services.OpenWeatherMap;
import com.rextuz.weathertogether.services.WeatherServiceInterface;
import com.rextuz.weathertogether.services.WorldWeatherOnline;
import com.rextuz.weathertogether.services.YahooWeather;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ServicesTest {

    @Test
    public void initializingService_isCorrect() throws Exception {
        List<WeatherServiceInterface> services = new ArrayList<>();
        services.add(new YahooWeather());
        services.add(new OpenWeatherMap());
        services.add(new WorldWeatherOnline());
        for (WeatherServiceInterface service : services) {
            assertNotNull(service);
        }
    }

    @Test
    public void yahooService_isCorrect() throws Exception {
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u = 'c'", "Moscow");
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", URLEncoder.encode(YQL, "UTF8"));
        assertNotNull(testConnection(endpoint));
    }

    private String testConnection(String task) throws Exception {
        String result = null;
        try {
            URL url = new URL(task);
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
        return result;
    }

}