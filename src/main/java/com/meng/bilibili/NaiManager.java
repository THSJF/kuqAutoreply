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
	public final String cookieSunny = "sid=5d245m2c; buvid3=6FE522EC-2313-4D06-B5FB-218F0ECD614240787infoc; DedeUserID=424436973; DedeUserID__ckMd5=9e1262810056755d; SESSDATA=2fd8d64b%2C1562489870%2Cf0715161; bili_jct=e5690748430c28d2e7916f16752b212d; _uuid=6FF4834C-2CD8-DC8D-31E7-19A8A9BFEC6870975infoc; LIVE_BUVID=AUTO5315598978735276";
	public final String cookieLuna = "sid=93yvcyb4; buvid3=D5700998-DE0C-493E-9094-6026E85ED20C110266infoc; DedeUserID=424444960; DedeUserID__ckMd5=0abcb1088f3a304d; SESSDATA=f18a2369%2C1562489899%2Ce0653761; bili_jct=381291a284e4407d6e7fb80c4d23a7db; _uuid=91AE6FBB-6257-92EC-B9B6-5239ECF5929998878infoc; LIVE_BUVID=AUTO3315598979008780";
	public final String cookieStar = "sid=68su41bu; buvid3=0005D079-181F-43AF-BF08-4AE9606BC3F2110255infoc; DedeUserID=424461971; DedeUserID__ckMd5=57442de70495d2e0; SESSDATA=a413f989%2C1562489945%2C526b2661; bili_jct=9f499ecfc9483d576674209ba59bcf28; _uuid=B88E3244-97CD-0B47-A24D-977C99B043DE45625infoc; LIVE_BUVID=AUTO8415598979476280";

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
