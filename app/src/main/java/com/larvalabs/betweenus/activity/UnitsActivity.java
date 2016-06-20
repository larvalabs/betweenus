package com.larvalabs.betweenus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.larvalabs.betweenus.AppSettings;
import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.WidgetProvider;
import com.larvalabs.betweenus.utils.TextViewUtil;
import com.larvalabs.betweenus.utils.Utils;

/**
 *
 */
public class UnitsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_units);
        TextViewUtil.applyTextViewStyles((ViewGroup) findViewById(R.id.root));
    }

    public void clickedMetric(View view) {
        new AppSettings(this).setUnitsStandard(false);

        Toast.makeText(this, "Will now use metric units.", Toast.LENGTH_SHORT).show();
        Utils.updateAppWidgets(this);
        finish();
    }

    public void clickedStandard(View view) {
        new AppSettings(this).setUnitsStandard(true);

        Toast.makeText(this, "Will now use standard units.", Toast.LENGTH_SHORT).show();
        Utils.updateAppWidgets(this);
        finish();
    }
}
