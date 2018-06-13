package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Test.DownloadImageTest;
import com.sdu.runningsdu.Utils.MyApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class UserInfoActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;

    private RelativeLayout setHeadImage;
    private CircleImageView head_image;

    private RelativeLayout getUserName;
    private TextView name;

    private RelativeLayout getUserSid;
    private TextView sid;

    private RelativeLayout getUserQRCode;

    private RelativeLayout getUserMore;

    private RelativeLayout getUserAddress;

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);

        myApplication = (MyApplication) getApplication();
        User user = myApplication.getUser();

        toolbarBack = findViewById(R.id.user_info_toolbar_back);
        toolbarButton = findViewById(R.id.user_info_toolbar_button);
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

        setHeadImage = findViewById(R.id.set_user_image);
        head_image = findViewById(R.id.user_image);
        getUserName = findViewById(R.id.get_user_name);
        name = findViewById(R.id.user_name);
        getUserSid = findViewById(R.id.get_user_sid);
        sid = findViewById(R.id.user_sid);
        getUserQRCode = findViewById(R.id.user_qrcode);
        getUserMore = findViewById(R.id.user_more);
        getUserAddress = findViewById(R.id.user_address);

        head_image.setImageResource(R.drawable.head_image);
        name.setText(user.getName());
        sid.setText(user.getSid());

        setHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // test 下载头像
//                Intent intent = new Intent(UserInfoActivity.this, DownloadImageTest.class);
//                startActivity(intent);

                // 点击弹出popueWindow 上传头像（从相册选择图片、拍照、取消）
                showPopupWindow();
            }
        });

    }

    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popup_window, null);
        Button album = popView.findViewById(R.id.btn_pop_album);
        Button camera = popView.findViewById(R.id.btn_pop_camera);
        Button cancel = popView.findViewById(R.id.btn_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels / 3;

        final PopupWindow popupWindow = new PopupWindow(popView, width, height);
//        popupWindow.setAnimationStyle(R.style.an);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从相册中选择图片
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍照
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.7f;
        getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

    }

}
