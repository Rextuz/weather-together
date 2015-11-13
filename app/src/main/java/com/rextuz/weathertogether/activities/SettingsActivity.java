package com.rextuz.weathertogether.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.rextuz.weathertogether.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Temperature units spinner
        Spinner spinner = (Spinner) findViewById(R.id.temperature_units_spinner);
        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.temperature_units, android.R.layout.simple_spinner_dropdown_item));
        // TODO: set first unit in spinner from shared preferences
        /* Dynamic adapter prototype
        List<String> units = new ArrayList<>();
        units.add("K");
        units.add("C");
        units.add("F");
        SpinnerAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, units);
        spinner.setAdapter(adapter);
        */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sf = getSharedPreferences("com.rextuz.weathertogether", Context.MODE_PRIVATE);
                sf.edit().putString("temperature_units", adapterView.getItemAtPosition(i).toString()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
