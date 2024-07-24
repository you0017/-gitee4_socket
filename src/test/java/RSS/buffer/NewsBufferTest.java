package RSS.buffer;

import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import junit.framework.TestCase;

import java.util.List;

public class NewsBufferTest extends TestCase {
    private List<CommonInformationItem> list;
    private NewsBuffer buffer = new NewsBuffer();

    /**
     * 在下面testXxx()方法执行之前一定执行的提前操作
     * @throws Exception
     */
    public void setUp() throws Exception {
        RSSDataCapturer rssDataCapturer = new RSSDataCapturer("人民网");
        list = rssDataCapturer.load("http://www.people.com.cn/rss/world.xml");
    }

    public void testAdd() {

        for (CommonInformationItem item : list) {
            buffer.add(item);
        }
        assertNotNull(buffer);//断言buffer缓存非空
    }

    public void testGet(){
        testAdd();
        System.out.println("3306");
        CommonInformationItem item = buffer.get();
        System.out.println(item);
        assertNotNull(buffer);
    }
}