package com.rextuz.weathertogether.enitites;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.activities.MainActivity;
import com.rextuz.weathertogether.managers.ImageManager;

public class WeatherEntity {
    // Service name
    private String serviceName;
    private boolean nodata = false;
    //location
    private String city;
    private String country;
    private String region;
    //wind
    private int direction;
    private int speed;
    //atmosphere
    private int humidity;
    private int pressure;
    //astronomy
    private String sunrise;
    private String sunset;
    //condition
    private String date;
    private int temperature;
    private String text;

    public WeatherEntity(String serviceName) {
        this.serviceName = serviceName;
        nodata = true;
    }

    public WeatherEntity(String serviceName, String city, String country, String region, int direction, int speed, int humidity, int pressure, String sunrise, String sunset, String date, int temperature, String text) {
        this.serviceName = serviceName;
        this.city = city;
        this.country = country;
        this.region = region;
        this.direction = direction;
        this.speed = speed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.temperature = temperature;
        this.text = text;
    }

    public boolean isNodata() {
        return nodata;
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

    public String getDirection() {
        String windDirection = null;
        if (((direction >= 0) && (direction <= 11.25)) || ((direction > 348.75) && (direction <= 360)))
            windDirection = "N";
        if ((direction > 11.25) && (direction <= 33.75))
            windDirection = "NNE";
        if ((direction > 33.75) && (direction <= 56.25))
            windDirection = "NE";
        if ((direction > 56.25) && (direction <= 78.75))
            windDirection = "ENE";
        if ((direction > 78.75) && (direction <= 101.25))
            windDirection = "E";
        if ((direction > 101.25) && (direction <= 123.75))
            windDirection = "ESE";
        if ((direction > 123.75) && (direction <= 146.25))
            windDirection = "SE";
        if ((direction > 146.25) && (direction <= 168.75))
            windDirection = "SSE";
        if ((direction > 168.75) && (direction <= 191.25))
            windDirection = "S";
        if ((direction > 191.25) && (direction <= 213.75))
            windDirection = "SSW";
        if ((direction > 213.75) && (direction <= 236.25))
            windDirection = "SW";
        if ((direction > 236.25) && (direction <= 258.75))
            windDirection = "WSW";
        if ((direction > 258.75) && (direction <= 281.25))
            windDirection = "W";
        if ((direction > 281.25) && (direction <= 303.75))
            windDirection = "WNW";
        if ((direction > 303.75) && (direction <= 326.25))
            windDirection = "NW";
        if ((direction > 326.25) && (direction <= 348.75))
            windDirection = "NNW";
        return windDirection;
    }

    public int getSpeed(String unit) {
        switch (unit) {
            //Kilometers per hour
            case "km/h":
                return speed;
            //Miles per hour
            case "mph":
                return (int) Math.round((double) speed * 0.621371);
            //Meters per second
            case "m/s":
                return (int) Math.round((double) speed * 1000 / 3600);
            default:
                return speed;
        }
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure(String unit) {
        switch (unit) {
            //Millibars
            case "mb":
                return pressure;
            //Inches of mercury
            case "in":
                return (int) Math.round((double) pressure * 0.02953);
            //Hectopascals(same as mb)
            case "hpa":
                return pressure;
            //Millimeters of mercury
            case "mmHg":
                return (int) Math.round((double) pressure * 0.750062);
            default:
                return pressure;
        }
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getDate() {
        return date;
    }

    public int getTemperature(String unit) {
        switch (unit) {
            //Celsius
            case "C":
                return temperature;
            //Fahrenheit
            case "F":
                return (int) Math.round((double) temperature * 9 / 5) + 32;
            //Kelvin
            case "K":
                return temperature + 273;
            default:
                return temperature;
        }
    }

    public String getText() {
        return text;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void fillView(final View layout, final MainActivity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Get preferences
                SharedPreferences sp = activity.sp;

                // Service name
                final TextView serviceName = (TextView) layout.findViewById(R.id.service_name);
                serviceName.setText(getServiceName());

                if (!isNodata())
                // If there is data
                {
                    // City
                    ((TextView) layout.findViewById(R.id.city)).setText(getCity());

                    // Temperature
                    String temperatureUnit = sp.getString("temperature_units", "C");
                    String temperature = temperatureUnit;
                    if (!temperatureUnit.equals("K"))
                        temperature = "°" + temperature;
                    String s = getTemperature(temperatureUnit) + temperature;
                    ((TextView) layout.findViewById(R.id.temperature)).setText(s);

                    // Region
                    String countryRegion = getCountry();
                    if (getRegion() != null)
                        if (!getRegion().isEmpty())
                            countryRegion += ", " + getRegion();
                    ((TextView) layout.findViewById(R.id.textView)).setText(countryRegion);

                    // Humidity
                    s = getHumidity() + "%";
                    ((TextView) layout.findViewById(R.id.humidity_value)).setText(s);

                    // Wind
                    String speedUnit = sp.getString("speed_units", "m/s");
                    s = getSpeed(speedUnit) + " " + speedUnit;
                    ((TextView) layout.findViewById(R.id.windspeed_value)).setText(s);
                    ((TextView) layout.findViewById(R.id.winddirection_value)).setText(getDirection());

                    // Pressure
                    String pressureUnit = sp.getString("pressure_units", "mmHg");
                    s = getPressure(pressureUnit) + " " + pressureUnit;
                    ((TextView) layout.findViewById(R.id.pressure_value)).setText(s);

                    // Condition
                    ((TextView) layout.findViewById(R.id.forecast_condition_value)).setText(getText());
                    ImageView image = (ImageView) layout.findViewById(R.id.condition_image);
                    image.setImageResource(ImageManager.parseText(getText()));
                } else {
                    layout.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                    layout.findViewById(R.id.weather_output).setVisibility(View.GONE);
                }
            }
        });
    }
}
