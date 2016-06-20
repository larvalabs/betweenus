package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.client.ServerResponse;
import com.larvalabs.betweenus.client.ServerUtil;
import com.larvalabs.betweenus.utils.TextViewUtil;
import com.larvalabs.betweenus.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class UserConnectedActivity extends Activity {

    private TextView connectionLabel;
    private AppSettings appSettings;

    public static void launch(Context context) {
        Intent intent = new Intent(context, UserConnectedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_connected);

        appSettings = new AppSettings(this);

        // CONNECTION LABEL
        connectionLabel = (TextView) findViewById(R.id.pairing_caption);
        refreshConnectionCaption();

        TextViewUtil.applyTextViewStyles((ViewGroup) findViewById(R.id.root));
    }

    public void clickedHamburger(View view) {
        HamburgerMenuActivity.launch(this);
    }

    private void refreshConnectionCaption() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionLabel != null) {
                    String pairedUser = appSettings.getPairedUsername();
                    if (TextUtils.isEmpty(pairedUser)) {
                        connectionLabel.setText(R.string.betweenus_default);
                    } else {
                        connectionLabel.setText("You're connected to " + pairedUser + ". \nYou're good to go.");
                    }
                }
            }
        });
    }

    public void clickedSetWidget(View view) {
        HowToSetUpWidgetActivity.launch(this);

        // Note: Failed attempt to select widget from app:
//        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
//        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1024);
//        startActivityForResult(pickIntent, 9);
    }
}
