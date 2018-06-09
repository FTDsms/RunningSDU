package com.sdu.runningsdu.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sdu.runningsdu.R;
import com.unity3d.player.UnityPlayerActivity;

public class unityactivity extends UnityPlayerActivity {

    private LinearLayout mLlUnityContainer;
    //private Button mBtnRotate;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_unityactivity);
        initView();
    }

    private void initView() {
        mLlUnityContainer = (LinearLayout) findViewById(R.id.ll_unity_container);
        //mBtnRotate = (Button) findViewById(R.id.btn_rotate);

        mUnityPlayer = new MyUnityPlayer(this);
        //将Unity的View添加到布局里
        View scene = mUnityPlayer.getView();
        mLlUnityContainer.addView(scene);

    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Intent i=new Intent();
                setResult(4);
                finish();
                //mUnityPlayer.quit();
            }
        });
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        mUnityPlayer.quit();
        super.onDestroy();
    }
}