package com.yc;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 基于UDP的Socket开发
 */
public class Test9_udp_sender {
    public static void main(String[] args) throws IOException {
        String s = "中文";
        //注意：DatagramPacket的构造方法参数  三个参数：最后一个参数表示另一台机器的地址和端口
        //UDP中的每一个包都必须指明要发送的地址
        DatagramPacket dp = new DatagramPacket(s.getBytes(),s.getBytes().length,new InetSocketAddress("127.0.0.1",3333));
        //使用本机的一个空闲端口发送
        DatagramSocket ds = new DatagramSocket(15678);
        ds.send(dp);
        ds.close();
        System.out.println("发送成功");
    }
}
