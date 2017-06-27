package org.wing4j.http.doc;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class HTTPServerAPITester {
    public static void main(String[] args) { 
        try { 
            HttpServer hs = HttpServer.create(new InetSocketAddress(8818),0);//设置HttpServer的端口为8888
            hs.createContext("/china", new MyHandler());//用MyHandler类内处理到/chinajash的请求 
            hs.setExecutor(null); // creates a default executor 
            hs.start(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
} 

class MyHandler implements HttpHandler { 
   public void handle(HttpExchange t) throws IOException { 
       String response = "我中华人民共和国ChinajashHappy New Year 2007!--Chinajash";
       t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
   OutputStream os = t.getResponseBody(); 
   System.out.println(response); 
   os.write(response.getBytes()); 
   os.close(); 
   } 
} 