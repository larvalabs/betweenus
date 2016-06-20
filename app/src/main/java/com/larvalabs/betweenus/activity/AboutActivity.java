package com.larvalabs.betweenus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.larvalabs.betweenus.R;
import com.larvalabs.betweenus.utils.TextViewUtil;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        widgetsInit();
    }

    private void widgetsInit() {
        TextViewUtil.applyTextViewStyles((ViewGroup) findViewById(R.id.root));
        ImageView exitButton = (ImageView) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }
}
