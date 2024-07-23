package com.yc;

import com.yc.util.WeatherUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Test3_Server {
    private static Logger log = Logger.getLogger(Test3_Server.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //方案一：直接指定一个端口
        //方案二：循环10000以上的端口，试一个空闲的端口
        //方案三：将端口配置在一个  .xml|.properties文件中
        final int port = 10000;
        ServerSocket ss = null;
        for (int i = port; i < 65535; i++) {
            try {
                ss = new ServerSocket(i);
                log.info("服务器启动成功，端口号："+ss.getLocalPort());
                break;
            } catch (Exception e) {
                log.info("端口"+i+"被占用");
            }
        }
        //用info级别
        log.info(ss.getInetAddress().getHostAddress() + "启动了，监听端口号："+ss.getLocalPort());

        /*DateFormat dfm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date d = null;
        String time = null;*/

        while (true){
            log.info("服务端ServerSocket准备等待客户端的连接");
            //只要有一个客户端连接到服务器，获取一个与客户端的套接字Socket对象
            Socket s = ss.accept(); //阻塞式方法
            System.out.println(s.getInetAddress().getHostAddress());
            log.info("(理解阻塞的意思...)获取了一个与客户端："+s.getRemoteSocketAddress()+"的连接");

            Thread.sleep(5000);
            //TODO:服务器中的业务
            /*d = new Date();
            time = dfm.format(d);*/

            //套接字编程的底层就是io,这是什么流   输入|输出
            try (OutputStream oos = s.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(oos, "UTF-8"))) {


                String weather = WeatherUtil.weather("衡阳");
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new ByteArrayInputStream(weather.getBytes("UTF-8")));

                    NodeList nodes = doc.getElementsByTagName("string");
                    weather = "";
                    for (int i = 0; i < nodes.getLength(); i++) {
                        System.out.println(nodes.item(i).getTextContent());
                        weather += nodes.item(i).getTextContent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //表明使用dom4j中的sax解析方式
                //SAXReader saxReader = new SAXReader();
                //writer.write(weather);
                PrintWriter pw = new PrintWriter(writer);
                pw.println(weather);
                pw.flush();
                //writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }


            s.close();
            log.info("服务端断开与客户端的连接");
        }

    }
}
