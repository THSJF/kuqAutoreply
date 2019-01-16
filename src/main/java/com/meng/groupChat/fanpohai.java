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

	private final String[][] ss = new String[][] { { "4guo1", "1106176045" }, { "com", "736461434" },
			{ "hop", "2695029036" }, { "mdzz", "1833661569" }, { "ti", "1244172541" }, { "yhx", "1148729387" },
			{ "斑点伞", "1254138109" }, { "苍老师", "1391857313" }, { "丑二桨", "289580425" }, { "大鸽", "869750266" },
			{ "大酱", "3427665460" }, { "丢人", "1581137837" }, { "枫哥", "2198047522" }, { "鸽鸽", "1437560361" },
			{ "记者", "1012539034" },
			// { "觉觉", "" },
			{ "觉恋", "1249934843" }, { "尻尻", "2448122241" }, { "空格", "839126279" }, { "懒骨头", "1393925434" },
			{ "懒瘦", "496276037" }, { "雷锋", "3206109525" }, { "蕾咪", "1090843173" },
			// { "恋萌萌", "" },
			{ "灵风", "2756253478" }, { "铃神", "2450811752" },
			// { "绿绿可柚", "" },
			{ "麻薯", "1272394657" }, { "毛玉", "421440146" }, { "伞挂", "203569312" }, { "沙苗", "1259176247" },
			{ "烧饼", "3035936740" }, { "圣师傅", "1211053685" }, { "实况主", "1822455876" }, { "水紫", "2994752341" },
			{ "岁月", "183889179" }, { "岁月", "3291680841" }, { "台长", "943486447" }, { "天星厨", "1282443047" },
			{ "骰子", "1808527498" }, { "团子", "1310144530" }, { "星墨", "1458573991" }, { "星小渚", "2835449913" },
			{ "逸诺", "942465781" }, { "苑神", "1813334125" }, { "针喵丸", "1023500486" }, { "紫苑", "839126279" }, };

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
				Autoreply.sendMessage(fromGroup, 0, "反迫害样本图更新");
			}
			boolean bpohai = false;
			// 处理带有迫害二字的消息
			if (msg.contains("迫害") && !msg.contains("反迫害")) {
				pohaicishu++;
				if (pohaicishu == alpohai || fromGroup == 348595763L || fromQQ == 2756253478L
						|| fromQQ == 1134808676L) {
					bpohai = true;
					pohaicishu = 0;
					alpohai = Autoreply.random.nextInt(2147483647) % 5 + 2;
				}
			}
			// 判定图片相似度
			if (!bpohai) {
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
			}
			if (!bpohai) {
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
					int ir = Autoreply.random.nextInt(2147483647) % 3;
					if (ir == 0) {
						Autoreply.sendMessage(fromGroup, 0, "鬼鬼");
					} else if (ir == 1) {
						Autoreply.sendMessage(fromGroup, 0, "除了迫害和膜你还知道什么");
					} else {
						Autoreply.sendMessage(fromGroup, 0, "草绳");
					}
					return true;
				} else {
					File[] files = (new File(folder)).listFiles();
					Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.image((File) Methods.rfa(files)));
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

}
