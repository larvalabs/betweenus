package com.larvalabs.betweenus;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Handler;
import android.widget.RemoteViews;

import com.larvalabs.betweenus.utils.Utils;

public class WidgetAnimation {

    private int[] frames;
    private int index = 0;
    private int frameLength;
    private long duration;
    private long startTime = 0;

    private Context context;
    private RemoteViews remoteViews;
    private int widgetId;
    private int imageViewId;
    private Handler handler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int frame = getNextFrame();
            Utils.log("Drawing frame #" + frame);
            remoteViews.setImageViewResource(imageViewId, frame);
            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);
            if (isRunning()) {
                handler.postDelayed(runnable, getFrameLength());
            }
        }
    };

    public WidgetAnimation(int[] resources, int frameLength, long duration) {
        this.frames = resources;
        this.frameLength = frameLength;
        this.duration = duration;
        this.handler = new Handler();
    }

    public int getNextFrame() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        index++;
        if (index >= frames.length) {
            index = 0;
        }
        return frames[index];
    }

    public int getFrameLength() {
        return frameLength;
    }

    public boolean isRunning() {
        return startTime == 0 || System.currentTimeMillis() - startTime < duration;
    }

    public void cancel() {
        Utils.log("Cancelling widget animation.");
        handler.removeCallbacksAndMessages(null);
    }

    public void run(Context context, RemoteViews remoteViews, int imageViewId, int widgetId) {
        this.context = context;
        this.remoteViews = remoteViews;
        this.imageViewId = imageViewId;
        this.widgetId = widgetId;

        // Wait for ViewFlipper animation to end
        handler.postDelayed(runnable, 500);
    }

}
