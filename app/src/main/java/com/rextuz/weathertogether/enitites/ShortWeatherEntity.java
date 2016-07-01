package com.rextuz.weathertogether.enitites;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.activities.MainActivity;
import com.rextuz.weathertogether.managers.ImageManager;

public class ShortWeatherEntity {
    //location
    private String city;
    private String country;
    private String region;

    //condition
    private String date;
    private String text;
    private int high;
    private int low;

    public ShortWeatherEntity(String city, String country, String region, String date, String text, int high, int low) {
        this.city = city;
        this.country = country;
        this.region = region;
        this.date = date;
        this.text = text;
        this.high = high;
        this.low = low;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int getHigh(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return high;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) high * 9 / 5) + 32;
            //Kelvin
            case "K":
                return high + 273;
            default:
                return high;
        }
    }

    public int getLow(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return low;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) low * 9 / 5) + 32;
            //Kelvin
            case "K":
                return low + 273;
            default:
                return low;
        }
    }

    public void fillView(final View layout, final MainActivity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Get preferences
                SharedPreferences sp = activity.sp;

                // Date
                ((TextView) layout.findViewById(R.id.forecast_date)).setText(getDate());

                // Temperature
                String temperatureUnit = sp.getString("temperature_units", "C");
                String forecastTemperature = temperatureUnit;
                if (!temperatureUnit.equals("K"))
                    forecastTemperature = "°" + forecastTemperature;
                String s = getHigh(temperatureUnit) + forecastTemperature;
                ((TextView) activity.findViewById(R.id.forecast_hi)).setText(s);
                s = getLow(temperatureUnit) + forecastTemperature;
                ((TextView) layout.findViewById(R.id.forecast_lo)).setText(s);

                // Condition
                String conditionText = getText();
                ((TextView) layout.findViewById(R.id.forecast_condition_value)).setText(conditionText);
                ImageView image = (ImageView) layout.findViewById(R.id.forecast_condition_image);
                image.setImageResource(ImageManager.parseText(conditionText));
            }
        });
    }

}
