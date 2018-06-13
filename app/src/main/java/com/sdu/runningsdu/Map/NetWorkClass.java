package com.sdu.runningsdu.Map;

import android.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetWorkClass {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

//    public void connect(String json){
//        //开启一个线程，做联网操作
//        new Thread() {
//            @Override
//            public void run() {
//                postJson(json);
//            }
//        }.start();
//    }

    //接收好友位置
    public static String receiveFriendslocation() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, json);
        FormBody formBody = new FormBody.Builder()
                .add("sid", DataConst.id)
                .build();
        Request request = new Request.Builder()
                .url(DataConst.locationip+"/getFriendsLocations")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }


    public static String postlocation(String longtitute, String latitute) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, json);
        FormBody formBody = new FormBody.Builder()
                .add("sid", "201500301132")
                .add("longitude", longtitute)
                .add("latitude", latitute)
                .build();
        Request request = new Request.Builder()
                .url(DataConst.locationip+"/updateLocation")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

//    public static String getAPIlocation(String longtitute, String latitute) throws IOException {
//        OkHttpClient okHttpClient = new OkHttpClient();
////        RequestBody body = RequestBody.create(JSON, json);
////        FormBody formBody = new FormBody.Builder()
////                .add("sid", "201500301132")
////                .add("longitude", longtitute)
////                .add("latitude", latitute)
////                .build();
//        //http://lbsyun.baidu.com/index.php?title=lbscloud/api/geosearch
//        //http://lbsyun.baidu.com/data/mydata#/?_k=avx8ac
//        Request request = new Request.Builder()
//                .url("http://api.map.baidu.com/geosearch/v3/nearby?ak="+DataConst.ak+"&mcode="+DataConst.anquanma+"&geotable_id="+DataConst.geotable_id+"&location="+longtitute+","+latitute+"&radius=5000&q=银行&sortby=distance:1")//GET请求
//                .get()
//                .build();
//        Response response = okHttpClient.newCall(request).execute();
//        return response.body().string();
//    }



    public static void postJson(String json, String requeste) {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(DataConst.locationip+requeste)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                Log.i("networkclass", response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String BuildLongLatiJson(double longtitude, double latitude){
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("id", DataConst.id);
        jsonObj1.put("longtitude", longtitude+"");
        jsonObj1.put("latitude", latitude+"");
        System.out.println("jsonObj1"+jsonObj1);
        return jsonObj1.toString();
    }

    public static void BuildJson() {
        //创建JSON对象的第一种方法
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("id", "1");
        jsonObj1.put("name", "rose");
        jsonObj1.put("age", 25);
        System.out.println("jsonObj1"+jsonObj1);
        //创建JSON对象的第二种方法
        HashMap<String, Object> map = new HashMap<String,Object>();
        map.put("id", "2");
        map.put("name", "zhangsan");
        map.put("age", "25");
        JSONObject jsonObj2 = JSONObject.fromObject(map);
        System.out.println("jsonObj2"+jsonObj2);

        //创建一个JsonArray方法1
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "ZHULI");
        jsonArray.add(1, "30");
        jsonArray.add(2, "ALI");
        System.out.println("jsonArray1：" + jsonArray);

        //创建JsonArray方法2
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("柯文修");
        arrayList.add("23");
        arrayList.add("5");
        System.out.println("jsonArray2：" + JSONArray.fromObject(arrayList));

        //创建一个复杂JSON对象（JSON对象里有JSON数组）
        JSONObject jsonObj3 = new JSONObject();
        jsonObj3.put("id", 3);
        jsonObj3.put("name", "lisi");
        jsonObj3.put("age", 23);
        jsonObj3.put("arrayList", arrayList);
        System.out.println("jsonObj3"+jsonObj3);

        //创建一个复杂JSON数组(JSON数组里含有JSON对象)、
        JSONArray jsonArray3 = new JSONArray();
        jsonArray3.add("4");
        jsonArray3.add("wangwu");
        jsonArray3.add("26");
        jsonArray3.add(map);
        System.out.println("jsonArray3"+jsonArray3);
        //把JSON字符串转为JSON对象
        String jsonString = "{\"id\":3,\"name\":\"lisi\",\"age\":23,\"arrayList\":[\"ZHULI\",\"30\",\"ALI\"]}";
        JSONObject jsonObj4 = JSONObject.fromObject(jsonString);
        System.out.println(jsonObj4.get("id")+"--"+jsonObj4.get("name")+"--"+jsonObj4.get("age")+
                "--"+jsonObj4.get("arrayList"));
        //java实体类转为JSONObject
//        Student stu = new Student();
//        stu.setId(1);
//        stu.setName("罗志茂");
//        stu.setAge("23");
//        stu.setBirthDay(new Date());
//        stu.setList(arrayList);
//        HashSet<Object> set = new HashSet<>();
//        set.add("6");
//        set.add("戚广辉");
//        set.add("25");
//        stu.setSet(set);
//        stu.setMap(map);
//        JsonConfig jsonConfig = new JsonConfig();
//        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
//        JSONObject jsonObj5 = JSONObject.fromObject(stu,jsonConfig);
//        System.out.println("序列化:"+jsonObj5);
//        //json转java实体类
//        Student student = (Student) JSONObject.toBean(jsonObj5, Student.class);
//        System.out.println("反序列化:"+student);

    }
}