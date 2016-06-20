package com.larvalabs.betweenus.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.SystemAlarmReceiver;
import com.larvalabs.betweenus.client.ServerResponse;
import com.larvalabs.betweenus.client.ServerUtil;
import com.larvalabs.betweenus.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DeviceLocation {

    public static ServerResponse refreshSync(final Context context) {
        Utils.log("System alarm: getting new location.");

        Location locationToUse = getLocation(context);

        if (locationToUse != null) {
            final AppSettings appSettings = new AppSettings(context);
            appSettings.updateFromLocation(locationToUse);

            ServerResponse serverResponse = ServerUtil.getService().updateLocationSync(appSettings.getServerUserId(),
                    appSettings.getLastLatitude(), appSettings.getLastLongitude());
            if (serverResponse != null) {
                appSettings.updateFromServerResponse(serverResponse);
                Utils.updateAppWidgets(context);
                return serverResponse;
            } else {
                Utils.error("Server response is null.");
                return null;
            }
        }
        return null;
    }

    public static void refresh(final Context context) {
        Utils.log("System alarm: getting new location.");

        Location locationToUse = getLocation(context);

        if (locationToUse != null) {
            final AppSettings appSettings = new AppSettings(context);
            appSettings.updateFromLocation(locationToUse);

            ServerUtil.getService().updateLocation(appSettings.getServerUserId(),
                    appSettings.getLastLatitude(), appSettings.getLastLongitude(),
                    new Callback<ServerResponse>() {
                        @Override
                        public void success(ServerResponse serverResponse, Response response) {
                            Utils.log("Successfully updated location with server.");

                            appSettings.updateFromServerResponse(serverResponse);
                            Utils.updateAppWidgets(context);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Utils.log("Error updating location on server: " + error.getMessage());
                        }
                    });

            Utils.log("New location is now " + appSettings.getLastLatitude() + "," + appSettings.getLastLongitude());
        }
    }

    @Nullable
    public static Location getLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location locationToUse = null;
        Location gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gpsLoc != null && netLoc != null) {
            if (gpsLoc.getTime() > netLoc.getTime()) {
                Utils.log("GPS last location most recent, using GPS location");
                locationToUse = gpsLoc;
            } else {
                Utils.log("Network last location most recent, using network location");
                locationToUse = netLoc;
            }
        } else if (gpsLoc != null) {
            Utils.log("Using GPS location");
            locationToUse = gpsLoc;
        } else if (netLoc != null) {
            Utils.log("Using network location");
            locationToUse = netLoc;
        } else {
            Utils.log("No last known location, requesting updated location.");
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String bestProvider = lm.getBestProvider(criteria, true);
            Utils.log("Best provider available for high accuracy: " + bestProvider);
            Intent intent = new Intent(context, SystemAlarmReceiver.class);
            PendingIntent systemUpdatePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            lm.requestSingleUpdate(bestProvider, systemUpdatePendingIntent);
        }
        return locationToUse;
    }

}
