package com.larvalabs.betweenus;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import com.larvalabs.betweenus.core.Constants;
import com.larvalabs.betweenus.core.DeviceLocation;
import com.larvalabs.betweenus.utils.Utils;

/**
 *
 */
public class BetweenUsApplication extends Application {

    private static BetweenUsApplication instance;

    public static Context get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Utils.scheduleAlarmForLocationUpdates(this);
        Utils.log("Scheduled alarm for location updates.");

        Constants.init();

        // Make sure we have a reasonable location in settings from the beginning
        Location location = DeviceLocation.getLocation(this);
        AppSettings appSettings = new AppSettings(this);
        appSettings.updateFromLocation(location);
    }
}
