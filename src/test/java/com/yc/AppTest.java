package com.yc;

import RSS.bean.RSSDataCapturerTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        //测试套件：一个测试套件可以包含多个测试用例，一个测试用例通常是对应一个类或模块
        TestSuite suite = new TestSuite(AppTest.class);
        suite.addTest(new RSSDataCapturerTest());
        return suite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
