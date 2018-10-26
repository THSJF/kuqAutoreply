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

import com.meng.barcode.BarcodeEncoder;
import com.meng.lookGroup.IPGetter;

public class Methods {
	private static String motmp = "";
	private static String meng2tmp = "";
	static int arrayFlag = new java.util.Random().nextInt(5000);

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
		return array[Autoreply.random.getNextInt(array.length)];
	}

	// 有@的时候
	public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
		if (Autoreply.CC.getAt(msg) == 1620628713L) {
			// 过滤特定的文字
			if (msg.indexOf("蓝") != -1 || msg.indexOf("藍") != -1 || msg.indexOf("赠送") != -1) {
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
			// String link = msg.substring(msg.indexOf("http"),
			// msg.indexOf(",title="));
			// String title = msg.substring(msg.indexOf("title=") + 6,
			// msg.indexOf(",content"));
			// String describe = msg.substring(msg.indexOf("content=") + 8,
			// msg.indexOf(",image"));
			String picture = msg.substring(msg.lastIndexOf("http"), msg.lastIndexOf("]"));
			// 发送消息
			// Autoreply.sendGroupMessage(fromGroup,
			// "标题:" + title + "\n链接:" + link + "\n封面图:" + picture + "\n描述:" +
			// describe);
			Autoreply.sendGroupMessage(fromGroup, "封面图:" + picture);
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
		if (msg.equals("有人吗") || msg.equalsIgnoreCase("testip")) {
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

	// 签到
	public static boolean checkSign(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:sign")) {
			if (fromGroup == 424838564L) {
				try {
					Autoreply.sendGroupMessage(fromGroup, "签到成功 这是你的签到奖励"
							+ Autoreply.CC.image(new File(Autoreply.appDirectory + "pic/qiaodaodnf.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Autoreply.sendGroupMessage(fromGroup, "image:pic/qiandao.jpg");
			}
			return true;
		}
		return false;
	}

	// 分享音乐
	public static boolean checkMusic(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:music")) {
			int i = Autoreply.random.nextInt(3);
			switch (i) {
			case 0:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(22636603, "163", false));
				break;
			case 1:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(103744845, "qq", false));
				break;
			case 2:
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.music(103744852, "qq", false));
				break;
			}
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

	// 图片搜索和二维码生成功能专用的消息发送
	public static void sendMsg(long fromGroup, long fromQQ, String msg) {
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
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
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
				System.out.println("response.statusCode()" + response.statusCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.body();
	}

	//
	// public static String getG_tk(String skey) {
	// int hash = 5381;
	// int flag = skey.length();
	// for (int i = 0; i < flag; i++) {
	// hash = hash + hash * 32 + skey.charAt(i);
	// }
	// return String.valueOf(hash & 0x7fffffff);
	// }

	public static boolean checkBarcodeEncode(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("生成QR ") || msg.startsWith("生成PDF417 ")) {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder(fromGroup, fromQQ, msg);
			barcodeEncoder.start();
			return true;
		}
		return false;
	}
}
