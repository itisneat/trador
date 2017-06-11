package org.bitcoin.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0";
    
    static HttpConnectionManager hcm = new HttpConnectionManager();
    
    static {
    	hcm.init();
    }
    
    static class HttpConnectionManager {

        PoolingHttpClientConnectionManager cm = null;
        
        public void init() {
            LayeredConnectionSocketFactory sslsf = null;
            try {
                sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
            		.register("https", sslsf)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(20);
            cm.setDefaultMaxPerRoute(20);
        }

        public CloseableHttpClient getHttpClient() {       
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();          
            
            return httpClient;
        }
    }
    
    
    

    public static Connection getConnectionForPost(String url, Map<String, String> datas) {
        url = appendHttpString(url);
        Connection connection = Jsoup.connect(url)
                .userAgent(USER_AGENT).timeout(5000)
                .method(Connection.Method.POST);
        if (datas != null && !datas.isEmpty()) {
            connection.data(datas);
        }
        return connection;
    }

    private static String appendHttpString(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    public static Connection getConnectionForGetNoCookies(String url, Map<String, String>... datas) {
        url = appendHttpString(url);
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).ignoreContentType(true).timeout(5000);
        if (datas != null && datas.length > 0 && !datas[0].isEmpty()) {
            connection.data(datas[0]);
        }

        return connection;
    }

    public static Connection getConnectionForGet(String url, Map<String, String>... datas) {
        return getConnectionForGetNoCookies(url, datas);
    }

    public static String getContentForGet(String url, int timeout) {
        try {
            Document objectDoc;
            try {
                Connection connection = getConnectionForGetNoCookies(url).timeout(timeout);
                objectDoc = connection.get();
            } catch (SocketTimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    // ignore
                }
                Connection connection = getConnectionForGetNoCookies(url).timeout(timeout);
                objectDoc = connection.get();
            }
            return objectDoc.body().text();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMethod(String url, int timeout) {  
        CloseableHttpClient httpclient = hcm.getHttpClient();  
        CloseableHttpResponse response = null;
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url); 
            httpget.setConfig(RequestConfig.custom().setConnectTimeout(timeout).build());
            // 执行get请求.    
            response = httpclient.execute(httpget);  
            
         // 获取响应实体    
            HttpEntity entity = response.getEntity();  
            if (entity != null) {  
//                // 打印响应内容长度    
//                System.out.println("Response content length: " + entity.getContentLength());  
//                // 打印响应内容    
//                System.out.println("Response content: " + EntityUtils.toString(entity)); 
                return EntityUtils.toString(entity);
            }  
            return "";
            
            
        } catch (Exception e) {
        	System.out.println("get method failed. Msg [" + e.getMessage() + "]");
            return "";
        } finally {  
        	try {
        		response.close();
        	} catch (Exception e) {
//        		e.printStackTrace();
        	}
            	
        }  
    }  
    
    
    
    public static void appadd(String jsonString) {

        try {
            //创建连接
            URL url = new URL("http://120.55.171.131:42421/api/put");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            
//            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setChunkedStreamingMode(4096);

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());

            out.writeBytes(jsonString);
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Post to 120.55.171.131 failed. msg [" + e.getMessage() + "]");;
        } 

    }
}
