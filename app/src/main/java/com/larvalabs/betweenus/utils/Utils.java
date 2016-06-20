package com.larvalabs.betweenus.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.SystemAlarmReceiver;
import com.larvalabs.betweenus.WidgetProvider;
import com.larvalabs.betweenus.activity.UserInfoActivity;

import java.util.concurrent.TimeUnit;

public class Utils {

    private static String tag = "BetweenUs";

    public static void log(String message) {
        Log.d(tag, message);
    }

    public static void error(String message) {
        Log.e(tag, message);
    }

    public static void error(Throwable e) {
        Log.e(tag, "", e);
    }

    public static void error(String message, Throwable e) {
        Log.e(tag, message, e);
    }

    public static void scheduleAlarmForLocationUpdates(Context context) {
        Intent intent = new Intent(context, SystemAlarmReceiver.class);
        scheduleRepeatingAlarm(context, TimeUnit.MINUTES.toMillis(10), 10, intent, false);
    }

    public static void scheduleRepeatingAlarm(Context context, long frequencyMillis, long delayMillis, Intent intent, boolean wakeup) {
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        int alarmType = AlarmManager.ELAPSED_REALTIME;
        if (wakeup) {
            alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        }
        am.setInexactRepeating(alarmType, delayMillis, frequencyMillis, sender);
    }

    public static void launchActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public static void updateAppWidgets(Context context) {
        Utils.log("updateAppWidgets");
        Intent updateIntent = new Intent(context, WidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(updateIntent);
    }

}
