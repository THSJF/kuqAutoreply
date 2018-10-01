package com.meng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import com.sobte.cqp.jcq.message.CQCode;

public class MainSwitch {
	private static String motmp = "";

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

	public static String rfa(String[] array) { // randomFromArray
		return array[Autoreply.random.nextInt(array.length)];
	}

	public static boolean checkAt(long fromGroup, long fromQQ, String msg, CQCode CC) {
		if (CC.getAt(msg) == 1620628713L) {
			if (fromQQ == 2856986197L) {
				Autoreply.sendGroupMessage(fromGroup, CC.at(fromQQ) + "找姐姐什么事？");
			} else {
				switch (Autoreply.random.nextInt(4)) {
				case 0:
					Autoreply.sendGroupMessage(fromGroup, CC.at(fromQQ) + "找姐姐什么事？");
					break;
				case 1:
					break;
				case 2:
					Autoreply.sendGroupMessage(fromGroup, "干啥");
					break;
				case 3:
					Autoreply.sendGroupMessage(fromGroup, "?");
					break;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean checkLink(long fromGroup, String msg) {
		if (msg.startsWith("[CQ:share,url=")) {
			String link = msg.substring(msg.indexOf("http"), msg.indexOf(",title="));
			String title = msg.substring(msg.indexOf("title=") + 6, msg.indexOf(",content"));
			String describe = msg.substring(msg.indexOf("content=") + 8, msg.indexOf(",image"));
			String picture = msg.substring(msg.lastIndexOf("http"), msg.lastIndexOf("]"));
			Autoreply.sendGroupMessage(fromGroup,
					"标题:" + title + "\n链接:" + link + "\n封面图:" + picture + "\n描述:" + describe);
			return true;
		}
		return false;
	}

	public static boolean checkMo(long fromGroup, String msg, CQCode CC, String appDirectory) throws IOException {

		if (Pattern.matches(
				".*(([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].*(t.*c.*l|t.*q.*l|太.*[触觸].*了)|.*([蓝藍]|裂隙妖怪的式神).*[椰叶葉].*[椰叶葉].{0,3})",
				msg.replace(" ", "").trim())) {
			Autoreply.sendGroupMessage(fromGroup, "打不过地灵殿Normal");
			Autoreply.sendGroupMessage(fromGroup, CC.image(new File(appDirectory + "a.jpg")));
			return true;
		}
		return false;
	}

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

	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	public static boolean checkMo(long fromGroup, String msg) {

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

}
