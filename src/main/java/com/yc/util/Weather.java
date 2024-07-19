package com.yc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Weather {
    public static String weather(String ip) throws IOException {
        String sky = Sky.sky(ip);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder() //101250401
                .url("http://d1.weather.com.cn/sk_2d/"+ sky +".html?_=1686662452105")
                .method("GET", null)
                .addHeader("Referer", "http://www.weather.com.cn/")
                .build();

        String parsedJson = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                //System.out.println("原始响应数据:");
                //System.out.println(responseBody);

                // 去掉开头的 JavaScript 变量声明部分
                String jsonStr = responseBody.substring(responseBody.indexOf('{'));

                // 解析 JSON 数据
                parsedJson = regex(jsonStr);
                if (parsedJson != null) {
                    //System.out.println("解析后的 JSON 数据:");
                    //System.out.println(parsedJson);
                } else {
                    System.out.println("解析失败");
                }
            } else {
                System.out.println("请求失败: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedJson;
    }

    public static String regex(String s) {
        try {
            // 解析 JSON 字符串
            JSONObject jsonObject = JSON.parseObject(s);

            // 创建新的 JSON 对象，用于存储中文字段名
            JSONObject newJsonObject = new JSONObject();

            // 设置中文字段名对应的值
            newJsonObject.put("城市英文名", jsonObject.getString("nameen"));
            newJsonObject.put("城市名称", jsonObject.getString("cityname"));
            newJsonObject.put("城市代码", jsonObject.getString("city"));
            newJsonObject.put("温度摄氏度", jsonObject.getString("temp"));
            newJsonObject.put("温度华氏度", jsonObject.getString("tempf"));
            newJsonObject.put("风向", jsonObject.getString("WD"));
            newJsonObject.put("风向英文", jsonObject.getString("wde"));
            newJsonObject.put("风速", jsonObject.getString("WS"));
            newJsonObject.put("风速英文", jsonObject.getString("wse"));
            newJsonObject.put("湿度", jsonObject.getString("SD"));
            newJsonObject.put("湿度英文", jsonObject.getString("sd"));
            newJsonObject.put("气压", jsonObject.getString("qy"));
            newJsonObject.put("能见度", jsonObject.getString("njd"));
            newJsonObject.put("时间", jsonObject.getString("time"));
            newJsonObject.put("降雨量", jsonObject.getString("rain"));
            newJsonObject.put("24小时降雨量", jsonObject.getString("rain24h"));
            newJsonObject.put("空气质量指数", jsonObject.getString("aqi"));
            newJsonObject.put("PM2.5空气质量指数", jsonObject.getString("aqi_pm25"));
            newJsonObject.put("天气情况", jsonObject.getString("weather"));
            newJsonObject.put("天气情况英文", jsonObject.getString("weathere"));
            newJsonObject.put("天气代码", jsonObject.getString("weathercode"));
            newJsonObject.put("限行号码", jsonObject.getString("limitnumber"));
            newJsonObject.put("日期", jsonObject.getString("date"));

            // 格式化输出 JSON 字符串
            String prettyJsonString = JSON.toJSONString(newJsonObject, SerializerFeature.PrettyFormat);
            System.out.println(prettyJsonString);

            return prettyJsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
