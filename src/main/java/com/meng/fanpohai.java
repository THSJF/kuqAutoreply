package com.meng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.CoolQ;
import com.sobte.cqp.jcq.message.CQCode;

public class fanpohai {
	cccccc cqCode = new cccccc();
	File[] pohaitu;
	FingerPrint[] fts;
	private int pohaicishu = 0;
	private int alpohai = Autoreply.random.nextInt(5)+2;
	private String lastMsg = "";
	String appdirectory = "";
	private final String[][] ss = new String[][] { { "丢人", "1581137837" }, { "hop", "2695029036" },
			{ "伞挂", "203569312" }, { "台长", "943486447" }, { "圣师傅", "1211053685" }, { "大鸽", "869750266" },
			{ "水紫", "2994752341" }, { "烧饼", "3035936740" }, { "空格", "839126279" }, { "紫苑", "839126279" },
			{ "雷锋", "3206109525" }, { "麻薯", "1272394657" }, { "记者", "1012539034" }, { "毛玉", "421440146" },
			{ "yhx", "1148729387" }, { "4guo1", "1106176045" }, { "mdzz", "1833661569" }, { "ti", "1244172541" },
			{ "星小渚", "2835449913" }, { "丑二桨", "289580425" }, { "懒瘦", "496276037" }, { "天星厨", "1282443047" },
			{ "逸诺", "942465781" }, { "com", "736461434" }, { "岁月", "183889179" }, { "鸽鸽", "1437560361" },
			{ "大酱", "3427665460" },{ "灵风", "2756253478" },{ "斑点伞", "1254138109" },{ "苍老师", "1391857313" } };

	public fanpohai(String appdirectory) {
		this.appdirectory = appdirectory;
		loadph();
	}

	private void loadph() {
		pohaitu = new File(appdirectory + "fan\\").listFiles();
		System.out.println(pohaitu.length);
		fts = new FingerPrint[pohaitu.length];
		for (int i = 0; i < fts.length; i++) {
			try {
				fts[i] = new FingerPrint(ImageIO.read(pohaitu[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public int check(long fromQQ, long fromGroup, String msg, String appdirectory) {
		if (msg.equalsIgnoreCase("loadph")) {
			loadph();
			Autoreply.sendGroupMessage(fromGroup, "反迫害样本图更新");
		}
		boolean bpohai = false;
		if (msg.indexOf("迫害") != -1) {
			pohaicishu++;
			if (pohaicishu == alpohai) {
				bpohai = true;
				pohaicishu = 0;
				alpohai = Autoreply.random.nextInt(5)+2;
			}
		}
		try {
			CQImage cmCqImage = cqCode.getCQImage(msg);
			if (cmCqImage != null) {
				float simi = 0.0f;
				System.out.println(fts.length);
				FingerPrint fp1 = new FingerPrint(ImageIO.read(cmCqImage.download(appdirectory + "phtmp.jpg")));
				for (int i = 0; i < fts.length; i++) {
					float tf = fts[i].compare(fp1);
					if (tf > simi) {
						simi = tf;
					}
				}

				if (simi > 0.75f) {
					System.out.printf("\nsim=%f", simi);
					bpohai = true;
				}

				// FingerPrint fp2 = new FingerPrint(ImageIO.read(new
				// File(appdirectory + "\fan\\pohai1.jpg")));
				// System.out.println(fp1.toString(true));
				// System.out.printf("sim=%f", fp1.compare(fp2));
				// Autoreply.sendGroupMessage(fromGroup, "sim=" +
				// fp1.compare(fp2));
			}
		} catch (IOException e1) {
		}

		try {
			String encoding = "utf-8";
			File file = new File(appdirectory + "fanpohai.txt");
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (msg.equals(lineTxt)) {
						bpohai = true;
						break;
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}

		if (bpohai) {
			String folder = "";
			for (int i = 0; i < ss.length; i++) {
				if (fromQQ == Long.parseLong(ss[i][1])) {
					folder = appdirectory + "pohai/" + ss[i][0] + "/";
					break;
				}
			}
			if (folder.equals("")) {
				System.out.println(folder);
				int ir=Autoreply.random.nextInt(3);
				if (ir == 0) {
					Autoreply.sendGroupMessage(fromGroup, "鬼鬼");
				} else if (ir==1) {
					Autoreply.sendGroupMessage(fromGroup, "除了迫害你还知道什么");
				}else {
					Autoreply.sendGroupMessage(fromGroup, "草绳");
				}
				return Autoreply.MSG_IGNORE;
			} else {
				File fo = new File(folder);
				System.out.println(folder);
				File[] files = fo.listFiles();

				try {
					Autoreply.sendGroupMessage(fromGroup, cqCode.image(files[Autoreply.random.nextInt(files.length)]));
				} catch (IOException e) {
				}

				return Autoreply.MSG_IGNORE;
			}
		}
		return 2;
	}

}
