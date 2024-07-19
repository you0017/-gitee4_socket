package com.yc.atm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import javax.sound.midi.Soundbank;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

public class AtmClient {
    protected static Logger log = Logger.getLogger(AtmClient.class);

    public static void main(String[] args) {
        String host = "localhost";
        int port = 12000;
        //创建一个socket
        Scanner keyboard = new Scanner(System.in);//键盘
        boolean flag = true;

        try (Socket s = new Socket(host, port);
            Scanner sc = new Scanner(s.getInputStream());
            PrintWriter pw = new PrintWriter(s.getOutputStream())){

            log.info("连接ATM服务器"+s.getRemoteSocketAddress()+"成功");

            do{
                System.out.println("=====ATM=====");
                System.out.println("1. 存");
                System.out.println("2. 取");
                System.out.println("3. 查询");
                System.out.println("4. 退出");
                System.out.println("5. 错误命令");
                System.out.println("============");
                System.out.println("请输入选项");
                String command = keyboard.nextLine();
                String response = null;
                if("1".equalsIgnoreCase(command)){
                    //TODL:操作资金输入
                    pw.println("DEPOSIT 1 100");
                }else if ("2".equalsIgnoreCase(command)){
                    pw.println("WITHDRAW 1 10");
                }else if ("3".equalsIgnoreCase(command)){
                    pw.println("BALANCE 1");
                } else if ("5".equalsIgnoreCase(command)) {
                    pw.println("qeqweq");
                }else {
                    pw.println("QUIT");
                    pw.flush();
                    flag = false;
                    break;
                }
                pw.flush();
                //去服务器响应
                response = sc.nextLine();
                System.out.println("服务器的响应:"+response);

                Gson gson = new Gson();
                //设定gson处理的字符串数据的模板  vue  axios
                Type type = new TypeToken<JsonModel<BankAccount>>(){}.getType();
                JsonModel<BankAccount> jm = gson.fromJson(response,type);
                if (jm.getCode()==1){
                    BankAccount ba = jm.getData();
                    System.out.println(ba.getBalance());
                }else{
                    System.out.println(jm.getMsg());
                }

            }while (flag);

            System.out.println("ATM退出");
        }catch (Exception e){
                e.printStackTrace();
        }
    }
}
