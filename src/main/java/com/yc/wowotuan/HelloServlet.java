package com.yc.wowotuan;


import com.yc.tomcat2.javax.servlet.YcWebServlet;
import com.yc.tomcat2.javax.servlet.http.YcHttpServlet;
import com.yc.tomcat2.javax.servlet.http.YcHttpServletRequest;
import com.yc.tomcat2.javax.servlet.http.YcHttpServletResponse;

@YcWebServlet("/hello")
public class HelloServlet extends YcHttpServlet {
    public HelloServlet(){
        System.out.println("构造方法");
    }

    public void init()
    {
        System.out.println("init方法");
    }

    protected void doGet(YcHttpServletRequest request, YcHttpServletResponse response){
        System.out.println("hello world");
    }
}
