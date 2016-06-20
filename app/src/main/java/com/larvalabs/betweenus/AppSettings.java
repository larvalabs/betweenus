package com.larvalabs.betweenus;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.larvalabs.betweenus.client.ServerResponse;
import com.larvalabs.betweenus.utils.Utils;

/**
 *
 */
public class AppSettings {

    private static final String KEY_SERVER_USER_ID = "userId";
    private static final String KEY_SERVER_LAST_LAT = "lastLat";
    private static final String KEY_SERVER_LAST_LONG = "lastLong";
    private static final String KEY_SERVER_LAST_DISTANCE = "lastDistance";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PAIRED_USERNAME = "pairedUsername";
    private static final String KEY_SERVER_USERS_CONNECTED = "usersConnected";
    private static final String KEY_UNITS_STANDARD = "unitsStandard";

    private SharedPreferences prefs;

    public AppSettings(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public Long getServerUserId() {
        return prefs.getLong(KEY_SERVER_USER_ID, -1);
    }

    public void setServerUserId(Long userId) {
        prefs.edit().putLong(KEY_SERVER_USER_ID, userId).commit();
    }

    public void resetServerUserId() {
        prefs.edit().putLong(KEY_SERVER_USER_ID, -1).commit();
    }

    public Double getLastLatitude() {
        String val = prefs.getString(KEY_SERVER_LAST_LAT, "0");
        return Double.parseDouble(val);
    }

    public void setLastLatitude(Double latitude) {
        prefs.edit().putString(KEY_SERVER_LAST_LAT, latitude.toString()).commit();
    }

    public Double getLastLongitude() {
        String val = prefs.getString(KEY_SERVER_LAST_LONG, "0");
        return Double.parseDouble(val);
    }

    public void setLastLongitude(Double longitude) {
        prefs.edit().putString(KEY_SERVER_LAST_LONG, longitude.toString()).commit();
    }

    public Double getLastDistance() {
        String val = prefs.getString(KEY_SERVER_LAST_DISTANCE, "0");
        return Double.parseDouble(val);
    }

    public void setLastDistance(Double distance) {
        prefs.edit().putString(KEY_SERVER_LAST_DISTANCE, distance.toString()).commit();
    }

    public void setUsersConnected(boolean connected) {
        prefs.edit().putBoolean(KEY_SERVER_USERS_CONNECTED, connected).commit();
    }

    public boolean getUsersConnected() {
        return prefs.getBoolean(KEY_SERVER_USERS_CONNECTED, false);
    }

    public void setUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).commit();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public boolean hasSetUsername() {
        return prefs.getString(KEY_USERNAME, null) != null;
    }

    public void setPairedUsername(String pairedUsername) {
        prefs.edit().putString(KEY_PAIRED_USERNAME, pairedUsername).commit();
    }

    public String getPairedUsername() {
        return prefs.getString(KEY_PAIRED_USERNAME, null);
    }

    public boolean hasOtherUsername() {
        return prefs.getString(KEY_PAIRED_USERNAME, null) != null;
    }

    public void updateFromLocation(Location location) {
        if (location != null) {
            setLastLatitude(location.getLatitude());
            setLastLongitude(location.getLongitude());
        }
    }

    public void updateFromServerResponse(ServerResponse serverResponse) {
        if (serverResponse.isConnected) {
            setUsersConnected(true);
            setLastDistance(serverResponse.distance);
            setPairedUsername(serverResponse.otherUsername);
            Utils.log("New distance to other user is " + serverResponse.distance);
        } else {
            setUsersConnected(false);
            setPairedUsername(null);
            Utils.log("Not currently connected to another user, no distance value.");
        }
    }

    public void setUnitsStandard(boolean unitsStandard) {
        prefs.edit().putBoolean(KEY_UNITS_STANDARD, unitsStandard).commit();
    }

    public boolean shouldUseStandardUnits() {
        return prefs.getBoolean(KEY_UNITS_STANDARD, true);
    }
}
