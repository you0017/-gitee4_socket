package xunlei1;

import com.yc.Test5_talkClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Xunlei {
    private static Logger log = Logger.getLogger(Xunlei.class.getName());

    //volatile->修饰属性
    //public static volatile Long dlSize = 0l;
    public static AtomicLong dlSize = new AtomicLong(0l);

    private static volatile long fileSizeDownLoaded = 0;

    public static void main(String[] args) throws Exception {
        //String url = "http://yun.zfshx.com/uploads/2024/05/07/KpyUw6Pc_smee.7z";
        String url = "http://www.hostbuf.com/downloads/finalshell_install.exe";
        //1.获取要下载文件的大小
        long fileSize = getDownloadFileSize(url);
        log.info("文件大小：" + fileSize);

        //2.新文件保存的位置
        String newFileName = geFileName(url);
        log.info("新文件名：" + newFileName);
        String savePath = "F:\\baiduiwet";
        String downloadPath = savePath + File.separator + newFileName;
        log.info("下载路径：" + downloadPath);

        //3.创建此新文件(空，但是有大小)
        RandomAccessFile raf = new RandomAccessFile(downloadPath, "rw");
        raf.setLength(fileSize);
        raf.close();
        log.info("创建空文件成功");

        //4.本机的核数=》线程数
        int threadSize = Runtime.getRuntime().availableProcessors();

        //5.计算每个线程要下载的字节数
        long sizePerThread = getSizePerThread(fileSize, threadSize);
        log.info("每个线程要下载的字节数：" + sizePerThread);

        //6.循环创建线程，每个线程要下载自己的部分
        for (int i = 0; i < threadSize; i++) {
            DownLoadedSizeNotify dlsn = new DownLoadedSizeNotify() {
                @Override
                public void notifySize(long size) {
                    /*synchronized (Xunlei.class){
                        fileSizeDownLoaded += size;
                    }*/
                    //System.out.println("下载的文件总大小:" + size + "字节");
                    Xunlei.dlSize.addAndGet(size);
                }
            };
            DownLoadTask task = new DownLoadTask(i, fileSize, threadSize, sizePerThread, url, downloadPath, dlsn);
            Thread t = new Thread(task);
            t.start();
        }
        while (true) {
            if (dlSize.get() == fileSize) {
                log.info("下载完成");
                System.out.println("总大小:"+dlSize.get());
                break;
            }else {
                //log.info("\r"+"下载进度：" + dlSize + "，" + (dlSize * 100 / fileSize) + "%");
                System.out.print("\r"+"下载进度：" + dlSize.get() + "，" + (dlSize.get() * 100 / fileSize) + "%");
            }
            Thread.sleep(50);
        }
        System.out.println(fileSizeDownLoaded);
    }

    /**
     * 计算每个线程要下载的字节数
     *
     * @param fileSize
     * @param threadSize
     * @return
     */
    private static long getSizePerThread(long fileSize, int threadSize) {

        return fileSize % threadSize == 0 ? fileSize / threadSize : fileSize / threadSize + 1;
    }

    /**
     * 根据日期和时间生成新文件的文件名
     */
    private static String geFileName(String url) {
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = df.format(d);

        //后缀名
        String suffix = url.substring(url.lastIndexOf("."));
        return prefix + suffix;
    }


    /**
     * 正是下载前，获取文件大小  "HEAD"
     */
    private static long getDownloadFileSize(String url) throws Exception {
        long fileSize = 0;
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        //设置请求头
        con.setRequestMethod("HEAD"); //head不下载文件内容，取响应头域(文件大小
        con.setConnectTimeout(30000);
        con.connect();

        fileSize = con.getContentLength();
        log.info("文件大小：" + fileSize);
        return fileSize;

    }
}

/**
 * 下载的数据量的回调接口
 */
interface DownLoadedSizeNotify{
    public void notifySize(long size);
}