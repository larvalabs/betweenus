package com.larvalabs.betweenus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.larvalabs.betweenus.core.DeviceLocation;

/**
 *
 */
public class SystemAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DeviceLocation.refresh(context);
    }
}
