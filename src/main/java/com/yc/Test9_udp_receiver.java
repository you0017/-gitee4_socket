package com.yc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 基于UDP的Socket的接收端
 */
public class Test9_udp_receiver {
    public static void main(String[] args) throws IOException {
        byte buf[] = new byte[3];//字节数组，缓冲数据
        DatagramPacket dp = new DatagramPacket(buf, buf.length);//构建一个数据包对象
        DatagramSocket ds = new DatagramSocket(3333);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            System.out.println("等待接收数据...");
            //阻塞式方法，等待接收数据
            ds.receive(dp);//接收数据

            //System.out.println(new String(buf,0,dp.getLength()));//将数据写入到字节数组输出流中
            baos.write(buf,0,dp.getLength());
            baos.flush();
            System.out.println(baos.toString());
            baos.reset();
        }

    }
}
