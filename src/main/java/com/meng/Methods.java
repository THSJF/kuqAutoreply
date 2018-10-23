package com.meng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Methods {
	private static String motmp = "";

	// 主开关
	public static boolean checkSwitch(long fromGroup, String msg) {
		if (msg.equals(".stop")) {
			Autoreply.sendGroupMessage(fromGroup, "disabled");
			Autoreply.enable = false;
			return true;
		}
		if (msg.equals(".start")) {
			Autoreply.enable = true;
			Autoreply.sendGroupMessage(fromGroup, "enabled");
			return true;
		}
		return false;
	}

	// randomFromArray 随机返回数组中的一项
	public static Object rfa(Object[] array) {
		return array[Autoreply.random.nextInt(2147483647) % array.length];
	}

	// 有@的时候
	public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
		if (Autoreply.CC.getAt(msg) == 1620628713L) {
			// 过滤特定的文字
			if (msg.indexOf("蓝") != -1 || msg.indexOf("藍") != -1) {
				return true;
			}
			// @消息发送者并复读内容
			Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + msg.substring(msg.indexOf(" ") + 1));
			return true;
		}
		return false;
	}

	// 有人发送分享链接时
	public static boolean checkLink(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:share,url=")) {// 分享链接的特征
			// 截取相关字符串
			String link = msg.substring(msg.indexOf("http"), msg.indexOf(",title="));
			String title = msg.substring(msg.indexOf("title=") + 6, msg.indexOf(",content"));
			String describe = msg.substring(msg.indexOf("content=") + 8, msg.indexOf(",image"));
			String picture = msg.substring(msg.lastIndexOf("http"), msg.lastIndexOf("]"));
			// 发送消息
			Autoreply.sendGroupMessage(fromGroup,
					"标题:" + title + "\n链接:" + link + "\n封面图:" + picture + "\n描述:" + describe);
			return true;
		}
		if (msg.startsWith("[CQ:rich,url=")) {// 某些分享链接是rich
			// 截取相关字符串
			try {
				String link = msg.substring(msg.indexOf("http"), msg.indexOf(",text="));
				String text = msg.substring(msg.indexOf("text=") + 5, msg.indexOf("]"));
				// 发送消息
				Autoreply.sendGroupMessage(fromGroup, "链接:" + link + "\n文字:" + text.trim());
			} catch (Exception e) {
				return true;
			}
			return true;
		}
		return false;
	}

	// 膜人回复
	public static boolean checkMo(long fromGroup, String msg) {
		// 使用了正则表达式
		if (Pattern.matches(
				".*(([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].*(t.*c.*l|t.*q.*l|太.*[触觸].*了)|.*([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].{0,3})",
				msg.replace(" ", "").trim())) {
			Autoreply.sendGroupMessage(fromGroup, "打不过地灵殿Normal");
			try {
				Autoreply.sendGroupMessage(fromGroup,
						Autoreply.CC.image(new File(Autoreply.appDirectory + "pic\\fanmo.jpg")));
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	// 读取文本文件
	public static String readFileToString(String fileName) throws IOException, UnsupportedEncodingException {
		String encoding = "UTF-8";
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		return new String(filecontent, encoding);
	}

	// 删除字符串中指定位置的文字
	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	// 删除字符串两端
	public static String removeCharAtStartAndEnd(String s) {
		String tmp = removeCharAt(s, 0);
		return removeCharAt(tmp, tmp.length() - 1);
	}

	// 字符串转换long
	public static long parseLong(String s) throws NumberFormatException {
		return Long.parseLong(s.replace("\"", ""));
	}

	// 暴力行为
	public static boolean checkGou(long fromGroup, String msg) {

		if (msg.equals("苟") || msg.equals("苟？") || msg.equals("苟?")) {
			motmp = "利";
			Autoreply.sendGroupMessage(fromGroup, "利");
			return true;
		} else if (msg.equals("国") && motmp.equals("利")) {
			motmp = "家";
			Autoreply.sendGroupMessage(fromGroup, "家");
			return true;
		} else if (msg.equals("生") && motmp.equals("家")) {
			motmp = "死";
			Autoreply.sendGroupMessage(fromGroup, "死");
			return true;
		} else if (msg.equals("以") && motmp.equals("死")) {
			motmp = "岂";
			Autoreply.sendGroupMessage(fromGroup, "岂");
			return true;
		} else if (msg.equals("因") && motmp.equals("岂")) {
			motmp = "祸";
			Autoreply.sendGroupMessage(fromGroup, "祸");
			return true;
		} else if (msg.equals("福") && motmp.equals("祸")) {
			motmp = "避";
			Autoreply.sendGroupMessage(fromGroup, "避");
			return true;
		} else if (msg.equals("趋") && motmp.equals("避")) {
			motmp = "之";
			Autoreply.sendGroupMessage(fromGroup, "之");
			return true;
		} else if (msg.equals("苟利国家生死以")) {
			Autoreply.sendGroupMessage(fromGroup, "岂因祸福避趋之");
			return true;
		}
		return false;
	}

	// 萌二
	public static boolean checkMeng2(long fromGroup, String msg) {

		if (msg.equals("此")) {
			motmp = "生";
			Autoreply.sendGroupMessage(fromGroup, "生");
			return true;
		} else if (msg.equals("无") && motmp.equals("生")) {
			motmp = "悔";
			Autoreply.sendGroupMessage(fromGroup, "悔");
			return true;
		} else if (msg.equals("入") && motmp.equals("悔")) {
			motmp = "东";
			Autoreply.sendGroupMessage(fromGroup, "东");
			return true;
		} else if (msg.equals("方") && motmp.equals("东")) {
			motmp = "来";
			Autoreply.sendGroupMessage(fromGroup, "来");
			return true;
		} else if (msg.equals("世") && motmp.equals("来")) {
			motmp = "愿";
			Autoreply.sendGroupMessage(fromGroup, "愿");
			return true;
		} else if (msg.equals("生") && motmp.equals("愿")) {
			motmp = "幻";
			Autoreply.sendGroupMessage(fromGroup, "幻");
			return true;
		} else if (msg.equals("想") && motmp.equals("幻")) {
			motmp = "乡";
			Autoreply.sendGroupMessage(fromGroup, "乡");
			return true;
		} else if (msg.equals("此生无悔入东方")) {
			Autoreply.sendGroupMessage(fromGroup, "来世愿生幻想乡");
			return true;
		}
		return false;
	}

	/*
	 * 下面都是连接网络需要的 直接从百度复制粘贴
	 */
	public static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	public static String openUrlWithHttps(String url) throws NoSuchAlgorithmException, KeyManagementException {
		return openUrlWithHttps(url, null);
	}

	// 输入网址返回网页源码
	@SuppressWarnings({ "deprecation", "null", "restriction" })
	public static String openUrlWithHttps(String url, String cookie)
			throws NoSuchAlgorithmException, KeyManagementException {
		InputStream in = null;
		OutputStream out = null;
		String str_return = "";
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			if (cookie != null) {
				conn.setRequestProperty("cookie", cookie);
			}
			conn.connect();
			InputStream is = conn.getInputStream();
			DataInputStream indata = new DataInputStream(is);
			String ret = "";
			while (ret != null) {
				ret = indata.readLine();
				if (ret != null && !ret.trim().equals("")) {
					str_return = str_return + ret;
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("ConnectException");
			System.out.println(e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}

		}
		return str_return;
	}

	public static String getRealUrl(String surl) {
		String realUrl = "";
		String line;
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		try {
			URL url = new URL(surl);
			URLConnection conn = url.openConnection();
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String nurl = conn.getURL().toString();
			realUrl = nurl;
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return realUrl;
	}

	public static String openUrlWithHttp(String url) {
		String result = "";
		String line;
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			result = sb.toString();
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
		return result;
	}

	public static String getStringBetween(String str, String start, String end, int index) {

		int flagA = str.indexOf(start, index);
		int flagB = str.indexOf(end, flagA + 1);
		if (flagA < 0 || flagB < 0) {
			return null;
		} else {
			flagA = flagA + start.length();
			if (flagA < 0 || flagB < 0) {
				return null;
			}
			return str.substring(flagA, flagB);
		}
	}

	public static String getG_tk(String skey) {
		int hash = 5381;
		int flag = skey.length();
		for (int i = 0; i < flag; i++) {
			hash = hash + hash * 32 + skey.charAt(i);
		}
		return String.valueOf(hash & 0x7fffffff);
	}

}
