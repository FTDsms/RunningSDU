package com.sdu.runningsdu.Test;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.sdu.runningsdu.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class TestActivity extends SwipeBackActivity {

    private RadioGroup radioGroup;

    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        swipeBackLayout = getSwipeBackLayout();
        int edgeFlag = SwipeBackLayout.EDGE_RIGHT;
        swipeBackLayout.setEdgeTrackingEnabled(edgeFlag);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//
//            }
//        });
    }
}
