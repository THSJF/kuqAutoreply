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

	public static final String GET_URL = " http://localhost:8080/welcome1 ";

	public static final String POST_URL = "http://api.live.bilibili.com/msg/send";

	public void readContentFromPost(long fromGroup, int roomId, long fromQQ, String msg) throws IOException {
		if (fromQQ != 2856986197L) {
			return;
		}
		// Post请求的url，与get不同的是不需要带参数
		URL postUrl = new URL(POST_URL);
		String content = "";
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		// Output to the connection. Default is
		// false, set to true because post
		// method must write something to the
		// connection
		// 设置是否向connection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true
		connection.setDoOutput(true);
		// Read from the connection. Default is true.
		connection.setDoInput(true);
		// Set the post method. Default is GET
		connection.setRequestMethod("POST");
		// Post cannot use caches
		// Post 请求不能使用缓存
		connection.setUseCaches(false);
		// This method takes effects to every instances of this class.
		// URLConnection.setFollowRedirects是static 函数，作用于所有的URLConnection对象。
		// connection.setFollowRedirects(true);

		// This methods only takes effacts to this instance.
		// URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数
		connection.setInstanceFollowRedirects(true);
		// Set the content type to urlencoded,
		// because we will write some URL-encoded content to the connection.
		// Settings above must be set before connect!
		// 配置本次连接的Content-type，配置为application/x- www-form-urlencoded的
		// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
		// 进行编码

		connection.setRequestProperty("Host", "api.live.bilibili.com");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		connection.setRequestProperty("Origin", "https://live.bilibili.com");
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("Referer", "https://live.bilibili.com/" + roomId);
		connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		if (fromQQ == 2856986197L) {
			connection.setRequestProperty("cookie",
					"fts=1509037707; sid=hwqicc8e; rpdid=omqxwqwkqpdoswlwkmxqw; pgv_pvi=2928584704; LIVE_BUVID=545efa86a230dcd329e38b73080179c5; LIVE_BUVID__ckMd5=99788681a69c645d; buvid3=08099554-0125-478D-94BA-08B2667C073914560infoc; UM_distinctid=165dd0db61f134-01fd72ef80ec3-6b1b1279-100200-165dd0db62019c; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1537110754,1537115478,1537115504; finger=05ae7751; im_seqno_64483321=12; im_local_unread_64483321=0; CURRENT_QUALITY=64; DedeUserID=64483321; DedeUserID__ckMd5=ae48d3a90319855b; SESSDATA=8a949502%2C1541579112%2C96f069d8; bili_jct=dac031192532b0a7402ec0e0b514ff8d; CURRENT_FNVAL=8; stardustvideo=1; im_notify_type_64483321=0; bp_t_offset_64483321=173187186050139381; _dfcaptcha=c6f9c3cd0bffbe6d35dbc9dfff1ffa2b");content = "color=16777215"
					+ "&fontsize=25"
					+ "&mode=1" 
					+ "&msg=" + encode(msg) 
					+ "&rnd=1539160506" 
					+ "&roomid="+ roomId 
					+ "&csrf_token=dac031192532b0a7402ec0e0b514ff8d";
			connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
		}
		// connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

		// 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
		// 要注意的是connection.getOutputStream会隐含的进行 connect。
		connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		// The URL-encoded contend
		// 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
		// 表单参数包括：（1）Param:案件类型:刑事案件（2）Index:1
		// （3）Page:5（4）Order:法院层级（5）Direction:asc
		// String content = "Param="
		// + URLEncoder.encode("案件类型:刑事案件 ", "utf-8");
		// DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
		out.writeBytes(content);

		out.flush();
		out.close(); // flush and close
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
	/*
	 * public static void readContentFromGet() throws IOException { //
	 * 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码 String getURL = GET_URL +
	 * " ?username= " + URLEncoder.encode(" fat man ", " utf-8 "); URL getUrl =
	 * new URL(getURL); // 根据拼凑的URL，打开连接，URL.openConnection函数会根据 URL的类型， //
	 * 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
	 * HttpURLConnection connection = (HttpURLConnection)
	 * getUrl.openConnection(); // 进行连接，但是实际上get request要在下一句的
	 * connection.getInputStream()函数中才会真正发到 // 服务器 connection.connect(); //
	 * 取得输入流，并使用Reader读取 BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(connection.getInputStream())); System.out.println(
	 * " ============================= "); System.out.println(
	 * " Contents of get request "); System.out.println(
	 * " ============================= "); String lines; while ((lines =
	 * reader.readLine()) != null) { System.out.println(lines); }
	 * reader.close(); // 断开连接 connection.disconnect(); System.out.println(
	 * " ============================= "); System.out.println(
	 * " Contents of get request ends "); System.out.println(
	 * " ============================= "); }
	 */

}
