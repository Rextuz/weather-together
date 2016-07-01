package com.rextuz.weathertogether.managers;

import android.view.View;

import com.rextuz.weathertogether.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class VirtualTabManager {

    private MainActivity activity;
    private List<Tab> tabs = new ArrayList<>();
    private int currentTab = 0;
    private View host;

    public VirtualTabManager(MainActivity activity, View host) {
        this.activity = activity;
        this.host = host;
    }

    // Add tab to list
    public int addTab(View weather, List<View> forecast) {
        Tab tab = new Tab(weather, forecast);
        tabs.add(tab);
        return tabs.indexOf(tab);
    }

    // Clear tab list
    public void clear() {
        tabs.clear();
    }

    // Show next tab
    public void nextTab() {
        if (currentTab + 1 < tabs.size()) {
            currentTab++;
            showJustTab(currentTab);
        }
    }

    // Show previous tab
    public void prevTab() {
        if (currentTab > 0) {
            currentTab--;
            showJustTab(currentTab);
        }
    }

    // Hide all tabs
    public void hideAll() {
        host.setVisibility(View.GONE);
        for (Tab tab : tabs)
            tab.setVisibility(View.GONE);
    }

    // Show just tab with index currentTab
    public void showJustTab(int currentTab) {
        this.currentTab = currentTab;
        hideAll();
        tabs.get(currentTab).setVisibility(View.VISIBLE);
        host.setVisibility(View.VISIBLE);
    }

    private class Tab {
        private View weather;
        private List<View> forecast = new ArrayList<>();

        // Create new tab
        public Tab(View weather, List<View> forecast) {
            this.weather = weather;
            this.forecast = forecast;
        }

        // Set visibility to a tab
        public void setVisibility(final int visibility) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weather.setVisibility(visibility);
                    for (View v : forecast)
                        v.setVisibility(visibility);
                }
            });
        }

        // Get tab's layout
        public View getWeatherEntity() {
            return weather;
        }
    }
}
