package com.rextuz.weathertogether.instrumentaltests;

import android.support.test.runner.AndroidJUnit4;

import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.services.OpenWeatherMap;
import com.rextuz.weathertogether.services.WeatherService;
import com.rextuz.weathertogether.services.WorldWeatherOnline;
import com.rextuz.weathertogether.services.YahooWeather;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ServicesAndroidUnitTest {

    final String PLACE = "Moscow";

    public List<WeatherService> services;
    public List<WeatherEntity> entities;
    public List<List<ShortWeatherEntity>> forecasts;

    @Before
    public void initialize() {
        services = new ArrayList<>();
        entities = new ArrayList<>();
        forecasts = new ArrayList<>();

        services.add(new YahooWeather());
        services.add(new OpenWeatherMap());
        services.add(new WorldWeatherOnline());

        connect();
    }

    private void connect() {
        for (WeatherService service : services) {
            WeatherEntity entity = service.getCurrentWeather(PLACE);
            Assert.assertNotNull(entity);
            entities.add(entity);
            List<ShortWeatherEntity> forecast = service.getWeatherForecast(PLACE);
            forecasts.add(forecast);
        }
    }

    @Test
    public void checkParseWeather() {
        Assert.assertFalse(entities.isEmpty());
        for (WeatherEntity entity : entities) {
            Assert.assertNotNull(entity.getCity());
            Assert.assertNotNull(entity.getCountry());
            Assert.assertNotNull(entity.getDate());
            Assert.assertNotNull(entity.getDirection());
            Assert.assertNotNull(entity.getSpeed("m/s"));
            Assert.assertNotNull(entity.getHumidity());
            Assert.assertNotNull(entity.getPressure("mmHg"));
            Assert.assertNotNull(entity.getServiceName());
            Assert.assertNotNull(entity.getSunrise());
            Assert.assertNotNull(entity.getSunset());
            Assert.assertNotNull(entity.getTemperature("C"));
            Assert.assertNotNull(entity.getText());
        }
    }

    @Test
    public void checkParseForecast() {
        Assert.assertFalse(forecasts.isEmpty());
        for (List<ShortWeatherEntity> forecast : forecasts)
            for (ShortWeatherEntity shortWeatherEntity : forecast) {
                Assert.assertNotNull(shortWeatherEntity.getCity());
                Assert.assertNotNull(shortWeatherEntity.getText());
                Assert.assertNotNull(shortWeatherEntity.getCountry());
                Assert.assertNotNull(shortWeatherEntity.getDate());
                Assert.assertNotNull(shortWeatherEntity.getHigh("C"));
                Assert.assertNotNull(shortWeatherEntity.getLow("C"));
            }
    }
}
