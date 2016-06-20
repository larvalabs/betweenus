package com.larvalabs.betweenus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.larvalabs.betweenus.core.Constants;

public class TextViewUtil {

    private static final String TAG = TextViewUtil.class.getSimpleName();

    public static void applyTextViewStyles(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                applyTextViewStyles((ViewGroup) view);
            } else if (view instanceof TextView) {
                applyTextViewStyle((TextView) view);
            }
        }
    }

    public static void applyTextViewStyle(TextView textView) {

        if (textView.getTag() instanceof String) {

            String tag = (String) textView.getTag();
            String[] tokens = tag.split(",");

            // typeface
            Typeface typeface = Constants.getTypefaceById(tokens[0]);
            if (typeface != null) {
                textView.setTypeface(typeface);
            } else {
                Log.w(TAG, "no typeface found for tag token " + tokens[0]);
            }
        }
    }

    public static Bitmap getFontBitmap(Context context, String text, int color, float fontSizeSP) {
        int fontSizePX = convertDiptoPix(context, fontSizeSP);
        int pad = (fontSizePX / 9);
        Paint paint = new Paint();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Questrial-Regular.ttf");
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        paint.setColor(color);
        paint.setTextSize(fontSizePX);

        int textWidth = (int) (paint.measureText(text) + pad * 2);
        int height = (int) (fontSizePX / 0.75);
        Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
/*
        Paint fillPaint = new Paint();
        fillPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), fillPaint);
*/
        float xOriginal = pad;
        canvas.drawText(text, xOriginal, fontSizePX, paint);
        return bitmap;
    }

    public static int convertDiptoPix(Context context, float dip) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return value;
    }
}
