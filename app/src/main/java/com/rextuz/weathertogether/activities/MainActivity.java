package com.rextuz.weathertogether.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.services.OpenWeatherMap;
import com.rextuz.weathertogether.services.WeatherService;
import com.rextuz.weathertogether.services.WorldWeatherOnline;
import com.rextuz.weathertogether.services.YahooWeather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<View> layouts = new ArrayList<>();
    private List<WeatherEntity> entities = new ArrayList<>();
    private String place;
    private SharedPreferences sf;
    private MainActivity activity = this;

    public static int parseText(String text) {
        text = text.toLowerCase();
        if (text.contains("snow"))
            return R.drawable.snow;
        else if (text.contains("storm"))
            return R.drawable.storm;
        else if (text.contains("sun") || text.contains("fair") || text.contains("clear"))
            return R.drawable.suny;
        else if (text.contains("cloud") || text.contains("overcast"))
            return R.drawable.windy;
        else if (text.contains("rain"))
            return R.drawable.rain;
        return R.drawable.windy;
    }

    private List<WeatherService> getServices() {
        List<WeatherService> services = new ArrayList<>();
        if (sf.getBoolean("yahoo", true))
            services.add(new YahooWeather());
        if (sf.getBoolean("openweather", true))
            services.add(new OpenWeatherMap());
        if (sf.getBoolean("worldweather", true))
            services.add(new WorldWeatherOnline());
        return services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sf = getSharedPreferences("com.rextuz.weathertogether", Context.MODE_PRIVATE);

        // AdMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Hide null weather
        findViewById(R.id.info_layout).setVisibility(View.INVISIBLE);

        // Get weather button
        Button getWeatherButton = (Button) findViewById(R.id.go_button);
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.onClick();
                    }
                }).start();
            }
        });
    }

    private void onClick() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Delete previous layouts
                for (View layout : layouts)
                    ((ViewManager) layout.getParent()).removeView(layout);
                layouts.clear();

                // Get city from text view
                EditText editTextPlace = (EditText) findViewById(R.id.editPlace);
                place = editTextPlace.getText().toString();

                TextView loadingText = (TextView) findViewById(R.id.loadingText);
                loadingText.setVisibility(View.VISIBLE);
                Button goButton = (Button) findViewById(R.id.go_button);
                goButton.setVisibility(View.GONE);
            }
        });

        // Get temperature units
        final String temperatureUnit = sf.getString("temperature_units", "C");
        final String speedUnit = sf.getString("speed_units", "m/s");
        final String pressureUnit = sf.getString("pressure_units", "mmHg");

        // Get enabled services
        List<WeatherService> services = getServices();
        int entitiesNumber = services.size();

        // Get weather
        for (WeatherService service : services) {
            entities.add(service.getCurrentWeather(place));
        }

        // Add layouts
        final LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < entitiesNumber; i++) {
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.services);
            final View serviceLayout = layoutInflater.inflate(R.layout.weather_output, linearLayout, false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.addView(serviceLayout);
                }
            });
            layouts.add(serviceLayout);
        }

        // Temperature
        String temperature = temperatureUnit;
        if (!temperatureUnit.equals("K"))
            temperature = "°" + temperature;

        // Fill data
        for (int i = 0; i < entitiesNumber; i++) {
            final WeatherEntity entity = entities.get(i);
            final View layout = layouts.get(i);
            final TextView serviceName = (TextView) layout.findViewById(R.id.service_name);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    serviceName.setText(entity.getServiceName());
                }
            });
            if (!entity.isNodata()) {
                final String finalTemperature = temperature;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s;
                        ((TextView) layout.findViewById(R.id.city)).setText(entity.getCity());
                        s = entity.getTemperature(temperatureUnit) + finalTemperature;
                        ((TextView) layout.findViewById(R.id.temperature)).setText(s);
                        String countryRegion = entity.getCountry();
                        if (entity.getRegion() != null)
                            if (!entity.getRegion().isEmpty())
                                countryRegion += ", " + entity.getRegion();
                        ((TextView) layout.findViewById(R.id.textView)).setText(countryRegion);
                        s = entity.getHumidity() + "%";
                        ((TextView) layout.findViewById(R.id.humidity_value)).setText(s);
                        s = entity.getSpeed(speedUnit) + " " + speedUnit;
                        ((TextView) layout.findViewById(R.id.windspeed_value)).setText(s);
                        ((TextView) layout.findViewById(R.id.winddirection_value)).setText(entity.getDirection());
                        s = entity.getPressure(pressureUnit) + " " + pressureUnit;
                        ((TextView) layout.findViewById(R.id.pressure_value)).setText(s);
                        ((TextView) layout.findViewById(R.id.forecast_condition_value)).setText(entity.getText());
                        {
                            int imageId = parseText(entity.getText());
                            ImageView image = (ImageView) layout.findViewById(R.id.condition_image);
                            image.setImageResource(imageId);
                        }
                    }
                });

                // Add forecast
                final LinearLayout main = (LinearLayout) layout.findViewById(R.id.weather_output_main);
                final List<ShortWeatherEntity> forecast = services.get(i).getWeatherForecast(place);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forecast != null)
                            while (!forecast.isEmpty()) {
                                String s;
                                View forecastLayoutEntry = layoutInflater.inflate(R.layout.forecast_entry, main, false);
                                main.addView(forecastLayoutEntry);
                                ShortWeatherEntity forecastEntity = forecast.remove(0);
                                ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_date)).setText(forecastEntity.getDate());
                                String forecastTemperature = temperatureUnit;
                                if (!temperatureUnit.equals("K"))
                                    forecastTemperature = "°" + forecastTemperature;
                                s = forecastEntity.getHigh(temperatureUnit) + forecastTemperature;
                                ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_hi)).setText(s);
                                s = forecastEntity.getLow(temperatureUnit) + forecastTemperature;
                                ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_lo)).setText(s);
                                String conditionText = forecastEntity.getText();
                                ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_condition_value)).setText(conditionText);
                                {
                                    int imageId = parseText(conditionText);
                                    ImageView image = (ImageView) forecastLayoutEntry.findViewById(R.id.forecast_condition_image);
                                    image.setImageResource(imageId);
                                }
                            }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.weather_output).setVisibility(View.GONE);
                    }
                });
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView loadingText = (TextView) findViewById(R.id.loadingText);
                loadingText.setVisibility(View.GONE);
                Button goButton = (Button) findViewById(R.id.go_button);
                goButton.setVisibility(View.VISIBLE);
                findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the fnu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* EditText editTextPlace = (EditText) findViewById(R.id.editPlace);
        Button getWeatherButton = (Button) findViewById(R.id.go_button);
        if (!editTextPlace.getText().toString().isEmpty())
            getWeatherButton.callOnClick();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }
}
