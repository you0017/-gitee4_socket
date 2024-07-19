package com.yc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Test5_talkServer {

    private static Logger log = Logger.getLogger(Test5_talkServer.class.getName());

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        for (int i = 10000; i < 65535; i++) {
            try {
                //完成一台时间服务
                ss = new ServerSocket(i);
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                if (e instanceof BindException){
                    log.error("端口:"+i+"已经被占用");
                }
            }
        }

        System.out.println(ss.getInetAddress().getHostName() + "启动了，监听了端口号: ***" + ss.getLocalPort() +"***");

        //客户端通过键盘录入聊天信息
        Scanner keyboard = new Scanner(System.in);
        while (true){
            try (
                    Socket s = ss.accept();
                    //套接字流
                    Scanner clientReader = new Scanner(s.getInputStream()); //Scanner.nextLine()  按\n来接收
                    PrintWriter pw = new PrintWriter(s.getOutputStream());
                    ){
                log.info("客户端:"+s.getRemoteSocketAddress()+"连接上来了");
                do{
                    String response = clientReader.nextLine();
                    System.out.println("客户端"+s.getRemoteSocketAddress()+"对server说:"+response);
                    if ("bye".equalsIgnoreCase(response)){
                        System.out.println("客户端"+s.getRemoteSocketAddress()+"主动与服务器断开连接");
                        break;
                    }
                    System.out.println("请输入服务器要对客户端"+s.getRemoteSocketAddress()+"说的话:");
                    String line = keyboard.nextLine();
                    pw.println(line);
                    pw.flush();

                    if ("bye".equalsIgnoreCase(line)){
                        System.out.println("服务器主动与客户端"+s.getRemoteSocketAddress()+"断开连接");
                        break;
                    }
                }while (true);
                System.out.println("服务器与客户端"+s.getRemoteSocketAddress()+"断开连接");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
