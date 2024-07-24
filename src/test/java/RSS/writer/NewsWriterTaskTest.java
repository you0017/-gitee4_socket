package RSS.writer;

import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import RSS.buffer.NewsBuffer;
import junit.framework.TestCase;

import java.util.List;

public class NewsWriterTaskTest extends TestCase {
    private List<CommonInformationItem> list;
    private NewsBuffer buffer = new NewsBuffer();

    /**
     * 在下面testXxx()方法执行之前一定执行的提前操作
     * @throws Exception
     */
    public void setUp() throws Exception {
        RSSDataCapturer rssDataCapturer = new RSSDataCapturer("人民网");
        list = rssDataCapturer.load("http://www.people.com.cn/rss/world.xml");
        for (CommonInformationItem item : list) {
            buffer.add(item);
        }

    }

    public void testTestRun() throws InterruptedException {
        NewsWriterTask newsWriterTask = new NewsWriterTask(buffer);
        Thread t = new Thread(newsWriterTask);
        t.start();
        t.join();   //新创建的线程在测试框架中以后台运行，所以这里让测试线程等待新线程结束
    }
}