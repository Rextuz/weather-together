package com.rextuz.weathertogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Place text edit
        final EditText editText = (EditText) findViewById(R.id.editText);

        // Get weather button
        Button getWeatherButton = (Button) findViewById(R.id.button);
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = editText.getText().toString();

                // Get weather from Yahoo and show it
                WeatherServiceInterface yahooWeather = new YahooWeather();
                WeatherEntity weather = yahooWeather.getCurrentWeather(place);
                ((TextView) findViewById(R.id.city)).setText(weather.getCity());
                ((TextView) findViewById(R.id.temperature)).setText(weather.getTemperature() + weather.getTemperatureUnit());
                String countryRegion = weather.getCountry();
                if (!weather.getRegion().isEmpty())
                    countryRegion += ", " + weather.getRegion();
                ((TextView) findViewById(R.id.textView)).setText(countryRegion);
                ((TextView) findViewById(R.id.humanity_value)).setText(weather.getHumidity() + "%");
                ((TextView) findViewById(R.id.windspeed_value)).setText(weather.getSpeed() + " " + weather.getSpeedUnit());
                ((TextView) findViewById(R.id.pressure_value)).setText(weather.getPressure() + " " + weather.getPressureUnit());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

    }
}
