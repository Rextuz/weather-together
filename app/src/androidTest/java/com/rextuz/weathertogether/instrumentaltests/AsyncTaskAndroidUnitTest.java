package com.rextuz.weathertogether.instrumentaltests;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.rextuz.weathertogether.misc.WeatherTask;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AsyncTaskAndroidUnitTest {

    private String request;

    @Before
    public void initialize() {
        String place = "";
        request = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=20b5ef928730077e4626a33fbdf5e355", Uri.encode(place));
    }

    @Test(timeout = 5000)
    public void checkProcess() {
        String result = null;
        try {
            result = new WeatherTask(request).execute().get();
        } catch (Exception e) {
            Log.d("test", e.toString());
        }
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }
}
