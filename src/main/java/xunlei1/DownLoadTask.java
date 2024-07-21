package xunlei1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownLoadTask implements Runnable{
    private int i;
    private long fileSize;
    private int threadSize;
    private long sizePerThread;
    private String url;
    private String downloadPath;

    private Logger log = Logger.getLogger(DownLoadTask.class.getName());

    public DownLoadTask(int i, long fileSize, int threadSize, long sizePerThread, String url, String downloadPath) {
        this.i = i;
        this.fileSize = fileSize;
        this.threadSize = threadSize;
        this.sizePerThread = sizePerThread;
        this.url = url;
        this.downloadPath = downloadPath;
    }

    @Override
    public void run() {
        //计算此线程要下载的起始和终止位置
        long start = i * sizePerThread;
        long end = (i + 1) * sizePerThread - 1;//类似数组 0——length-1

        RandomAccessFile raf = null;
        BufferedInputStream bis = null;
        try {
            //让RandomAccessFile对象定位到起始位置
            raf = new RandomAccessFile(downloadPath, "rw");
            raf.seek(start);

            //3.开始下载 Range请求头
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
            connection.setConnectTimeout(3000);//超时时间
            connection.connect();//开始连接


            bis = new BufferedInputStream(connection.getInputStream());
            byte[] bs = new byte[1024];
            int len = -1;
            while ((len = bis.read(bs)) != -1) {
                raf.write(bs, 0, len);
                //log.info("线程" + i + "下载了" + len + "字节");
                Xunlei.dlSize.addAndGet(len);
                //System.out.println(Xunlei.dlSize);
            }
            System.out.println();
            log.info("线程" + i + "下载完成");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("下载异常"+e);
        }finally {
            if (bis!=null){
                try {
                    bis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (raf!=null){
                try {
                    raf.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        //4.流操作
    }
}
