package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.client.ServerResponse;
import com.larvalabs.betweenus.client.ServerUtil;
import com.larvalabs.betweenus.events.ServerResponseEvent;
import com.larvalabs.betweenus.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class UserInfoActivity extends Activity {

    private EditText usernameEditText;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);

        appSettings = new AppSettings(this);

        // Get username EditText and set continue button click listener.
        usernameEditText = (EditText) findViewById(R.id.user_info_et);

        String username = appSettings.getUsername();
        if (!TextUtils.isEmpty(username)) {
            usernameEditText.setText(username);
        }

        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameEditText.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextView continueButton = (TextView) findViewById(R.id.continue_btn);
        if (continueButton != null) {
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onContinueButtonClick();
                }
            });
        }
    }

    private void onContinueButtonClick() {
        // Also checks server id
        if (isUsernameContentValid()) {
            ServerUtil.getService().setUsername(appSettings.getServerUserId(), appSettings.getUsername(),
                    new Callback<ServerResponse>() {
                        @Override
                        public void success(ServerResponse serverResponse, Response response) {
                            TouchPhonesActivity.launch(UserInfoActivity.this);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(UserInfoActivity.this, R.string.error_username_server, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean isUsernameContentValid() {
        String username = usernameEditText.getText().toString();
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError(getString(R.string.username_edittext_error));
            return false;
        }
        appSettings.setUsername(username);

        if (appSettings.getServerUserId() == -1) {
            Toast.makeText(this, "No Server user ID", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
