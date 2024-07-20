package com.yc;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Test8_url_urlconnection_httpurlconnection {
    private static Logger log = Logger.getLogger(Test8_url_urlconnection_httpurlconnection.class);

    public static void main(String[] args) throws IOException {
        //1.URL类：URL:统一资源定位符 + URLConnection
        /*URL url = new URL("https://www.baidu.com");
        URLConnection con = url.openConnection();

        String contentType = con.getContentType();  //返回内容类型
        long conlength = con.getContentLength();    //内容长度
        log.info(contentType+"\t"+conlength);

        //响应头没有，响应头的内容已经被解析成con中的属性值了

//        InputStream iis = con.getInputStream();
//        byte[] bs = new byte[1024];
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int length = -1;
//        while ((length = iis.read(bs)) != -1) {
//            baos.write(bs, 0, length);
//        }
//        baos.flush();
//        byte[] bb = baos.toByteArray();
//        String str = new String(bb, "utf-8");
//        System.out.println(str);    //响应头，相应实体


        InputStream iis = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(iis);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }*/




        URL url = new URL("http://www.hyycinfo.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //HttpURLConnection是专门针对HTTP协议，所以有很多http相关的属性
        con.setRequestProperty("HOST","www.hyycinfo.com");
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");

        con.setDoInput(true);
        con.setDoOutput(true);

        String contentType = con.getContentType();
        long conlength = con.getContentLength();
        log.info(contentType+"\t"+conlength);

        InputStream iis = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(iis);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int length = -1;
        while ((length = iis.read()) != -1) {
            baos.write(length);
            baos.flush();
        }
        byte[] byteArray = baos.toByteArray();
        String str = new String(byteArray, "utf-8");
        System.out.println(str);*/
    }
}
