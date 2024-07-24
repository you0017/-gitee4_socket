package RSS;

import RSS.reader.NewsSystem;

import java.io.File;

public class App {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator +"RSS" + File.separator + "sources.txt";
        NewsSystem ns = new NewsSystem(path);
        Thread t = new Thread(ns);
        t.start();
    }
}
