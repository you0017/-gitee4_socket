package com.yc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test2_client {
    //编写第一个客户端
    public static void main(String[] args) throws IOException {
        InetAddress[] ia2 = InetAddress.getAllByName("www.baidu.com");
        if (ia2!=null){
            //baidu服务器的ip有多个
            for (InetAddress inetAddress : ia2) {
                Socket client = new Socket(inetAddress,80);
                System.out.println("连接成功，"+client); //Socket[addr=www.baidu.com/183.240.98.198,port=80,localport=54952]
                                                       //基于TCP的客户端套接字对象                             本机发送端口

                //本地端口 localport是随机选的
                client.close();
                break;
            }
        }

        Socket client2 = new Socket("www.baidu.com",80);
        System.out.println("连接成功2"+client2);

        Socket client3 = new Socket("45.113.192.102",80);
        System.out.println("连接成功3"+client3);

        client2.close();
        client3.close();
    }
}
