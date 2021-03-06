package com.sdu.runningsdu.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sdu.runningsdu.R;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by FTDsm on 2018/4/16.
 */

public class MapFragment extends Fragment {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker,othersCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;

    private SensorManager mSensorManager;
    double degree = 0;
    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton,startguiji,missionButton;
    ToggleButton togglebtn = null;
    boolean isFirstLoc = true;// 是否首次定位

    //轨迹相关
    boolean guiji = false;//运行轨迹
    boolean isFirstGuiji = true;
    //起点图标
    BitmapDescriptor startBD;
    //终点图标
    BitmapDescriptor finishBD;
    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    Polyline mPolyline;//运动轨迹图层
    LatLng last = new LatLng(0, 0);//上一个定位点
    //MapStatus.Builder builder;

    //boolean jieshourenwu = false;
    int didian;//任务地点
    String missionLocation[];
    HashMap<Integer, double[]> hashMap = new HashMap<>();
    AlertDialog kaishirenwuDialog,dia1,dia2;
    String friendslongitude,friendslatitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getActivity().getApplicationContext());
//        setContentView(R.layout.activity_location);

    }
    @Override @Nullable

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_location,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestLocButton = (Button) getView().findViewById(R.id.button1);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("普通");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;

                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        togglebtn = (ToggleButton) getView().findViewById(R.id.togglebutton);
        togglebtn.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    // 普通地图
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                } else {
                    // 卫星地图
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }

            }
        });


        hashMap.put(0, new double[]{117.146500,36.674000});//三区
        hashMap.put(1, new double[]{117.147067,36.672689});//一号食堂
        hashMap.put(2, new double[]{117.144200,36.674773});//图书馆
        hashMap.put(3, new double[]{117.1459800,36.672512});//行政楼
        missionLocation = new String[]{"三区","一号食堂","图书馆","行政楼"};
        double mi = Math.random() * 3;
        didian = (int)mi;
        didian = 0;
        missionButton = (Button) getView().findViewById(R.id.missionButton);
        missionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DataConst.jieshourenwu){
                    dia1 = new AlertDialog.Builder(getActivity()).setTitle("是否接受任务？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DataConst.jieshourenwu = true;
                                    dia2 = new AlertDialog.Builder(getActivity()).setTitle("任务地点").setMessage(missionLocation[didian])
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                }
                                            })
                                            .show();
                                }
                            })
                            .show();
                }
                if(DataConst.jieshourenwu) {
                    dia2 = new AlertDialog.Builder(getActivity()).setTitle("任务地点").setMessage(missionLocation[didian])
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                }
            }
        });

        // 地图初始化
        mMapView = (MapView) getView().findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);//设置是否需要返回位置语义化信息
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        //指南针
        mSensorManager= (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor magenticSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(listener,magenticSensor,SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(listener,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);

        //轨迹
        startguiji = (Button)getView().findViewById(R.id.startguiji);
        startguiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!guiji){//开始
                    guiji = true;
                    startguiji.setText("停止记录轨迹");
                } else {//停止
                    guiji = false;
                    startguiji.setText("开始记录轨迹");

//                    if (mLocClient != null && mLocClient.isStarted()) {
                    //mLocClient.stop();
                    if (isFirstGuiji) {
                        points.clear();
                        last = new LatLng(0, 0);
                        return;
                    }

                    MarkerOptions oFinish = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oFinish.position(points.get(points.size() - 1));
                    oFinish.icon(finishBD);// 设置覆盖物图片
                    mBaiduMap.addOverlay(oFinish); // 在地图上添加此图层

                    //复位
                    points.clear();
                    last = new LatLng(0, 0);
                    isFirstGuiji = true;

//                    }
                }
            }
        });
        startBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_startpoint);
        finishBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_finishpoint);

        //开始任务
        AlertDialog.Builder kaishirenwuDialogBuilder = new AlertDialog.Builder(getActivity()).setTitle("是否开始任务？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int missiond = (int) (Math.random() * 3) ;//3个任务
                        switch (missiond){
                            case 0:{
                                kaishirenwuDialog.dismiss();
                                dia1.dismiss();
                                dia2.dismiss();
                                startActivityForResult(new Intent(getActivity(),unityactivity.class),1);
                                break;}
                            case 1:{
                                kaishirenwuDialog.dismiss();
                                dia1.dismiss();
                                dia2.dismiss();
                                startActivityForResult(new Intent(getActivity(),HandgestureActivity.class), 4);

                                break;}
                            case 2:{
                                kaishirenwuDialog.dismiss();
                                dia1.dismiss();
                                dia2.dismiss();

                                break;}
                        }
                    }
                });
        kaishirenwuDialog = kaishirenwuDialogBuilder.create();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) //从handgesture返回的回调
        {
            //连接服务器
            new AlertDialog.Builder(getActivity()).setTitle("任务完成")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DataConst.jieshourenwu = false;
                        }
                    })
                    .show();
        }
        if (requestCode == 1 && resultCode == 4) { //从unityactivity返回的回调
            new AlertDialog.Builder(getActivity()).setTitle("任务完成")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DataConst.jieshourenwu = false;
                        }
                    })
                    .show();
        }

    }



    //指南针 获取degree值
    private SensorEventListener listener=new SensorEventListener() {
        float[] accelerometerValues=new float[3];
        float[] magenticValues=new float[3];
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //判断当前是加速度传感器还是地磁传感器
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerometerValues=sensorEvent.values.clone();
            }else if(sensorEvent.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD){
                magenticValues=sensorEvent.values.clone();
            }
            float[] R=new float[9];
            float[] values=new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magenticValues);
            SensorManager.getOrientation(R,values);
            //Log.d("LocationDemo","value[0] is"+Math.toDegrees(values[0]));
            degree = Math.toDegrees(values[0]);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    //如果打开了地图页面就给服务器发送一个当前位置，每隔几秒会给服务器发自己的位置并且请求所有好友的位置。。关闭这个页面的时候给服务器发一个取消位置的信息然后服务器吧位置设为null
    public void addOthersLocation(double latitute,double longtitute, Bitmap touxiang) {

        Resources r = this.getResources();
        Bitmap bmp= BitmapFactory.decodeResource(r, R.drawable.icon_geo);//红点

//        //构建Marker图标
//        othersCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);

        //构建Marker图标
        othersCurrentMarker = BitmapDescriptorFactory
                .fromBitmap(mergeBitmap(bmp,touxiang));//public static BitmapDescriptor fromBitmap(Bitmap image)  + mergeBitmap()

        //定义Maker坐标点
        LatLng point = new LatLng(latitute, longtitute);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(othersCurrentMarker);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    //将两张图片合并为一张图片 用作头像
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        int Width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight();
        secondBitmap = zoomImage(secondBitmap,Width,height);
        Bitmap bitmap = Bitmap.createBitmap(Width, height*2,
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(secondBitmap, new Matrix(), null);
        canvas.drawBitmap(firstBitmap, 0, height, null);
        return bitmap;
    }

    //缩放头像图片
    public  Bitmap zoomImage(Bitmap bgimage, double newWidth,
                             double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        int i = 0;
        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            if(DataConst.jieshourenwu) {
                for(int a = 0;a<missionLocation.length;a++){

                    if(Math.abs(location.getLongitude()-hashMap.get(a)[0])<=0.0003 && Math.abs(location.getLatitude()-hashMap.get(a)[1])<=0.0003) {

                        if((!kaishirenwuDialog.isShowing())&&a==didian){
                            Toast.makeText(getActivity(), "你已到达"+missionLocation[a],Toast.LENGTH_SHORT).show();
                            kaishirenwuDialog.show();
                        }
                        break;
                    }
                }
            }

            final MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction((float)degree).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            i++;
            if(i%10 == 0) {
                //发送自己的位置，退出页面的时候给服务器发个null
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            NetWorkClass.postlocation(location.getLongitude() + "", location.getLatitude() + "");
                        } catch (IOException e) {
                            Log.e("locationdemo", "ioexception");
                        }

                    }
                }.start();

