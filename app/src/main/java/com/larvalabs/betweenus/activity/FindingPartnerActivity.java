package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.Toast;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.client.ServerResponse;
import com.larvalabs.betweenus.client.ServerUtil;
import com.larvalabs.betweenus.core.DeviceLocation;
import com.larvalabs.betweenus.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class FindingPartnerActivity extends Activity {

    private static final String INTENT_POLL = "poll";
    private static final int MAX_SERVER_POLL_ATTEMPTS = 10;

    public static void launchActivity(Context context, boolean pollServer) {
        Intent intent = new Intent(context, FindingPartnerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_POLL, pollServer);
        context.startActivity(intent);
    }

    private AppSettings appSettings;
    private boolean pollServer = false;
    private Timer timer = new Timer();
    private int serverPollAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finding_partner);

        appSettings = new AppSettings(this);

        ImageView img = (ImageView) findViewById(R.id.finding_partner_image);
        img.setImageResource(R.drawable.finding_partner);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getDrawable();
        frameAnimation.setOneShot(false);
        frameAnimation.start();

        if (getIntent() != null) {
            pollServer = getIntent().getBooleanExtra(INTENT_POLL, false);

            handleIntent(getIntent());
        }

        Utils.log("Should poll server: " + pollServer);

        // We proceed to the next step in one of two ways:
        // 1) We are the recipient of the NFC message, so we connect the users on the server and see
        // and immediate response.
        // 2) We are waiting for the NFC recipient to connect the users, so we just poll the server
        // to see when we're connected, or give up.

        // Poll server
        if (pollServer) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Utils.log("Checking server to see if users connected...");

                    if (serverPollAttempts > MAX_SERVER_POLL_ATTEMPTS) {
                        Utils.log("Exceeded max server checks for user connection, aborting.");
                        timer.cancel();
                        FindingPartnerActivity.this.finish();
                        return;
                    }


                    ServerResponse serverResponse = ServerUtil.getService().getInfoSync(appSettings.getServerUserId());
                    serverPollAttempts++;
                    if (serverResponse != null) {
                        appSettings.updateFromServerResponse(serverResponse);
                        if (serverResponse.isConnected) {
                            Utils.log("Connected to user! Other user is " + serverResponse.otherUsername);
                            Utils.updateAppWidgets(FindingPartnerActivity.this);
                            UserConnectedActivity.launch(FindingPartnerActivity.this);
                            finish();
                            timer.cancel();
                        }
                    } else {
                        Utils.error("Can't connect to user because location unavailable.");
                    }
                }
            }, 0, 1000);
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.log("Lifecycle: onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            handleNfcIntent(intent);
        }
    }

    private void handleNfcIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                NdefRecord[] records = msgs[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    NdefRecord record = records[j];
                    String text = new String(record.getPayload());
                    Utils.log("Received payload from partner via NFC: " + text);
                    Long otherUserId = Long.parseLong(text);
                    final AppSettings appSettings = new AppSettings(this);
                    Utils.log("Attempting to pair with user id " + otherUserId);
                    ServerUtil.getService().connect(appSettings.getServerUserId(), otherUserId,
                            new Callback<ServerResponse>() {
                                @Override
                                public void success(ServerResponse serverResponse, Response response) {

                                    Utils.log("Users connected.");
                                    appSettings.updateFromServerResponse(serverResponse);
                                    Utils.updateAppWidgets(FindingPartnerActivity.this);
                                    UserConnectedActivity.launch(FindingPartnerActivity.this);
                                    finish();
//                                    showUserConnectedScreen();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Utils.error("User connection failed: " + error.getMessage());
                                    Toast.makeText(FindingPartnerActivity.this, "User connection failed",
                                            Toast.LENGTH_LONG).show();

                                    finish();
                                }
                            });
//                    Toast.makeText(this, "Received: '" + text + "'.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
