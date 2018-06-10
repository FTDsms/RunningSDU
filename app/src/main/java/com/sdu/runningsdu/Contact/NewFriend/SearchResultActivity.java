package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class SearchResultActivity extends AppCompatActivity {

    private SearchView searchView;

    private ListView listView;

    private List<Friend> resultList;

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_result);

        myApplication = (MyApplication) getApplication();

        searchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.result_list);

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(SearchResultActivity.this, "请输入查找内容!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        resultList.clear();
                        resultList = MyHttpClient.findUserByName(myApplication.getIp(), query);
                        resultList.add(MyHttpClient.findUserBySid(myApplication.getIp(), query));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
