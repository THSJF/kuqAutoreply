package com.meng.groupChat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;

public class fanpohai {
	private FingerPrint[] fts;// 存放图片指纹的数组 用于对比新收到的图片和样本相似度
	private int pohaicishu = 0;// 收到的消息包含迫害二字的次数
	private int alpohai = Autoreply.random.nextInt(5) + 2;// 收到的消息包含迫害二字的次数到达此值也会触发反迫害
	private File fanpohafile;// 存放触发反迫害的消息
	private CQImage cmCqImage;

	private final String[][] ss = new String[][] { { "丢人", "1581137837" }, { "hop", "2695029036" },
			{ "伞挂", "203569312" }, { "台长", "943486447" }, { "圣师傅", "1211053685" }, { "大鸽", "869750266" },
			{ "水紫", "2994752341" }, { "烧饼", "3035936740" }, { "空格", "839126279" }, { "紫苑", "839126279" },
			{ "雷锋", "3206109525" }, { "麻薯", "1272394657" }, { "记者", "1012539034" }, { "毛玉", "421440146" },
			{ "yhx", "1148729387" }, { "4guo1", "1106176045" }, { "mdzz", "1833661569" }, { "ti", "1244172541" },
			{ "星小渚", "2835449913" }, { "丑二桨", "289580425" }, { "懒瘦", "496276037" }, { "天星厨", "1282443047" },
			{ "逸诺", "942465781" }, { "com", "736461434" }, { "岁月", "183889179" }, { "鸽鸽", "1437560361" },
			{ "大酱", "3427665460" }, { "灵风", "2756253478" }, { "斑点伞", "1254138109" }, { "苍老师", "1391857313" },
			{ "尻尻", "2448122241" } };

	public fanpohai() {
		fanpohafile = new File(Autoreply.appDirectory + "fanpohai.txt");
		try {
			loadph();
		} catch (IOException e) {// 文件或文件夹不存在会IOException
		}
	}

	private void loadph() throws IOException {
		File[] pohaitu = new File(Autoreply.appDirectory + "fan\\").listFiles();
		fts = new FingerPrint[pohaitu.length];
		for (int i = 0; i < fts.length; i++) {
			fts[i] = new FingerPrint(ImageIO.read(pohaitu[i]));
		}
	}

	public boolean check(long fromQQ, long fromGroup, String msg) {
		// 每次收到消息时都读取样本太耗资源，所以手动更新
		try {
			if (msg.equalsIgnoreCase("loadph")) {
				loadph();
				Autoreply.sendGroupMessage(fromGroup, "反迫害样本图更新");
			}
			boolean bpohai = false;
			// 处理带有迫害二字的消息
			if (msg.indexOf("迫害") != -1) {
				pohaicishu++;
				if (pohaicishu == alpohai) {
					bpohai = true;
					pohaicishu = 0;
					alpohai = Autoreply.random.nextInt(5) + 2;
				}
			}
			// 判定图片相似度
			Autoreply.CC.getCQImage(msg);
			if (cmCqImage != null) {
				float simi = 0.0f;
				FingerPrint fp1 = null;
				fp1 = new FingerPrint(ImageIO.read(cmCqImage.download(Autoreply.appDirectory + "phtmp.jpg")));
				// 取值为所有样本中最高的相似度
				for (int i = 0; i < fts.length; i++) {
					float tf = fts[i].compare(fp1);
					if (tf > simi) {
						simi = tf;
					}
				}
				if (simi > 0.92f) {
					bpohai = true;
				}
			}
			// 从反迫害文本文件中读取
			if (fanpohafile.isFile() && fanpohafile.exists()) {
				InputStreamReader read;
				read = new InputStreamReader(new FileInputStream(fanpohafile));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (msg.equals(lineTxt)) {
						bpohai = true;
						break;
					}
				}
				read.close();
			}
			// 如果满足反迫害条件 根据上面的数组从QQ号得到用户迫害图文件夹
			if (bpohai) {
				String folder = "";
				for (int i = 0; i < ss.length; i++) {
					if (fromQQ == Long.parseLong(ss[i][1])) {
						folder = Autoreply.appDirectory + "pohai/" + ss[i][0] + "/";
						break;
					}
				}
				if (folder.equals("")) {
					int ir = Autoreply.random.nextInt(3);
					if (ir == 0) {
						Autoreply.sendGroupMessage(fromGroup, "鬼鬼");
					} else if (ir == 1) {
						Autoreply.sendGroupMessage(fromGroup, "除了迫害和膜你还知道什么");
					} else {
						Autoreply.sendGroupMessage(fromGroup, "草绳");
					}
					return true;
				} else {
					File[] files = (new File(folder)).listFiles();
					// 丢人专属双倍快乐
					if (folder.equals(Autoreply.appDirectory + "pohai/丢人/")) {
						Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
					}
					Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

}