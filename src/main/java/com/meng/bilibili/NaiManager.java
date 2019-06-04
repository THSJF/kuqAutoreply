package com.meng.bilibili;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.meng.Autoreply;

public class NaiManager {

	public final String POST_URL = "http://api.live.bilibili.com/msg/send";
	public final String cookieSunny = "sid=k56idlfb; buvid3=FB8A8514-E46B-4241-B208-39354E432A8740787infoc; DedeUserID=424436973; DedeUserID__ckMd5=9e1262810056755d; SESSDATA=7a0ff344%2C1559893859%2C80d0ec51; bili_jct=1d0906de2ba4e6652456a13df99a5d31; _uuid=6C63B475-88A3-C07E-6703-2C653D58869762776infoc; LIVE_BUVID=AUTO5615573018638405";
	public final String cookieLuna = "sid=b6g7is1s; buvid3=CCD3714C-12B9-411D-A879-7AD48F42608D40767infoc; DedeUserID=424444960; DedeUserID__ckMd5=0abcb1088f3a304d; SESSDATA=376c7bf7%2C1559893898%2C7400f951; bili_jct=b369f9d0a3d9354c288169f0200f7b08; _uuid=995A48AA-16C3-CC4A-F387-C51FEAFD432D00434infoc; LIVE_BUVID=AUTO1315573019008782";
	public final String cookieStar = "sid=4ns19hr6; buvid3=360AABDE-2D2A-4355-B4C7-26039D918C2440762infoc; DedeUserID=424461971; DedeUserID__ckMd5=57442de70495d2e0; SESSDATA=cec8dc9f%2C1559894112%2C6d7b5651; bili_jct=5082bb8b63bfa8373612375aaeab94a1; _uuid=D3F7CB09-0240-3862-14F1-00643882C9B314874infoc; LIVE_BUVID=AUTO6715573021144850";

	public void check(long fromGroup, int roomId, long fromQQ, String msg) {
		if (fromQQ != 2856986197L) {
			return;
		}
		try {
			sendDanmaku(roomId, cookieSunny, msg);
			sendDanmaku(roomId, cookieLuna, msg);
			sendDanmaku(roomId, cookieStar, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fromGroup == -1) {
			Autoreply.sendMessage(0, fromQQ, roomId + "已奶");
		} else {
			Autoreply.sendMessage(fromGroup, 0, roomId + "已奶");
		}
	}

	public void sendDanmaku(int roomId, String cookie, String msg) throws IOException {
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
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Referer", "https://live.bilibili.com/" + roomId);
		connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		connection.setRequestProperty("cookie", cookie);
		String scrfStr = cookieToMap(cookie).get("bili_jct");
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

	public Map<String, String> cookieToMap(String value) {
		Map<String, String> map = new HashMap<String, String>();
		String values[] = value.split("; ");
		for (String val : values) {
			String vals[] = val.split("=");
			if (vals.length == 2) {
				map.put(vals[0], vals[1]);
			} else if (vals.length == 1) {
				map.put(vals[0], "");
			}
		}
		return map;
	}

	public String encode(String url) {
		try {
			String encodeURL = URLEncoder.encode(url, "UTF-8");
			return encodeURL;
		} catch (UnsupportedEncodingException e) {
			return "Issue while encoding" + e.getMessage();
		}
	}

}
