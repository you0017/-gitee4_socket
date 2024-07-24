package RSS.bean;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RSSDataCapturerTest extends TestCase {

    public void testDateFormat() throws Exception{
        String d = "Fri, 19 Jul 2024 18:26:01 +0800";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date dd = new Date();
        System.out.println(sdf.format(dd));
        System.out.println(sdf.parse(d));
    }

    public void testLoad() {
        RSSDataCapturer rssDataCapturer = new RSSDataCapturer("人民网");
        List<CommonInformationItem> list = rssDataCapturer.load("http://www.people.com.cn/rss/world.xml");
        for (CommonInformationItem item : list) {
            System.out.println(item);
        }
        System.out.println(list.size());
    }
}