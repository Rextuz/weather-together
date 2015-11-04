package com.rextuz.weathertogether;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WeatherTask extends AsyncTask<Void, Void, String> {

    String request;

    WeatherTask(String task) {
        request = task;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        try {
            URL url = new URL(request);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
