package com.meng;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
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
		return false;
	}

	// 膜人回复
	public static boolean checkMo(long fromGroup, String msg) throws IOException {
		// 使用了正则表达式
		if (Pattern.matches(
				".*(([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].*(t.*c.*l|t.*q.*l|太.*[触觸].*了)|.*([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].{0,3})",
				msg.replace(" ", "").trim())) {
			Autoreply.sendGroupMessage(fromGroup, "打不过地灵殿Normal");
			Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.image(new File(Autoreply.appDirectory + "pic\\fanmo.jpg")));
			return true;
		}
		return false;
	}

	// 读取文本文件
	public static String readToString(String fileName) throws IOException, UnsupportedEncodingException {
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
	public static long parseLong(String s) throws NumberFormatException{
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

	// 输入网址返回网页源码
	@SuppressWarnings({ "deprecation", "null", "restriction" })
	public static String open(String url) throws NoSuchAlgorithmException, KeyManagementException {
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
		} catch (ConnectException e) {
			System.out.println("ConnectException");
			System.out.println(e);
		} catch (IOException e) {
			System.out.println("IOException");
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

}
