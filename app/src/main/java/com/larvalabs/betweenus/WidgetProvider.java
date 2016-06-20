package com.larvalabs.betweenus;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import com.larvalabs.betweenus.activity.IntroActivity;
import com.larvalabs.betweenus.core.DeviceLocation;
import com.larvalabs.betweenus.utils.TextViewUtil;
import com.larvalabs.betweenus.utils.Utils;

import java.util.List;

public class WidgetProvider extends AppWidgetProvider {

    // Yes... must use different action intents..
    public static String UNIT_IMAGE_LEFT_CLICK_ACTION = "unitImage1ClickAction";
    public static String UNIT_IMAGE_CENTER_CLICK_ACTION = "unitImage2ClickAction";
    public static String UNIT_IMAGE_RIGHT_CLICK_ACTION = "unitImage3ClickAction";
    public static String BACK_CLICK_ACTION = "backClickAction";
    private static final String ACTION_UPDATE_LOCATION_AND_REFRESH = "updateLocationAndRefresh";
    private static final String ACTION_DISCONNECTED_CLICK = "disconnectedClick";

    private static List<Units.Unit> randomUnits;
    private static Units.Unit unitDisplay = null;

    private boolean usersConnected;
    private static WidgetAnimation runningAnimation;
    private static int flipperDisplayedChild;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        AppSettings appSettings = new AppSettings(context);

        unitDisplay = null;
        Utils.log("Intent action: " + intent.getAction());
        if (intent.getAction().equals(UNIT_IMAGE_LEFT_CLICK_ACTION)) {
            if (randomUnits != null) {
                unitDisplay = randomUnits.get(0);
            }
        } else if (intent.getAction().equals(UNIT_IMAGE_CENTER_CLICK_ACTION)) {
            if (randomUnits != null) {
                unitDisplay = randomUnits.get(1);
            }
        } else if (intent.getAction().equals(UNIT_IMAGE_RIGHT_CLICK_ACTION)) {
            if (randomUnits != null) {
                unitDisplay = randomUnits.get(2);
            }
        } else if (intent.getAction().equals(BACK_CLICK_ACTION)) {
            // Prevent further animation frames to run if they're scheduled
            if (runningAnimation != null) {
                runningAnimation.cancel();
                runningAnimation = null;
            }
        } else if (intent.getAction().equals(ACTION_UPDATE_LOCATION_AND_REFRESH)) {
            DeviceLocation.refresh(context);    // Note this will eventually trigger a full widget refresh too
        } else if (intent.getAction().equals(ACTION_DISCONNECTED_CLICK)) {
            Intent launchIntent = new Intent(context, IntroActivity.class);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
        } else {
            // REFRESH
            randomUnits = Units.getRandomSetOfThreeUnits(appSettings.getLastDistance());
        }

        if (unitDisplay != null) {
            this.onUpdateUnitInfo(context, AppWidgetManager.getInstance(context), unitDisplay);
        } else {
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("Widget", "onUpdate");
        final int n = appWidgetIds.length;
        final AppSettings appSettings = new AppSettings(context);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < n; i++) {
            int appWidgetId = appWidgetIds[i];
            if (appSettings.getUsersConnected()) {
                // USERS CONNECTED
                usersConnected = true;
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                setupConnectedViews(context, appWidgetIds, appSettings, views);
                handleViewFlipperOnUpdate(views);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            } else if (usersConnected) {
                // USERS DISCONNECTED
                usersConnected = false;
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_disconnected);
                setupDisconnectedViews(context, appWidgetIds, views);
                handleViewFlipperOnUpdate(views);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            } else {
                // IDLE
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_idle);
                setupIdleViews(context, appWidgetIds, views);
                handleViewFlipperOnUpdate(views);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    private void handleViewFlipperOnUpdate(RemoteViews views) {
        if (runningAnimation != null && runningAnimation.isRunning() && flipperDisplayedChild != 1) {
            views.setDisplayedChild(R.id.root, 1);
            flipperDisplayedChild = 1;
        } else if (flipperDisplayedChild != 0) {
            views.setDisplayedChild(R.id.root, 0);
            flipperDisplayedChild = 0;
        }
    }

