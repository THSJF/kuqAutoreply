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
    private final String cookieSunny = "sid=iuddp3q8; buvid3=88CE640D-B46B-46F5-8A6D-4F74C54BC3A440753infoc; DedeUserID=424436973; DedeUserID__ckMd5=9e1262810056755d; SESSDATA=de997eb6%2C1565110064%2Ccaa47c71; bili_jct=8b6bc3ad47a2605fe80f9b7235c32f21; _uuid=1EA28D30-7F0B-0B7F-E2DC-9A05DA46349954313infoc; LIVE_BUVID=AUTO3815625180672914";
    private final String cookieLuna = "sid=j4oxqgie; buvid3=DD86DBE2-AE29-4848-AEAC-294F8517C320110262infoc; DedeUserID=424444960; DedeUserID__ckMd5=0abcb1088f3a304d; SESSDATA=fd06b264%2C1565110091%2Ca344ea71; bili_jct=b2c274a72a0b21d303ba4dfb09c2e6b3; _uuid=56E84548-BAAE-476F-2B18-A0FE44604E0E80514infoc; LIVE_BUVID=AUTO7315625180939643";
    private final String cookieStar = "sid=i7440w68; buvid3=D16A53C4-2628-4845-9773-5E23A6C857CF40779infoc; DedeUserID=424461971; DedeUserID__ckMd5=57442de70495d2e0; SESSDATA=909c5a67%2C1565110113%2C3a046a71; bili_jct=44a538c1d3ca73506a9b953173bf5477; _uuid=28B6C8C5-F955-1D29-0571-A65655F2F0FD02644infoc; LIVE_BUVID=AUTO5015625181152097";
    private final String cookieXingHUo = "sid=cebgzd0p; buvid3=3AF52A07-66A8-4703-8502-D1ED49D5F8B7110244infoc; DedeUserID=32845432; DedeUserID__ckMd5=6a8a7473e1de5637; SESSDATA=61942582%2C1565164757%2C1ffe6c71; bili_jct=4fbf55846efc86f6980fab92c33d289d; _uuid=C2C713A5-6F78-7670-224D-8954E22E6AD847395infoc; LIVE_BUVID=AUTO8615625727608501";

    public void checkXinghuo(long fromGroup, int roomId, long fromQQ, String msg) {
        try {
            sendDanmaku(roomId, cookieXingHUo, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Autoreply.sendMessage(fromGroup, fromQQ, roomId + "已奶");
    }

    public void check(long fromGroup, int roomId, long fromQQ, String msg) {
        try {
            sendDanmaku(roomId, cookieSunny, msg);
            sendDanmaku(roomId, cookieLuna, msg);
            sendDanmaku(roomId, cookieStar, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Autoreply.sendMessage(fromGroup, fromQQ, roomId + "已奶");
    }

    private void sendDanmaku(int roomId, String cookie, String msg) throws IOException {
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
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
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
