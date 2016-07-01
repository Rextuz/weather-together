package com.rextuz.weathertogether.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.managers.OnClickManager;
import com.rextuz.weathertogether.managers.VirtualTabManager;
import com.rextuz.weathertogether.services.OpenWeatherMap;
import com.rextuz.weathertogether.services.WeatherService;
import com.rextuz.weathertogether.services.WorldWeatherOnline;
import com.rextuz.weathertogether.services.YahooWeather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences sp;
    public VirtualTabManager tabs;
    private MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("com.rextuz.weathertogether", Context.MODE_PRIVATE);

        // AdMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Switch tabs
        tabs = new VirtualTabManager(this, findViewById(R.id.info_layout));
        tabs.hideAll();
        Button nextServiceButton = (Button) findViewById(R.id.next_service);
        Button prevServiceButton = (Button) findViewById(R.id.prev_service);
        prevServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.prevTab();
            }
        });
        nextServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.nextTab();
            }
        });
        // tabs.clickableCheck(prevServiceButton, nextServiceButton);

        // Get weather button
        Button getWeatherButton = (Button) findViewById(R.id.go_button);
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable button
                Button goButton = (Button) findViewById(R.id.go_button);
                goButton.setText(getText(R.string.loading));
                goButton.setClickable(false);

                // Clear tabs
                tabs.clear();

                // Get place
                TextView placeText = (TextView) findViewById(R.id.edit_place);
                String place = placeText.getText().toString();

                // Spawn layouts
                LinearLayout root = (LinearLayout) findViewById(R.id.services);
                root.removeAllViews();  // Remove all previous views
                for (WeatherService service : getServices()) {
                    View layout = getLayoutInflater().inflate(R.layout.weather_output, root, false);
                    root.addView(layout);

                    // Start async task
                    new OnClickManager().execute(activity, place, layout, service);
                }
            }
        });
    }

    public List<WeatherService> getServices() {
        List<WeatherService> services = new ArrayList<>();
        if (activity.sp.getBoolean("yahoo", true))
            services.add(new YahooWeather());
        if (activity.sp.getBoolean("openweather", true))
            services.add(new OpenWeatherMap());
        if (activity.sp.getBoolean("worldweather", true))
            services.add(new WorldWeatherOnline());
        return services;
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
        EditText editTextPlace = (EditText) findViewById(R.id.edit_place);
        Button getWeatherButton = (Button) findViewById(R.id.go_button);
        if (!editTextPlace.getText().toString().isEmpty())
            getWeatherButton.callOnClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
