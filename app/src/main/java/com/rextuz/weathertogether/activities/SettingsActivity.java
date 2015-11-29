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

        RadioGroup unitsRadio = (RadioGroup) findViewById(R.id.unitsRadio);
        String units = sf.getString("temperature_units", "C");
        switch (units) {
            case "F":
                unitsRadio.check(R.id.fRadioButton);
                break;
            case "K":
                unitsRadio.check(R.id.kRadioButton);
                break;
            default:
                unitsRadio.check(R.id.cRadioButton);
        }

        unitsRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton btn = (RadioButton) radioGroup.findViewById(id);
                sf.edit().putString("temperature_units", btn.getText().toString()).apply();
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
