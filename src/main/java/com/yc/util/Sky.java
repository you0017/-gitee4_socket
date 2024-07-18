package com.yc.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class Sky {
    public static String sky(String ip) throws IOException {
        OkHttpClient client2 = new OkHttpClient().newBuilder()
                .build();
        Request request2 = new Request.Builder()    //114.247.50.2
                .url("https://restapi.amap.com/v3/ip?ip="+ip+"&key=920e2366699e0de321522811e8900cb4")
                .method("GET", null)
                .build();
        Response response2 = client2.newCall(request2).execute();
        String s = response2.body().string();
        System.out.println(s);

        // 解析JSON
        JSONObject jsonObject = new JSONObject(s);
        System.out.println(jsonObject);

        // 获取city字段的值
        String city = jsonObject.getString("city");
        city = city.substring(0, city.length() - 1);

        // 输出city值
        System.out.println(city);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://j.i8tq.com/weather2020/search/city.js")
                .method("GET", null)
                .addHeader("Referer", "http://www.weather.com.cn/")
                .build();
        Response response = client.newCall(request).execute();
        s = response.body().string();
        s = s.substring(s.indexOf("{"));
        //System.out.println(s);


        // 解析 JSON 字符串
        JSONObject cityData = new JSONObject(s);

        // 获取海淀的 AREAID
        String haiDianAreaId = getAreaId(cityData, city);
        System.out.println(haiDianAreaId);

        return haiDianAreaId;
    }

    // 根据 NAMECN 获取 AREAID
    private static String getAreaId(JSONObject cityData, String cityName) {
        for (String province : cityData.keySet()) {
            JSONObject provinceObj = cityData.getJSONObject(province);
            for (String city : provinceObj.keySet()) {
                JSONObject cityObj = provinceObj.getJSONObject(city);
                if (cityObj.has(cityName) && cityObj.getJSONObject(cityName).has("AREAID")) {
                    return cityObj.getJSONObject(cityName).getString("AREAID");
                }
            }
        }
        return null; // 没有找到匹配的城市 NAMECN 对应的 AREAID
    }

        /*
        OkHttpClient client2 = new OkHttpClient().newBuilder()
                .build();
        Request request2 = new Request.Builder()
                .url("https://restapi.amap.com/v3/ip?ip="+ip+"&key=920e2366699e0de321522811e8900cb4")
                .method("GET", null)
                .build();
        Response response2 = client2.newCall(request2).execute();
        System.out.println(response2.body().string());*/

}
