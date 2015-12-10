package com.rextuz.weathertogether.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.rextuz.weathertogether.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sf = getSharedPreferences("com.rextuz.weathertogether", Context.MODE_PRIVATE);

        RadioGroup temperatureRadio = (RadioGroup) findViewById(R.id.temperatureRadio);
        RadioGroup speedRadio = (RadioGroup) findViewById(R.id.speedRadio);
        RadioGroup pressureRadio = (RadioGroup) findViewById(R.id.pressureRadio);
        String temperatureUnit = sf.getString("temperature_units", "C");
        String speedUnit = sf.getString("speed_units", "m/s");
        String pressureUnit = sf.getString("pressure_units", "mmHg");

        switch (temperatureUnit) {
            case "F":
                temperatureRadio.check(R.id.fRadioButton);
                break;
            case "K":
                temperatureRadio.check(R.id.kRadioButton);
                break;
            default:
                temperatureRadio.check(R.id.cRadioButton);
        }

        temperatureRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton btn = (RadioButton) radioGroup.findViewById(id);
                sf.edit().putString("temperature_units", btn.getText().toString()).apply();
            }
        });

        switch (speedUnit) {
            case "km/h":
                speedRadio.check(R.id.kmRadioButton);
                break;
            case "mph":
                speedRadio.check(R.id.mphRadioButton);
                break;
            default:
                speedRadio.check(R.id.mRadioButton);
        }

        speedRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton btn = (RadioButton) radioGroup.findViewById(id);
                sf.edit().putString("speed_units", btn.getText().toString()).apply();
            }
        });

        switch (pressureUnit) {
            case "in":
                pressureRadio.check(R.id.inRadioButton);
                break;
            case "mb":
                pressureRadio.check(R.id.mbRadioButton);
                break;
            default:
                pressureRadio.check(R.id.mmHgRadioButton);
        }

        pressureRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton btn = (RadioButton) radioGroup.findViewById(id);
                sf.edit().putString("pressure_units", btn.getText().toString()).apply();
            }
        });

        Switch yahooSwitch = (Switch) findViewById(R.id.yahoo_switch);
        yahooSwitch.setChecked(sf.getBoolean("yahoo", true));
        yahooSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sf.edit().putBoolean("yahoo", b).apply();
            }
        });

        Switch openWeatherSwitch = (Switch) findViewById(R.id.openweather_switch);
        openWeatherSwitch.setChecked(sf.getBoolean("openweather", true));
        openWeatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sf.edit().putBoolean("openweather", b).apply();
            }
        });

        Switch worldWeatherSwitch = (Switch) findViewById(R.id.worldweather_switch);
        worldWeatherSwitch.setChecked(sf.getBoolean("worldweather", true));
        worldWeatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sf.edit().putBoolean("worldweather", b).apply();
            }
        });
    }
}
