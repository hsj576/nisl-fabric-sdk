package com.rain.fabricdemo.test;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class SimpleHttpServer1 implements Runnable {

    ServerSocket serverSocket;//服务器Socket
    public static int PORT = 8080;//监听8080端口

    // fabric 相关
    WalletDemo walletDemo;

    // 开始服务器 Socket 线程.
    public SimpleHttpServer1() throws Exception {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (Exception e) {
            System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
        }
        if (serverSocket == null) System.exit(1);//无法开始服务器
        new Thread(this).start();
        System.out.println("HTTP server is running, port:" + PORT);

        walletDemo = new WalletDemo();
    }

    // 运行服务器主线程, 监听客户端请求并返回响应.
    public void run() {
        while (true) {
            try {
                Socket client = null;//客户Socket
                client = serverSocket.accept();//客户机(这里是 IE 等浏览器)已经连接到当前服务器
                if (client != null) {
                    System.out.println("connect to client:" + client);
                    try {
                        // 第一阶段: 打开输入流
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                client.getInputStream()));

                        System.out.println("receive client message: ***************");
                        // 读取第一行, 请求地址
                        String line = in.readLine();
                        System.out.println("line: " + line);
                        String resource = line.substring(line.indexOf('/') + 1, line.lastIndexOf('/') - 5);
                        //获得请求的资源的地址
                        resource = URLDecoder.decode(resource, "UTF-8");//反编码 URL 地址
                        String method = new StringTokenizer(line).nextElement().toString();// 获取请求方法, GET 或者 POST

                        // 读取所有浏览器发送过来的请求参数头部信息
                        while ((line = in.readLine()) != null) {
//                            System.out.println(line);
                            if (line.equals("")) break;  //读到尾部即跳出
                        }

                        System.out.println("end of receiving ***************");
                        System.out.println("resource:" + resource);
                        System.out.println("method: " + method);

//                        walletDemo.query();

                        client.close();
                    } catch (Exception e) {
                        System.out.println("HTTP server error:" + e.getLocalizedMessage());
                    }
                }
                //System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
            } catch (Exception e) {
                System.out.println("HTTP server error:" + e.getLocalizedMessage());
            }
        }
    }


    //命令行打印用途说明.
    private static void usage() {
        System.out.println("Usage: java HTTPServer <port> Default port is 80.");
    }

    /**
     * 启动简易 HTTP 服务器
     */
    public static void main(String[] args) throws Exception {

        try {
            if (args.length != 1) {
                usage();
            } else if (args.length == 1) {
                PORT = Integer.parseInt(args[0]);
            }
        } catch (Exception ex) {
            System.err.println("Invalid port arguments. It must be a integer that greater than 0");
        }

        new SimpleHttpServer1();   //创建一个
    }
}