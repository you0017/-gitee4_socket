package com.yc.tomcat2.javax.servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用程序上下文类
 */
public class YcServletContext {
    /**
     * url地址,servlet字节码路径
     */
    public static Map<String,Class> servletClass = new ConcurrentHashMap<>();
}
