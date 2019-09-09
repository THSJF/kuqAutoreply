package com.meng.bilibili.live;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.meng.Autoreply;
import com.meng.tools.Methods;

public class NaiManager {

    private final String POST_URL = "http://api.live.bilibili.com/msg/send";
    
    public void checkXinghuo(long fromGroup, String roomId, long fromQQ, String msg) {
        try {
            sendDanmaku(roomId, Autoreply.instence.cookieManager.cookie.xinghuo, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Autoreply.sendMessage(fromGroup, fromQQ, roomId + "已奶");
    }
	
	public String sendChat(String roomId, String msg) {
		try {
            sendDanmaku(roomId, Autoreply.instence.cookieManager.cookie.Hina, msg);
		  } catch (IOException e) {
            return e.toString();
		  }
        return "";
	}

    public void check(long fromGroup, String roomId, long fromQQ, String msg) {
        try {
            sendDanmaku(roomId, Autoreply.instence.cookieManager.cookie.Sunny, msg);
            sendDanmaku(roomId, Autoreply.instence.cookieManager.cookie.Luna, msg);
            sendDanmaku(roomId, Autoreply.instence.cookieManager.cookie.Star, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Autoreply.sendMessage(fromGroup, fromQQ, roomId + "已奶");
    }

    public void sendDanmaku(String roomId, String cookie, String msg) throws IOException {
        URL postUrl = new URL(POST_URL);
        String content = "";// 要发出的数据
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Host", "api.live.bilibili.com");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Origin", "https://live.bilibili.com");
        connection.setRequestProperty("User-Agent", Autoreply.instence.userAgent);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Referer", "https://live.bilibili.com/" + roomId);
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        connection.setRequestProperty("cookie", cookie);
        String scrfStr = Methods.cookieToMap(cookie).get("bili_jct");
        content = "color=16777215&" + "fontsize=25&" + "mode=1&" + "bubble=0&" + "msg=" + encode(msg) + // 发送的句子中特殊符号需要转换一下
                "&rnd=" + (System.currentTimeMillis() / 1000) + "&roomid=" + roomId + "&csrf=" + scrfStr
                + "&csrf_token=" + scrfStr;
        connection.setRequestProperty("Content-Length", String.valueOf(content.length()));

        // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行 connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(content);
        out.flush();
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        System.out.println(" Contents of post request ");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println(" Contents of post request ends ");
        reader.close();
        connection.disconnect();
    }

    public String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "Issue while encoding" + e.getMessage();
        }
    }

}
