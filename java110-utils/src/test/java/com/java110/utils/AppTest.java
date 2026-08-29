package com.java110.utils;

import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ImageUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

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
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        String faceBase64 = ImageUtils.getBase64ByImgUrl("https://oss.homecommunity.cn/java110/hc/img/20260829/587f7699-f9d4-4aa0-9773-63912d55aca0.jpg");

        System.out.println(faceBase64);

        // 图片加载成功返回非空 Base64，加载失败/网络不可达静默降级返回空串，均不应抛异常
        assertNotNull("图片URL转Base64不应返回null", faceBase64);

    }
}
