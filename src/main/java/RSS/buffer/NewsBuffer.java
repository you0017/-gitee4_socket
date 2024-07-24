package RSS.buffer;

import RSS.bean.CommonInformationItem;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class NewsBuffer {
    //用于存新闻的有界阻塞队列
    private LinkedBlockingQueue<CommonInformationItem> buffer;

    //用于去重的map
    private ConcurrentHashMap<String, String> storedItem;  //已经保存的新闻id

    public NewsBuffer() {
        buffer = new LinkedBlockingQueue<>(1000);
        storedItem = new ConcurrentHashMap<>();
    }

    public void add(CommonInformationItem item) {
        if (storedItem.containsKey(item.getId())) {
            return;
        }
        storedItem.put(item.getId(), item.getId());
        buffer.add(item);
        //TODO：大数据的保存方式  hash算法  ->  位图  ->  布隆过滤器
    }

    public CommonInformationItem get() {
        try {
            return buffer.take();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
