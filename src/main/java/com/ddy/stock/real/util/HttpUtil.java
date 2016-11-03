package com.ddy.stock.real.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinchao
 * @time 2014-9-9 上午08:45:51
 * @description
 */
public class HttpUtil {
	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	public static String doGet(String url) {
		return executeGet(url);
	}

	private static String executeGet(String req) {
		HttpURLConnection conn = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			URL url = new URL(req); // 创建URL对象
			// 返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000); // 设置连接超时为5秒
			conn.setRequestMethod("GET"); // 设定请求方式
			conn.connect(); // 建立到远程对象的实际连接
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) { // 判断是否正常响应数据
				System.out.println("网络错误异常！!!!");
				log.error("network error:"+conn.getResponseCode());
				return "";
			}
			is = conn.getInputStream(); // 返回打开连接读取的输入流
			isr = new InputStreamReader(is, "GBK");
			br = new BufferedReader(isr);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		} finally {
			if(br !=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(isr!=null){
				try {
					isr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				conn.disconnect(); // 中断连接
			}
		}
		return "";
	}
	
	public static String executePost(String requestUrl,String content ){
    	HttpURLConnection httpURLConnection=null;
    	PrintWriter printWriter=null;
    	StringBuffer sb = new StringBuffer();
    	try {  
            URL realUrl = new URL(requestUrl);  
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
            httpURLConnection.setRequestProperty("accept", "*/*");  
            // 发送POST请求必须设置如下两行  
            httpURLConnection.setDoOutput(true);  
            httpURLConnection.setDoInput(true);  
            // 获取URLConnection对象对应的输出流  
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
            // 发送请求参数  
            printWriter.write(content);  
            // flush输出流的缓冲  
            printWriter.flush();  
            // 根据ResponseCode判断连接是否成功  
            int responseCode = httpURLConnection.getResponseCode();  
            if (responseCode != 200) {  
                log.error(" Error===" + responseCode);  
            }  
            // 定义BufferedReader输入流来读取URL的ResponseData  
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(  
                    httpURLConnection.getInputStream()));  
            String line;
            while ((line = bufferedReader.readLine()) != null) {  
            	sb.append(line);
            }  
        } catch (Exception e) {  
            log.error("send post request error!" + e);  
        } finally {  
            httpURLConnection.disconnect();  
            if (printWriter != null) {  
                printWriter.close();  
            } 
        }  
    	return sb.toString();
    }
    public static void main(String[] args) {
    	String content = "{userName:\"15824135596\",password:\"12345abc\"}";
    	String c = executePost("http://127.0.0.1:8080/ddy-ass/v1/uapi/user/login",content);
    	System.out.println(c);
	}
}
