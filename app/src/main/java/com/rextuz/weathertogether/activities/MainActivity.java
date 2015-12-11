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

import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.services.OpenWeatherMap;
import com.rextuz.weathertogether.services.WeatherServiceInterface;
import com.rextuz.weathertogether.services.WorldWeatherOnline;
import com.rextuz.weathertogether.services.YahooWeather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<View> layouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide null weather
        findViewById(R.id.info_layout).setVisibility(View.INVISIBLE);

        // Place text edit
        final EditText editTextPlace = (EditText) findViewById(R.id.editPlace);

        // Get weather button
        Button getWeatherButton = (Button) findViewById(R.id.button);
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete previous layouts
                for (View layout : layouts)
                    ((ViewManager) layout.getParent()).removeView(layout);
                layouts.clear();

                // Get city from text view
                String place = editTextPlace.getText().toString();

                // Get temperature units
                SharedPreferences sf = getSharedPreferences("com.rextuz.weathertogether", Context.MODE_PRIVATE);
                String temperatureUnit = sf.getString("temperature_units", "C");
                String speedUnit = sf.getString("speed_units", "m/s");
                String pressureUnit = sf.getString("pressure_units", "mmHg");

                // Create services
                List<WeatherServiceInterface> services = new ArrayList<>();
                if (sf.getBoolean("yahoo", true))
                    services.add(new YahooWeather());
                if (sf.getBoolean("openweather", true))
                    services.add(new OpenWeatherMap());
                if (sf.getBoolean("worldweather", true))
                    services.add(new WorldWeatherOnline());

                // Get weather
                List<WeatherEntity> entities = new ArrayList<>();
                for (WeatherServiceInterface service : services) {
                    WeatherEntity entity = service.getCurrentWeather(place);
                    entities.add(entity);
                }

                // Add layouts
                LayoutInflater layoutInflater = getLayoutInflater();
                for (int i = 0; i < entities.size(); i++) {
                    View serviceLayout = layoutInflater.inflate(R.layout.weather_output, null, false);
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.services);
                    linearLayout.addView(serviceLayout);
                    layouts.add(serviceLayout);
                }

                // Temperature
                String temperature = temperatureUnit;
                if (!temperatureUnit.equals("K"))
                    temperature = "°" + temperature;

                // Fill data
                for (int i = 0; i < entities.size(); i++) {
                    WeatherEntity entity = entities.get(i);
                    View layout = layouts.get(i);
                    TextView serviceName = (TextView) layout.findViewById(R.id.service_name);
                    serviceName.setText(entity.getServiceName());
                    if (!entity.isNodata()) {
                        ((TextView) layout.findViewById(R.id.city)).setText(entity.getCity());
                        ((TextView) layout.findViewById(R.id.temperature)).setText(entity.getTemperature(temperatureUnit) + temperature);

                        String countryRegion = entity.getCountry();
                        if (entity.getRegion() != null)
                            if (!entity.getRegion().isEmpty())
                                countryRegion += ", " + entity.getRegion();
                        ((TextView) layout.findViewById(R.id.textView)).setText(countryRegion);
                        ((TextView) layout.findViewById(R.id.humidity_value)).setText(entity.getHumidity() + "%");
                        ((TextView) layout.findViewById(R.id.windspeed_value)).setText(entity.getSpeed(speedUnit) + " " + speedUnit);
                        ((TextView) layout.findViewById(R.id.winddirection_value)).setText(entity.getDirection());
                        ((TextView) layout.findViewById(R.id.pressure_value)).setText(entity.getPressure(pressureUnit) + " " + pressureUnit);
                        ((TextView) layout.findViewById(R.id.forecast_condition_value)).setText(entity.getText());
                        parseText(entity.getText(), layout, R.id.condition_image);

                        // Add forecast
                        LinearLayout main = (LinearLayout) layout.findViewById(R.id.weather_output_main);
                        List<ShortWeatherEntity> forecast = services.get(i).getWeatherForecast(place);
                        while (!forecast.isEmpty()) {
                            View forecastLayoutEntry = layoutInflater.inflate(R.layout.forecast_entry, null, false);
                            main.addView(forecastLayoutEntry);
                            ShortWeatherEntity forecastEntity = forecast.remove(0);
                            ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_date)).setText(forecastEntity.getDate());
                            String forecastTemperature = temperatureUnit;
                            if (!temperatureUnit.equals("K"))
                                forecastTemperature = "°" + forecastTemperature;
                            ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_hi)).setText(forecastEntity.getHigh(temperatureUnit) + forecastTemperature);
                            ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_lo)).setText(forecastEntity.getLow(temperatureUnit) + forecastTemperature);
                            String conditionText = forecastEntity.getText();
                            ((TextView) forecastLayoutEntry.findViewById(R.id.forecast_condition_value)).setText(conditionText);
                            parseText(conditionText, forecastLayoutEntry, R.id.forecast_condition_image);
                        }
                    } else {
                        layout.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.weather_output).setVisibility(View.GONE);
                    }
                }
                findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            }
        });
    }

    private void parseText(String text, View layout, int id) {
        text = text.toLowerCase();
        ImageView image = (ImageView) layout.findViewById(id);
        if (text.contains("snow"))
            image.setImageResource(R.drawable.snow);
        else if (text.contains("storm"))
            image.setImageResource(R.drawable.storm);
        else if (text.contains("sun") || text.contains("fair") || text.contains("clear"))
            image.setImageResource(R.drawable.suny);
        else if (text.contains("cloud") || text.contains("overcast"))
            image.setImageResource(R.drawable.windy);
        else if (text.contains("rain"))
            image.setImageResource(R.drawable.rain);
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
        EditText editTextPlace = (EditText) findViewById(R.id.editPlace);
        Button getWeatherButton = (Button) findViewById(R.id.button);
        if (!editTextPlace.getText().toString().isEmpty())
            getWeatherButton.callOnClick();
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
