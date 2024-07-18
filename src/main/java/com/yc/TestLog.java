package com.yc;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class TestLog
{
    private static Logger logger = Logger.getLogger(TestLog.class.getName());
    public static void main( String[] args )
    {
        logger.info("hello");
        System.out.println( "Hello World!" );
    }
}
