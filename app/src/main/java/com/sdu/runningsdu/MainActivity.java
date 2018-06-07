package com.sdu.runningsdu;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.sdu.runningsdu.Contact.ContactFragment;
import com.sdu.runningsdu.Find.FindFragment;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Map.MapFragment;
import com.sdu.runningsdu.Message.MessageFragment;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static int navigationButtonIndex = 0;

    private MapFragment mapFragment;
    private MessageFragment messageFragment;
    private ContactFragment contactFragment;
    private FindFragment findFragment;

    private BottomNavigationView bottomNavigationView;

    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;

    private MyApplication myApplication;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    if (mapFragment == null) {
                        mapFragment = new MapFragment();
                    }
                    transaction.replace(R.id.content, mapFragment);
                    navigationButtonIndex = 1;
                    break;
                case R.id.navigation_message:
                    if (messageFragment == null) {
                        messageFragment = new MessageFragment();
                    }
                    transaction.replace(R.id.content, messageFragment);
                    navigationButtonIndex = 2;
                    break;
                case R.id.navigation_contact:
                    if (contactFragment == null) {
                        contactFragment = new ContactFragment();
                    }
                    transaction.replace(R.id.content, contactFragment);
                    navigationButtonIndex = 3;
                    break;
                case R.id.navigation_find:
                    if (findFragment == null) {
                        findFragment = new FindFragment();
                    }
                    transaction.replace(R.id.content, findFragment);
                    navigationButtonIndex = 4;
                    break;
                default:
                    navigationButtonIndex = 0;
                    return false;
            }
            transaction.commitAllowingStateLoss();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //设置标题布局为titlebar
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        right = findViewById(R.id.coordinator);
        left = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // "setNavigationIcon" should after "setSupportActionBar" and "addDrawerListener"
        toolbar.setNavigationIcon(R.drawable.head_image);
        toolbar.setTitle("主题");

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + dm.widthPixels, dm.heightPixels);
            }

            @Override
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                if (id == R.id.nav_camera) {
                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        myApplication = (MyApplication) getApplication();
        User user = myApplication.getUser();
        //通过NavigationView获取headerLayout，进而获取其中组件
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userName.setText(user.getName());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
