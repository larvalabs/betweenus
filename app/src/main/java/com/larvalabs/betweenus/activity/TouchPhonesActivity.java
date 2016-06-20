package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.utils.Utils;

/**
 *
 */
public class TouchPhonesActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private AppSettings appSettings;
    private NfcAdapter mNfcAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TouchPhonesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phones_touch);

        appSettings = new AppSettings(this);

        ImageView img = (ImageView) findViewById(R.id.pairing_image);
        img.setImageResource(R.drawable.pairing);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getDrawable();
        frameAnimation.setOneShot(false);
        frameAnimation.start();

        // NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null || (mNfcAdapter != null && !mNfcAdapter.isEnabled())) {
            Toast.makeText(this, R.string.nfc_not_available, Toast.LENGTH_LONG).show();
            Utils.log("NFC not available.");
        } else {
            Utils.log("Registering NFC callbacks.");
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return getNdefMessage(appSettings.getUsername());
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        Utils.log("NFC push complete");

        FindingPartnerActivity.launchActivity(this, true);
    }

    private NdefMessage getNdefMessage(String username) {
        Utils.log("Creating NDEF message.");
        Long id = new AppSettings(this).getServerUserId();
        byte[] payload = id.toString().getBytes();
        NdefRecord record = NdefRecord.createMime("app/betweenus", payload);
        return new NdefMessage(record);
    }

}
