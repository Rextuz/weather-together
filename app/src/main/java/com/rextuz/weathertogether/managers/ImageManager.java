package com.rextuz.weathertogether.managers;

import com.rextuz.weathertogether.R;

public class ImageManager {

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

}