//获取好友位置
                new Thread() {
                    @Override
                    public void run() {
                        try{
                            //Log.e("friendsLocation",NetWorkClass.receiveFriendslocation());
                            getListPersonByArray(NetWorkClass.receiveFriendslocation(),locData);

                        }catch (IOException e) {
                            Log.e("locationdemo", "ioexception");
                        }
                    }
                }.start();
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                //定义Maker坐标点
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // 设置缩放比例,更新地图状态
                float f = mBaiduMap.getMaxZoomLevel();// 19.0
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f - 2);
                mBaiduMap.animateMapStatus(u);
            }

            if(guiji) {
                if(isFirstGuiji){
                    //第一个点很重要，决定了轨迹的效果，gps刚开始返回的一些点精度不高，尽量选一个精度相对较高的起始点
                    LatLng ll = null;

                    ll = getMostAccuracyLocation(location);
                    if(ll == null){
                        return;
                    }
                    isFirstGuiji = false;
                    points.add(ll);//加入集合
                    last = ll;
//                    //显示当前定位点，缩放地图
//                    locateAndZoom(location, ll);

                    //标记起点图层位置
                    MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                    oStart.icon(startBD);// 设置覆盖物图片
                    mBaiduMap.addOverlay(oStart); // 在地图上添加此图层
                }

                //从第二个点开始
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为5米才添加到集合中
                if (DistanceUtil.getDistance(last, ll) < 5) {
                    return;
                }

                points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

                last = ll;
//                //显示当前定位点，缩放地图
//                locateAndZoom(location, ll);

                //清除上一次轨迹，避免重叠绘画
                mMapView.getMap().clear();

                //起始点图层也会被清除，重新绘画
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(0));
                oStart.icon(startBD);
                mBaiduMap.addOverlay(oStart);

                //将points集合中的点绘制轨迹线条图层，显示在地图上
                OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
                mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                //}
            }
        }

    }
    //解析好友jsonarray
    public void getListPersonByArray(String jsonString,final MyLocationData locData) {
        try {
//            if (points.size() > 0) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mBaiduMap.clear();
//                        mBaiduMap.setMyLocationData(locData);
//
//                        //起始点图层也会被清除，重新绘画
//                        MarkerOptions oStart = new MarkerOptions();
//                        oStart.position(points.get(0));
//                        oStart.icon(startBD);
//                        mBaiduMap.addOverlay(oStart);
//
//                        //将points集合中的点绘制轨迹线条图层，显示在地图上
//                        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
//                        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
//
//                    }
//                });
//            }


            //final List<OverlayOptions> options = new ArrayList<OverlayOptions>();//存好友marker数组
            JSONArray jsonArray=new JSONArray(jsonString);
            Log.e("jsonArray.length()长度", jsonArray.toString());
            Resources r = this.getResources();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);

                String id = jsonObject.optString("sid");
                friendslongitude = jsonObject.optString("longitude");
                friendslatitude = jsonObject.optString("latitude");
                if(!(friendslongitude.equals("null")&&friendslatitude.equals("null"))) {
                    //http://lbsyun.baidu.com/index.php?title=androidsdk/guide/render-map/point

                    final double friendslatitudeD = Double.parseDouble(friendslatitude);
                    final double friendslongitudeD = Double.parseDouble(friendslongitude);

                    final Bitmap bmp1= BitmapFactory.decodeResource(r, R.drawable.head_image);//默认头像
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addOthersLocation(friendslatitudeD,friendslongitudeD,bmp1);
                        }
                    });
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(BDLocation location){

        if (location.getRadius()>40) {//gps位置精度大于40米的点直接弃用
            return null;
        }

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

        if (DistanceUtil.getDistance(last, ll) > 10) {
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
        if(points.size() >= 5){
            points.clear();
            return ll;
        }
        return null;
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;

        if(mSensorManager!=null){
            mSensorManager.unregisterListener(listener);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    NetWorkClass.postlocation("null", "null");
                } catch (IOException e) {
                    Log.e("locationdemo", "ioexception");
                }
            }
        }.start();

        super.onDestroy();
    }

}
