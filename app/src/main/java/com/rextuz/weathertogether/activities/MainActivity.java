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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rextuz.weathertogether.Enitites.WeatherEntity;
import com.rextuz.weathertogether.Enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.R;
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
        final EditText editTextPlace = (EditText) findViewById(R.id.editText);

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
                String units = sf.getString("temperature_units", "C");

                // Create services
                List<WeatherServiceInterface> services = new ArrayList<>();
                if (sf.getBoolean("yahoo", true))
                    services.add(new YahooWeather());
                if (sf.getBoolean("openweather", true))
                    services.add(new OpenWeatherMap());

                // Get weather
                List<WeatherEntity> entities = new ArrayList<>();
                for (WeatherServiceInterface service : services) {
                    WeatherEntity entity = service.getCurrentWeather(place);
                    if (entity != null)
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
                String temperature = units;
                if (!units.equals("K"))
                    temperature = "°" + temperature;

                // Fill data
                for (int i = 0; i < entities.size(); i++) {
                    WeatherEntity entity = entities.get(i);
                    if (entity != null) {
                        View layout = layouts.get(i);
                        ((TextView) layout.findViewById(R.id.city)).setText(entity.getCity());
                        ((TextView) layout.findViewById(R.id.temperature)).setText(entity.getTemperature(units) + temperature);

                        String countryRegion = entity.getCountry();
                        if (entity.getRegion() != null)
                            if (!entity.getRegion().isEmpty())
                                countryRegion += ", " + entity.getRegion();
                        ((TextView) layout.findViewById(R.id.textView)).setText(countryRegion);
                        ((TextView) layout.findViewById(R.id.humanity_value)).setText(entity.getHumidity() + "%");
                        ((TextView) layout.findViewById(R.id.windspeed_value)).setText(entity.getSpeed("m/s") + " " + "m/s");
                        ((TextView) layout.findViewById(R.id.pressure_value)).setText(entity.getPressure("mmHg") + " " + "mmHg");
                    } else {
                        entities.remove(i);
                    }
                }

                // TODO: Usless data invisibility. Loading animations.
                findViewById(R.id.info_layout).setVisibility(View.VISIBLE);

                /*
                List<ShortWeatherEntity> list = yahooWeather.getWeatherForecast(place);

                WeatherServiceInterface openWeatherMap = new OpenWeatherMap();
                WeatherEntity weather2 = openWeatherMap.getCurrentWeather(place);
                List<ShortWeatherEntity> list2 = openWeatherMap.getWeatherForecast(place);
                */
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
        EditText editTextPlace = (EditText) findViewById(R.id.editText);
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
