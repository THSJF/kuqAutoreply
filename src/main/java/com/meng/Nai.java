package com.meng;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Nai {

	// public static final String GET_URL = " http://localhost:8080/welcome1 ";

	public static final String POST_URL = "http://api.live.bilibili.com/msg/send";

	public void readContentFromPost(long fromGroup, int roomId, long fromQQ, String msg) throws IOException {
		if (fromQQ != 2856986197L) {
			return;
		}
		URL postUrl = new URL(POST_URL);
		String content = "";//要发出的数据
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
		//发送弹幕貌似要检查User-Agent和cookie Referer貌似并没有检查但发送的数据中包含这一项于是就加上了
		connection.setRequestProperty("Host", "api.live.bilibili.com");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		connection.setRequestProperty("Origin", "https://live.bilibili.com");
		connection.setRequestProperty("User-Agent",
				"浏览器UA");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Referer", "https://live.bilibili.com/" + roomId);
		connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");

		connection.setRequestProperty("cookie",
				"浏览器cookie");
		content = "color=16777215" +
				"&fontsize=25" + 
				"&mode=1" +
				"&msg=" + encode(msg) + //发送的句子中特殊符号需要转换一下
				"&rnd=1539160506" + //看起来是时间戳
				"&roomid="+ roomId + 
				"&csrf_token=dac031192532b0a7402ec0e0b514ff8d";//暂时不明
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
		System.out.println(" ============================= ");
		System.out.println(" Contents of post request ");
		System.out.println(" ============================= ");
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println(" ============================= ");
		System.out.println(" Contents of post request ends ");
		System.out.println(" ============================= ");
		reader.close();
		connection.disconnect();
		if (fromGroup == -1) {
			Autoreply.sendPrivateMessage(fromQQ, roomId + "已奶");
		} else {
			Autoreply.sendGroupMessage(fromGroup, roomId + "已奶");
		}
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
