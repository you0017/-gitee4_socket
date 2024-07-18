package com.yc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("192.168.16.222", 10002);
        System.out.println(s);


        OutputStream oos = s.getOutputStream();
        oos.write("靠你吉瓦".getBytes());
        oos.flush();

        oos.close();
        s.close();
    }
}
