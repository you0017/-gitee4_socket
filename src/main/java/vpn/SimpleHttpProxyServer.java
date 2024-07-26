package vpn;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleHttpProxyServer {
    private static final int PORT = 8080; // 代理服务器监听的端口
    private static final String CACHE_DIR = "F:\\cache"; // 缓存目录
    private static final long CACHE_EXPIRY = 60 * 60 * 1000; // 缓存过期时间（毫秒），例如1小时

    public static void main(String[] args) {
        // 创建缓存目录
        new File(CACHE_DIR).mkdirs();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Proxy server is listening on port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClientRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) throws IOException {
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        // 读取客户端请求
        String requestLine = clientReader.readLine();
        if (requestLine == null || !requestLine.startsWith("GET")) {
            clientWriter.write("HTTP/1.1 400 Bad Request\r\n\r\n");
            clientWriter.flush();
            return;
        }

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 2) {
            clientWriter.write("HTTP/1.1 400 Bad Request\r\n\r\n");
            clientWriter.flush();
            return;
        }

        String url = requestParts[1];
        String filePath = CACHE_DIR + "/" + url.replaceAll("[^a-zA-Z0-9.-]", "_") + ".html";
        File cachedFile = new File(filePath);

        if (cachedFile.exists() && !isCacheExpired(cachedFile)) {
            // 读取缓存文件并返回给客户端
            byte[] fileBytes = Files.readAllBytes(cachedFile.toPath());
            clientWriter.write("HTTP/1.1 200 OK\r\n");
            clientWriter.write("Content-Length: " + fileBytes.length + "\r\n");
            clientWriter.write("Content-Type: text/html\r\n\r\n");
            clientWriter.flush();
            clientSocket.getOutputStream().write(fileBytes);
        } else {
            // 从目标网址下载页面并缓存
            URL targetUrl = new URL(url.substring(1)+":80");
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                byte[] responseBytes = new byte[1024];
                int len = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ( (len = connection.getInputStream().read(responseBytes)) != -1){
                    baos.write(responseBytes, 0, len);
                }

                // 写入缓存文件
                try (FileOutputStream fos = new FileOutputStream(cachedFile)) {
                    fos.write(baos.toByteArray());
                }

                // 返回响应给客户端
                clientWriter.write("HTTP/1.1 200 OK\r\n");
                clientWriter.write("Content-Length: " + responseBytes.length + "\r\n");
                clientWriter.write("Content-Type: text/html\r\n\r\n");
                clientWriter.flush();
                clientSocket.getOutputStream().write(responseBytes);
            } else {
                clientWriter.write("HTTP/1.1 " + responseCode + " " + connection.getResponseMessage() + "\r\n\r\n");
                clientWriter.flush();
            }
        }

        clientWriter.close();
        clientReader.close();
    }

    private static boolean isCacheExpired(File file) {
        long lastModified = file.lastModified();
        return (System.currentTimeMillis() - lastModified) > CACHE_EXPIRY;
    }
}

