package com.rain.fabricdemo.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer2 {

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/hello", new TestHandler());
        server.start();
    }

    static  class TestHandler implements HttpHandler{
        WalletDemo walletDemo;
        TestHandler() throws Exception {
            walletDemo = new WalletDemo();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "hello";
            walletDemo.query();
//            System.out.println(response);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}