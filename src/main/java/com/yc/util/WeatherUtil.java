package com.yc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherUtil {
    public static String weather(String cityName) throws IOException
    {
        URL url = new URL("http://ws.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName?theCityName="+cityName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Host","ws.webxml.com.cn");

        con.setDoInput(true);
        con.setDoOutput(true);

        try (InputStream iis = con.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();){
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = iis.read(buf)) != -1)
            {
                baos.write(buf,0,len);
            }
            return baos.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
