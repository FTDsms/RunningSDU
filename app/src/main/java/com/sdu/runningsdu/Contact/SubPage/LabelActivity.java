package com.sdu.runningsdu.Contact.SubPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sdu.runningsdu.R;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class LabelActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_label);

        toolbarBack = findViewById(R.id.label_toolbar_back);
        toolbarButton = findViewById(R.id.label_toolbar_button);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
