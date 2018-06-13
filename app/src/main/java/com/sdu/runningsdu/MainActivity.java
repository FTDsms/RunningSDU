package com.sdu.runningsdu;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.sdu.runningsdu.Contact.ContactFragment;
import com.sdu.runningsdu.Contact.GroupList.CreateGroupActivity;
import com.sdu.runningsdu.Contact.NewFriend.AddFriendActivity;
import com.sdu.runningsdu.Find.FindFragment;
import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Map.MapFragment;
import com.sdu.runningsdu.Information.UserInfoActivity;
import com.sdu.runningsdu.Message.MessageFragment;
import com.sdu.runningsdu.Utils.CircleDrawable;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.lang.reflect.Method;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

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

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private MyApplication myApplication;

    private MyDAO myDAO;

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
//                    appBarLayout.setVisibility(View.VISIBLE);
                    toolbarTitle.setText("地图");
                    break;
                case R.id.navigation_message:
                    if (messageFragment == null) {
                        messageFragment = new MessageFragment();
                    }
                    transaction.replace(R.id.content, messageFragment);
                    navigationButtonIndex = 2;
//                    appBarLayout.setVisibility(View.VISIBLE);
                    toolbarTitle.setText("消息");
                    break;
                case R.id.navigation_contact:
                    if (contactFragment == null) {
                        contactFragment = new ContactFragment();
                    }
                    transaction.replace(R.id.content, contactFragment);
                    navigationButtonIndex = 3;
//                    appBarLayout.setVisibility(View.VISIBLE);
                    toolbarTitle.setText("联系人");
                    break;
                case R.id.navigation_find:
                    if (findFragment == null) {
                        findFragment = new FindFragment();
                    }
                    transaction.replace(R.id.content, findFragment);
                    navigationButtonIndex = 4;
//                    appBarLayout.setVisibility(View.VISIBLE);
                    toolbarTitle.setText("发现");
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
        SDKInitializer.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

//        Button unityButton = findViewById(R.id.unitybutton);
//        unityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, unityactivity.class);
//                //startActivityForResult(intent,1);
//                startActivity(intent);
//            }
//        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        right = findViewById(R.id.coordinator);
        left = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(false);
//        toggle.setHomeAsUpIndicator(R.mipmap.head_image);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // "setNavigationIcon" should after "setSupportActionBar" and "addDrawerListener"
        Resources resources = MainActivity.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.head_image);
        int size = 44;
        CircleDrawable circleDrawable = new CircleDrawable(drawable, MainActivity.this, size);
        toolbar.setNavigationIcon(circleDrawable);

        // 标题
        toolbarTitle = findViewById(R.id.toolbar_title);

        // 选项菜单
//        toolbar.inflateMenu(R.menu.action_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.item_create_group:
                        // 打开创建群聊Activity
                        intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_add_friend:
                        intent = new Intent(MainActivity.this, AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_scan:
                        break;
                    case R.id.item_help_and_feedback:
                        break;
                }
                return true;
            }
        });

        // 溢出菜单icon，显示、隐藏溢出菜单弹出的窗口
        toolbar.setOverflowIcon(resources.getDrawable(R.drawable.add_white_36x36));
        toolbar.showOverflowMenu();
        toolbar.dismissPopupMenus();

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
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_wallet:
                        break;
                    case R.id.nav_collection:
                        break;
                    case R.id.nav_album:
                        break;
                    case R.id.nav_card_bag:
                        break;
                    case R.id.nav_expression:
                        break;
                    case R.id.nav_setting:
                        break;
                }
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        // 获取NavigationView的HeaderLayout
        View navigationHeader = navigationView.getHeaderView(0);
        LinearLayout userInfo = navigationHeader.findViewById(R.id.user_info);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开个人信息界面
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        myApplication = (MyApplication) getApplication();
        User user = myApplication.getUser();
        //通过NavigationView获取headerLayout，进而获取其中组件
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userName.setText(user.getName());
        TextView userSid = navigationView.getHeaderView(0).findViewById(R.id.user_sid);
        userSid.setText(user.getSid());

        // 初始化 消息页面
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
        }
        transaction.replace(R.id.content, messageFragment);
        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
        navigationButtonIndex = 2;
        toolbarTitle.setText("消息");


        Badge badge = new QBadgeView(this).bindTarget(findViewById(R.id.navigation_message));
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.setBadgeTextSize(12, true);
        badge.setGravityOffset(15, 0, true); //设置外边距
//            badge.setBadgePadding(6, true); //设置内边距
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {

                }
            }
        });

        int unread = 0;
        myDAO = new MyDAO(this, user.getName());
        List<Friend> friends = myDAO.findAllFriend();
        for (Friend friend : friends) {
            unread += friend.getUnread();
        }
        List<Group> groups = myDAO.findAllGroup();
        for (Group group : groups) {
            unread += group.getUnread();
        }
        if (unread > 99) {
            badge.setBadgeText("99+");
        } else {
            badge.setBadgeNumber(unread);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 解决Toolbar中Menu无法同时显示图标和文字的问题
     * */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
