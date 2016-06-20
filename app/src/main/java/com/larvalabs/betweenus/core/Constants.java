package com.larvalabs.betweenus.core;

import android.graphics.Typeface;

import com.larvalabs.betweenus.BetweenUsApplication;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    // Base reference duration in ms for almost all tweening
    // Is public so it can be changed at runtime for testing purposes
    public static int baseDuration = 290;

    public static final String NFC_URI = "nfc.betweenus.larvalabs.com";

    public static final String PREFS_FILE_KEY = "com.larvalabs.betweenus.PREFS_FILE_KEY";
    public static final String PREFS_USERNAME = "com.larvalabs.betweenus.PREFS_USERNAME";

    public static final int WELCOME_TIMEOUT_MS = 4000;
    public static final int MAX_DEBUG_LINES = 8;

    public static final int FINDING_PARTNER_MAX_RETRY = 8;
    public static final int FINDING_PARTNER_TIME_OUT = 2000;

    private static Map<String, Typeface> typefacesById;

    public static void init() {
        if (typefacesById == null) {
            typefacesById = new HashMap<>();
            Typeface questrialRegular = Typeface.createFromAsset(BetweenUsApplication.get().getAssets(), "fonts/Questrial-Regular.ttf");
            typefacesById.put("typeface_questrial_regular", questrialRegular);
        }
    }

    public static Typeface getTypefaceById(String id) {
        return typefacesById.get(id);
    }
}
