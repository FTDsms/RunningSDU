package com.sdu.runningsdu.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by FTDsm on 2018/6/13.
 */

public class DownloadImageTest extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;
    private ImageView imageView;

    private MyApplication myApplication;
    private MyDAO myDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_image);

        myApplication = (MyApplication) getApplication();

        toolbarBack = findViewById(R.id.download_image_toolbar_back);
        toolbarButton = findViewById(R.id.download_image_toolbar_button);
        imageView = findViewById(R.id.download_image_view);

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
                // 点击下载
                final Bitmap[] bitmap = new Bitmap[1];
                bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.head_image);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 下载图片
                            byte[] bytes = MyHttpClient.downloadImage(myApplication.getIp(), myApplication.getUser().getImagePath());
                            bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 显示图片
                                imageView.setImageBitmap(bitmap[0]);
                            }
                        });
                    }
                }).start();
            }
        });

    }
}
