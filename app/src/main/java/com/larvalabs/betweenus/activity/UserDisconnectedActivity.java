package com.larvalabs.betweenus.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.utils.Utils;

public class UserDisconnectedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_disconnected);

        TextView startOverButton = (TextView) findViewById(R.id.start_over_btn);
        if (startOverButton != null) {
            startOverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStartOverButtonClick();
                }
            });
        }
    }

    private void onStartOverButtonClick() {
        Utils.launchActivity(UserDisconnectedActivity.this, UserInfoActivity.class);
    }
}
