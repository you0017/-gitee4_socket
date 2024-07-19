package com.yc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Test5_talkClient {
    private static Logger log = Logger.getLogger(Test5_talkClient.class.getName());

    public static void main(String[] args) throws IOException {
        InetAddress ia = InetAddress.getByName("localhost");
        System.out.println(ia);
        Socket s = new Socket(ia,10001);
        log.info("客户端连接服务器"+s.getRemoteSocketAddress()+"成功");

        try (Scanner keyboard = new Scanner(System.in);
             //套接字流
             Scanner clientReader = new Scanner(s.getInputStream());
             PrintWriter pw = new PrintWriter(s.getOutputStream());
        ) {
            do{

                System.out.println("请输入客户端对服务器要"+s.getRemoteSocketAddress()+"说的话:");
                String line = keyboard.nextLine();
                pw.println(line);
                pw.flush();
                if ("bye".equalsIgnoreCase(line)) {
                    System.out.println("客户端主动与服务器断开连接");
                    return;
                }

                String serverLine = clientReader.nextLine();
                System.out.println("服务器说："+serverLine);
                if ("bye".equalsIgnoreCase(serverLine)){
                    System.out.println("服务器主动与客户端断开连接");
                    return;
                }
            }while (true);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
