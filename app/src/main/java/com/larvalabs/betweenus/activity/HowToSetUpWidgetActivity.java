package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.utils.TextViewUtil;

/**
 *
 */
public class HowToSetUpWidgetActivity extends Activity {

    private TextView connectionLabel;
    private AppSettings appSettings;

    public static void launch(Context context) {
        Intent intent = new Intent(context, HowToSetUpWidgetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_howtosetwidget);

        appSettings = new AppSettings(this);

        ImageView img = (ImageView) findViewById(R.id.howtosetwidget_image);
        img.setImageResource(R.drawable.widgethelp);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getDrawable();
        frameAnimation.setOneShot(false);
        frameAnimation.start();

        TextViewUtil.applyTextViewStyles((ViewGroup) findViewById(R.id.root));
    }

    public void clickedClose(View view) {
        finish();
    }

}
