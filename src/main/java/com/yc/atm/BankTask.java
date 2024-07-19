package com.yc.atm;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTask implements Runnable{
    private Socket socket;
    private Bank b;
    private boolean flag;

    public BankTask(Socket socket, Bank b) {
        this.socket = socket;
        this.b = b;
        this.flag = true;
    }

    @Override
    public void run() {
        try (   //因为要用try-with-resources
                Socket s = this.socket;
                Scanner reader = new Scanner(s.getInputStream());
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                ){
            while (flag&&!Thread.currentThread().isInterrupted()){
                //客户端是否没有传数据
                if (!reader.hasNext()){
                    System.out.println("atm客户端："+s.getRemoteSocketAddress()+"掉线了");
                    break;
                }
                //如果有信息则取出
                String command = reader.next();
                BankAccount ba = null;
                JsonModel<BankAccount> jm = new JsonModel<>();
                //命令模式
                if ("DEPOSIT".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    double money = reader.nextDouble();
                    ba = b.deposit(id,money);
                }else if ("WITHDRAW".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    double money = reader.nextDouble();
                    ba = b.withdraw(id,money);
                }else if ("BALANCE".equalsIgnoreCase(command)){
                    int id = reader.nextInt();
                    ba = b.search(id);
                } else if ("QUIT".equalsIgnoreCase(command)) {
                    System.out.println("atm客户端要求主动断开..."+s.getRemoteSocketAddress());
                    break;
                }else{
                    jm.setCode(0);
                    jm.setMsg("错误命令");
                    Gson g = new Gson();
                    String json = g.toJson(jm);
                    pw.println(json);
                    pw.flush();
                    continue;
                }
                //TODO:将基本字符转为json

                jm.setCode(1);
                jm.setData(ba);
                Gson g = new Gson();
                String json = g.toJson(jm);
                pw.println(json);
                pw.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
