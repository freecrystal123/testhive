package webtest;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.*;
import java.util.*;
public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {
        // 创建一个 HttpServer，绑定到本地地址和端口 8080
            try {
                // 获取所有网络接口
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();

                    // 获取与此网络接口相关的所有IP地址
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        // 排除回环地址 (127.0.0.1) 和 IPv6 地址
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            System.out.println("本机可访问的IP地址: " + inetAddress.getHostAddress());
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 设置处理根路径 "/"
        server.createContext("/", new MyHandler());

        // 启动服务器
        server.start();

        System.out.println("HTTP 服务已启动，监听端口 8080...");
    }

    // 处理 HTTP 请求的类
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello, World! 这是一个简单的 HTTP 服务。";
            exchange.sendResponseHeaders(200, response.getBytes().length);

            // 获取输出流并写入响应内容
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
