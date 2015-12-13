package com.rextuz.weathertogether.instrumentaltests;

import android.support.test.runner.AndroidJUnit4;

import com.rextuz.weathertogether.activities.MainActivity;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ParseTextAndroidUnitTest {
    public static final String TEST_STRING = "wind";

    @Test
    public void checkOutputLegit() {
        int imageID = MainActivity.parseText(TEST_STRING);
        Assert.assertNotNull(imageID);
        Assert.assertTrue(imageID > 0);
    }

}
