package com.meng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.meng.bilibili.LiveManager;
import com.meng.lookGroup.IPGetter;

public class Methods {
	private static String motmp = "";
	private static String meng2tmp = "";
	public static final String helpMenu = "当前有二维码生成与识别，发送“二维码帮助”可获得使用方法."
			+ "图片搜索，发送“sp.help”可获得使用方法.窥屏检测,群内发送“窥屏检测”然后稍等片刻. 哔哩哔哩链接详情，群内发送一个哔哩哔哩视频链接或专栏链接即可触发."
			+ "苟利……(不解释，，，)。此生无悔……（，，）.roll，发送“roll.help”可获得使用方法."
			+ "催更，发送“xx今天更了吗”即可触发，需要在配置文件中有这位up的信息. 复读和根据词库相应消息（不解释，，，）";
	static int arrayFlag = new java.util.Random().nextInt(5000);

	// 主开关
	public static boolean checkSwitch(long fromGroup, String msg) {
		if (msg.equals(".stop")) {
			Autoreply.sendGroupMessage(fromGroup, "disabled");
			Autoreply.enable = false;
			return true;
		}
		if (msg.equals(".livestop")) {
			Autoreply.sendGroupMessage(fromGroup, "livedisabled");
			LiveManager.liveStart = false;
			return true;
		}
		if (msg.equals(".start")) {
			Autoreply.enable = true;
			Autoreply.sendGroupMessage(fromGroup, "enabled");
			return true;
		}
		if (msg.equals(".livestart")) {
			LiveManager.liveStart = true;
			Autoreply.sendGroupMessage(fromGroup, "liveenabled");
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
			// @消息发送者并复读内容
			if (!msg.contains("蓝") && !msg.contains("藍") && !msg.contains("赠送")) {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + msg.substring(msg.indexOf(" ") + 1));
				return true;
			}
			if (msg.contains("野兽先辈") || msg.contains("仰望星空派") || msg.contains("英国") || msg.contains("鬼杀酒")
					|| msg.contains("羊杂碎布丁") || msg.contains("昏睡红茶")) {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + msg.substring(msg.indexOf(" ") + 1));
				return true;
			}
			return true;
		}
		return false;
	}

	// 膜人回复
	public static boolean checkMo(long fromGroup, String msg) {
		// 使用了正则表达式
		if (Pattern.matches(".*([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].{0,3}", msg.replace(" ", "").trim())) {
			Autoreply.sendGroupMessage(fromGroup, "打不过地灵殿Normal");
			try {
				Autoreply.sendGroupMessage(fromGroup,
						Autoreply.CC.image(new File(Autoreply.appDirectory + "pic\\fanmo.jpg")));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	// 读取文本文件
	public static String readFileToString(String fileName) throws IOException, UnsupportedEncodingException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		return new String(filecontent, "UTF-8");
	}

	// 删除字符串中指定位置的文字
	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	// 获取字符串中指定位置的文字
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

	// 删除字符串两端
	public static String removeCharAtStartAndEnd(String s) {
		String tmp = removeCharAt(s, 0);
		return removeCharAt(tmp, tmp.length() - 1);
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

	// 窥屏检测
	public static boolean checkLook(long fromGroup, String msg) {
		if (msg.equals("有人吗") || msg.equalsIgnoreCase("testip") || msg.equalsIgnoreCase("窥屏检测")) {
			int port = Autoreply.random.nextInt(5000);
			Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.share("http://119.27.186.107:" + (port + 4000), "窥屏检测",
					"滴滴滴", "http://119.27.186.107:" + (port + 4000) + "/111.jpg"));
			final IPGetter ipGetter = new IPGetter(fromGroup, port);
			ipGetter.start();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					Autoreply.sendGroupMessage(ipGetter.fromGroup, "当前有" + ipGetter.hSet.size() + "个小伙伴看了群聊");
					ipGetter.running = false;
				}
			}, 20000);
			return true;
		}
		return false;
	}

	// 萌二
	public static boolean checkMeng2(long fromGroup, String msg) {

		if (msg.equals("此")) {
			meng2tmp = "生";
			Autoreply.sendGroupMessage(fromGroup, "生");
			return true;
		} else if (msg.equals("无") && meng2tmp.equals("生")) {
			meng2tmp = "悔";
			Autoreply.sendGroupMessage(fromGroup, "悔");
			return true;
		} else if (msg.equals("入") && meng2tmp.equals("悔")) {
			meng2tmp = "东";
			Autoreply.sendGroupMessage(fromGroup, "东");
			return true;
		} else if (msg.equals("方") && meng2tmp.equals("东")) {
			meng2tmp = "来";
			Autoreply.sendGroupMessage(fromGroup, "来");
			return true;
		} else if (msg.equals("世") && meng2tmp.equals("来")) {
			meng2tmp = "愿";
			Autoreply.sendGroupMessage(fromGroup, "愿");
			return true;
		} else if (msg.equals("生") && meng2tmp.equals("愿")) {
			meng2tmp = "幻";
			Autoreply.sendGroupMessage(fromGroup, "幻");
			return true;
		} else if (msg.equals("想") && meng2tmp.equals("幻")) {
			meng2tmp = "乡";
			Autoreply.sendGroupMessage(fromGroup, "乡");
			return true;
		} else if (msg.equals("此生无悔入东方")) {
			Autoreply.sendGroupMessage(fromGroup, "来世愿生幻想乡");
			return true;
		}
		return false;
	}

	// 图片搜索专用的消息发送
	public static void sendMsg(long fromGroup, long fromQQ, String msg) throws Exception {
		if (fromGroup == -1) {
			Autoreply.sendPrivateMessage(fromQQ, msg);
		} else {
			Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + msg);
		}
	}

	public static Map<String, String> cookieToMap(String value) {
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

	public static String getRealUrl(String surl) throws Exception {
		String realUrl = "";
		String line;
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		URL url = new URL(surl);
		URLConnection conn = url.openConnection();
		conn.connect();
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		while ((line = in.readLine()) != null) {
			sb.append(line);
			System.out.println(line);
		}
		String nurl = conn.getURL().toString();
		System.out.println("realUrl" + nurl);
		realUrl = nurl;
		if (in != null) {
			in.close();
		}
		return realUrl;
	}

	public static String getSourceCode(String url) {
		return getSourceCode(url, null);
	}

	public static String getSourceCode(String url, String cookie) {
		Connection.Response response = null;
		Connection connection = null;
		try {
			connection = Jsoup.connect(url);
			if (cookie != null) {
				connection.cookies(cookieToMap(cookie));
			}
			connection.ignoreContentType(true).method(Connection.Method.GET);
			response = connection.execute();
			if (response.statusCode() != 200) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.body();
	}
	// public static String getG_tk(String skey) {
	// int hash = 5381;
	// int flag = skey.length();
	// for (int i = 0; i < flag; i++) {
	// hash = hash + hash * 32 + skey.charAt(i);
	// }
	// return String.valueOf(hash & 0x7fffffff);
	// }
}
