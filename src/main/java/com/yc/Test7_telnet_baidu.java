package com.yc;

import java.io.*;
import java.net.Socket;

public class Test7_telnet_baidu {
    public static void main(String[] args) {
        String website = "www.baidu.com";
        int port = 80;

        //String http = "GET / HTTP/1.0\r\nHost: www.baidu.com\r\n\r\n";  //应用层协议
        String http = "GET / HTTP/1.0\r\n\r\n";//应用层协议
        //http服务器 -> http协议
        try (Socket s = new Socket(website,port);
             OutputStream oos = s.getOutputStream();
             InputStream iis = s.getInputStream();){
            oos.write(http.getBytes());
            oos.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();   //因为怕字符的字节被拆开读，所以用
            byte[] bs = new byte[10*1024];
            int length = -1;
            while ((length = iis.read(bs,0,bs.length))!=-1){
                baos.write(bs,0,length);
                baos.flush();
            }
            System.out.println(new String(baos.toByteArray()));
            /*InputStreamReader isr = new InputStreamReader(iis);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine())!=null){
                System.out.println(line);
            }*/
        }catch (Exception e){

        }
    }
}
