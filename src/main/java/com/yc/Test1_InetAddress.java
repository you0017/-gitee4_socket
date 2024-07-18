package com.yc;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddress案例
 */
public class Test1_InetAddress {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress ia1 = InetAddress.getLocalHost();
        System.out.println(ia1);


        InetAddress[] ia2 = InetAddress.getAllByName("ffgal.com");
        for (InetAddress inetAddress : ia2) {
            System.out.println(inetAddress);
        }
    }
}