    public void onUpdateUnitInfo(Context context, AppWidgetManager appWidgetManager, Units.Unit unit) {
        Log.d("Widget", "onUpdateUnitInfo");
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = man.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        final int n = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < n; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Resources resources = context.getResources();
            if (unitDisplay != null && unitDisplay.getFramesArrayResourceId() != -1) {
                // Add first frame of upcoming animation while animation is being loaded
                TypedArray framesTA = resources.obtainTypedArray(unitDisplay.getFramesArrayResourceId());
                views.setImageViewResource(R.id.image_big, framesTA.getResourceId(0, 0));
            } else {
                views.setImageViewResource(R.id.image_big, unit.getDefaultResourceId());
            }

            views.setImageViewResource(R.id.image, unit.getImageResourceId());

            {
                String captionString = resources.getString(unit.getCaptionStrId());
                Bitmap textBitmap = TextViewUtil.getFontBitmap(context, captionString,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_title_text_size));
                views.setImageViewBitmap(R.id.caption, textBitmap);
            }
            {
                String sizeString = unit.getSizeString(context);
                Bitmap textBitmap = TextViewUtil.getFontBitmap(context, sizeString,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_unit_subtitle_text_size));
                views.setImageViewBitmap(R.id.size, textBitmap);
            }
            {
                // OnClickListener
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(BACK_CLICK_ACTION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.root, pendingIntent);
                views.setDisplayedChild(R.id.root, 1);
                flipperDisplayedChild = 1;
            }

            setupWidgetInfoSearchButton(context, unit, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            if (unitDisplay != null && unitDisplay.getFramesArrayResourceId() != -1) {
                runUnitInfoAnimation(context, appWidgetId, unitDisplay);
            }
        }
    }

    public void setupWidgetInfoSearchButton(Context context, Units.Unit unit, RemoteViews views) {
        // Search click listener
        Intent intent = new Intent();
//        intent.setPackage("com.google.android.googlequicksearchbox");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra("query", unit.getSearchQuery());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_google, pendingIntent);
    }

    private void setupConnectedViews(Context context, int[] appWidgetIds, AppSettings appSettings, RemoteViews views) {

        Double distanceKm = appSettings.getLastDistance();
        if (randomUnits == null) {
            randomUnits = Units.getRandomSetOfThreeUnits(distanceKm);
        }

        Utils.log("Widget: Distance is " + distanceKm);

        Resources resources = context.getResources();
        {
            if (appSettings.hasOtherUsername()) {
                String title = String.format(context.getString(R.string.things_between_username_and_i), appSettings.getPairedUsername());
                Bitmap titleTextBitmap = TextViewUtil.getFontBitmap(context, title,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_title_text_size));
                views.setImageViewBitmap(R.id.widget_title, titleTextBitmap);
            } else {
                String title = context.getString(R.string.things_between_us);
                views.setImageViewBitmap(R.id.widget_title, TextViewUtil.getFontBitmap(context, title,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_title_text_size)));
            }
        }

        {
            views.setImageViewResource(R.id.image_left, randomUnits.get(0).getImageResourceId());
            String distance = randomUnits.get(0).getFormattedDistance(distanceKm);
            Bitmap distanceTextBitmap = TextViewUtil.getFontBitmap(context, distance,
                    resources.getColor(R.color.text_widget),
                    resources.getInteger(R.integer.widget_unit_text_size));
            views.setImageViewBitmap(R.id.distance_caption1, distanceTextBitmap);
            setClickListener(context, views, appWidgetIds, R.id.image_left, UNIT_IMAGE_LEFT_CLICK_ACTION);
            setClickListener(context, views, appWidgetIds, R.id.distance_caption1, UNIT_IMAGE_LEFT_CLICK_ACTION);
        }
        {
            views.setImageViewResource(R.id.image_center, randomUnits.get(1).getImageResourceId());
            String distance = randomUnits.get(1).getFormattedDistance(distanceKm);
            Bitmap distanceTextBitmap = TextViewUtil.getFontBitmap(context, distance,
                    resources.getColor(R.color.text_widget),
                    resources.getInteger(R.integer.widget_unit_text_size));
            views.setImageViewBitmap(R.id.distance_caption2, distanceTextBitmap);
            setClickListener(context, views, appWidgetIds, R.id.image_center, UNIT_IMAGE_CENTER_CLICK_ACTION);
            setClickListener(context, views, appWidgetIds, R.id.distance_caption2, UNIT_IMAGE_CENTER_CLICK_ACTION);
        }
        {
            views.setImageViewResource(R.id.image_right, randomUnits.get(2).getImageResourceId());
            String distance = randomUnits.get(2).getFormattedDistance(distanceKm);
            Bitmap distanceTextBitmap = TextViewUtil.getFontBitmap(context, distance,
                    resources.getColor(R.color.text_widget),
                    resources.getInteger(R.integer.widget_unit_text_size));
            views.setImageViewBitmap(R.id.distance_caption3, distanceTextBitmap);
            setClickListener(context, views, appWidgetIds, R.id.image_right, UNIT_IMAGE_RIGHT_CLICK_ACTION);
            setClickListener(context, views, appWidgetIds, R.id.distance_caption3, UNIT_IMAGE_RIGHT_CLICK_ACTION);
        }
        setRefreshClickListener(context, appWidgetIds, views, R.id.refresh);
    }

    private void setClickListener(Context context, RemoteViews views, int[] appWidgetIds, int imageResourceId, String action) {
        // OnClickListener
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(imageResourceId, pendingIntent);
    }

    private void setupDisconnectedViews(Context context, int[] appWidgetIds, RemoteViews views) {
        // OnClickListener
        setRefreshClickListener(context, appWidgetIds, views, R.id.refresh);
    }

    private void setupIdleViews(Context context, int[] appWidgetIds, RemoteViews views) {
        // OnClickListener
        setRefreshClickListener(context, appWidgetIds, views, R.id.refresh);

        setClickListener(context, views, appWidgetIds, R.id.image_big, ACTION_DISCONNECTED_CLICK);
    }

    private void setRefreshClickListener(Context context, int[] appWidgetIds, RemoteViews views, int refresh) {
        // OnClickListener
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(ACTION_UPDATE_LOCATION_AND_REFRESH);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(refresh, pendingIntent);
    }

    private void runUnitInfoAnimation(Context context, int widgetId, Units.Unit unit) {
        if (unit.getFramesArrayResourceId() != -1) {

            // Use dedicated widget unit layout to avoid ViewFliper blinking due to its fade_in animation.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_unit_info);

            Resources resources = context.getResources();

            views.setImageViewResource(R.id.image_big, unit.getImageResourceId());
            {
                String captionString = resources.getString(unit.getCaptionStrId());
                Bitmap textBitmap = TextViewUtil.getFontBitmap(context, captionString,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_title_text_size));
                views.setImageViewBitmap(R.id.caption, textBitmap);
            }
            {
                String sizeString = unit.getSizeString(context);
                Bitmap textBitmap = TextViewUtil.getFontBitmap(context, sizeString,
                        resources.getColor(R.color.text_widget),
                        resources.getInteger(R.integer.widget_unit_subtitle_text_size));
                views.setImageViewBitmap(R.id.size, textBitmap);
            }
            {
                // Return OnClickListener
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(BACK_CLICK_ACTION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.root, pendingIntent);
            }

            setupWidgetInfoSearchButton(context, unit, views);

            TypedArray framesTA = resources.obtainTypedArray(unit.getFramesArrayResourceId());
            int[] frames = new int[framesTA.length()];
            for (int i = 0; i < framesTA.length(); i++) {
                frames[i] = framesTA.getResourceId(i, -1);
            }

            int FRAME_LENGTH = 250;
            long ANIMATION_LENGTH = 5000;
            if (runningAnimation != null) {
                runningAnimation.cancel();
            }
            runningAnimation = new WidgetAnimation(frames, FRAME_LENGTH, ANIMATION_LENGTH);
            runningAnimation.run(context, views, R.id.image_big, widgetId);
        }
    }

}
