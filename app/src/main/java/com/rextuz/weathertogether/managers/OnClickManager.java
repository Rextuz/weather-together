package com.rextuz.weathertogether.managers;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rextuz.weathertogether.R;
import com.rextuz.weathertogether.activities.MainActivity;
import com.rextuz.weathertogether.enitites.ShortWeatherEntity;
import com.rextuz.weathertogether.enitites.WeatherEntity;
import com.rextuz.weathertogether.services.WeatherService;

import java.util.ArrayList;
import java.util.List;

public class OnClickManager extends AsyncTask<Object, Void, Void> {

    private MainActivity activity;

    @Override
    protected Void doInBackground(Object... args) {
        // Initialize context
        activity = (MainActivity) args[0];
        String place = (String) args[1];
        View layout = (View) args[2];
        WeatherService service = (WeatherService) args[3];

        // Get weather
        WeatherEntity entity = service.getCurrentWeather(place);

        // Fill in weather
        entity.fillView(layout, activity);

        // Get forecast
        final LinearLayout root = (LinearLayout) activity.findViewById(R.id.services);
        final List<View> forecastViews = new ArrayList<>();
        for (final ShortWeatherEntity forecast : service.getWeatherForecast(place)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Spawn layout
                    View layout = activity.getLayoutInflater().inflate(R.layout.forecast_entry, root, false);
                    root.addView(layout);
                    forecastViews.add(layout);

                    // Fill layout
                    forecast.fillView(layout, activity);
                }
            });
        }

        activity.tabs.addTab(layout, forecastViews);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Button goButton = (Button) activity.findViewById(R.id.go_button);
        goButton.setText(activity.getText(R.string.go));
        goButton.setClickable(true);

        activity.tabs.showJustTab(0);
    }

}
