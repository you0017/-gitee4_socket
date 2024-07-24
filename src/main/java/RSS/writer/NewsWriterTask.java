package RSS.writer;

import RSS.bean.CommonInformationItem;
import RSS.buffer.NewsBuffer;
import RSS.dao.ProjectProperties;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 从缓存中取数据，写到磁盘(mongodb，数据库
 */
public class NewsWriterTask implements Runnable{
    private String name;//RSS源名称
    private NewsBuffer buffer;//缓存
    private boolean flag = false;
    private Logger logger = Logger.getLogger(NewsWriterTask.class);

    public NewsWriterTask(NewsBuffer buffer) {
        this.name = "位置数据源";
        this.buffer = buffer;
    }
    public NewsWriterTask(String name, NewsBuffer buffer) {
        this.name = name;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        //循环从buffer中去新闻，存到磁盘(mongodb
        while (!flag && !Thread.currentThread().isInterrupted()) {
            CommonInformationItem item = null;
            try {
                item = buffer.get();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            if (item == null) {
                continue;
            }
            //将此item写到磁盘
            Path p = Paths.get(ProjectProperties.getInstance().getProperty("file.path")+item.getFileName());
            //流:        创建一个缓冲字符流(因为可以一行一行写数据)create:每次都创建新文件
            try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.CREATE_NEW)){
                writer.write(item.toString());
                writer.flush();
            }catch (Exception e){
                logger.error("写文件失败",e);
            }
        }
    }
}
