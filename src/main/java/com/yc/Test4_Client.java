package com.yc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Test4_Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost",10001);
        System.out.println("连接成功."+s);


        //输入流来接时间
        try (InputStream iis = s.getInputStream();
             InputStreamReader isr = new InputStreamReader(iis, StandardCharsets.UTF_8)){
            char[] bs = new char[1024];
            int length = -1;
            System.out.println("客户端等待服务端的响应");
            while ((length= isr.read(bs,0,bs.length))!=-1){
                String str = new String(bs,0,length);

                System.out.println("服务端的响应："+str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("客户端断开与服务器的连接");
        s.close();

    }
}
