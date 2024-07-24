package RSS.reader;

import RSS.bean.CommonInformationItem;
import RSS.bean.RSSDataCapturer;
import RSS.buffer.NewsBuffer;
import org.apache.log4j.Logger;

import java.util.List;

public class NewsProductTask implements Runnable{
    private String name;
    private String url;
    private NewsBuffer buffer;
    private Logger log = Logger.getLogger(NewsProductTask.class);
    public NewsProductTask(String name, String url, NewsBuffer buffer)
    {
        this.name = name;
        this.url = url;
        this.buffer = buffer;
    }
    @Override
    public void run()
    {
        RSSDataCapturer capturer = new RSSDataCapturer(name);
        List<CommonInformationItem> list = capturer.load(url);
        //存到缓存队列
        for (CommonInformationItem item : list)
        {
            buffer.add(item);
        }
        log.info("下载:"+name+"源的"+url+"成功");
    }
}
