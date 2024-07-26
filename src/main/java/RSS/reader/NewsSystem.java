package RSS.reader;

import RSS.buffer.NewsBuffer;
import RSS.writer.NewsWriterTask;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewsSystem implements Runnable{
    private String sourcesPath; //sources.txt文件地址
    private ScheduledThreadPoolExecutor executor;//定时调度的线程池
    private NewsBuffer buffer;
    private Logger log = Logger.getLogger(NewsSystem.class);
    public NewsSystem(String sourcesPath){
        this.sourcesPath = sourcesPath;
        executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        buffer = new NewsBuffer();
    }

    @Override
    public void run() {
        //消费任务
        NewsWriterTask writerTask = new NewsWriterTask(buffer);//消费
        executor.execute(writerTask);
        //executor.scheduleWithFixedDelay(writerTask,0,30, TimeUnit.SECONDS);
        //生产线程
        Path file = Paths.get(sourcesPath);
        try (BufferedReader reader = Files.newBufferedReader(file)){
            String line = null;
            while ((line = reader.readLine()) != null){
                String[] strs = line.split(";");
                if (strs!=null&&strs.length==2){
                    String rssName = strs[0];
                    String url = strs[1];
                    //创建生产者的任务
                    NewsProductTask productTask = new NewsProductTask(rssName,url,buffer);
                    executor.scheduleWithFixedDelay(productTask,0,30, TimeUnit.SECONDS);
                }
            }
        }catch (Exception e){
            log.error(e);
        }
    }
}
