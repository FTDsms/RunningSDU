package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.sdu.runningsdu.Information.DetailedInfoActivity;
import com.sdu.runningsdu.Information.NonFriendDetailedInfoActivity;
import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Message.RecyclerAdapter;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class SearchResultActivity extends AppCompatActivity {

    private SearchView searchView;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private SearchResultAdapter searchResultAdapter;

    private List<Friend> friends;

    private MyApplication myApplication;

    private MyDAO myDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_result);

        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        friends = new ArrayList<>();

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.result_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchResultAdapter = new SearchResultAdapter(friends, this);
        recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 打开详细信息页面
                if (!myApplication.isTest()) {
                    if (myDAO.hasFriend(friends.get(position).getSid())) {
                        // 已是好友
                        Intent intent = new Intent(SearchResultActivity.this, DetailedInfoActivity.class);
                        intent.putExtra("sid", friends.get(position).getSid());
                        startActivity(intent);
                    } else {
                        // 还不是好友
                        Intent intent = new Intent(SearchResultActivity.this, NonFriendDetailedInfoActivity.class);
                        intent.putExtra("sid", friends.get(position).getSid());
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, NonFriendDetailedInfoActivity.class);
                    intent.putExtra("sid", friends.get(position).getSid());
                    startActivity(intent);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(SearchResultActivity.this, "请输入查找内容!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!myApplication.isTest()) {
                                try {
                                    friends.clear();
                                    friends.addAll(MyHttpClient.findUserByName(myApplication.getIp(), query));
                                    friends.add(MyHttpClient.findUserBySid(myApplication.getIp(), query));
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                friends.add(new Friend("201500301132", "焦方锴", null));
                            }
                            for (Friend friend : friends) {
                                Log.i("test search", friend.toString());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchResultAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
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
