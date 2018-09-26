package com.meng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.sobte.cqp.jcq.entity.CQImage;

public class fanpohai {
	private cccccc cqCode = new cccccc();
	private File[] pohaitu;
	private FingerPrint[] fts;
	private int pohaicishu = 0;
	private String encoding = "utf-8";
	private int alpohai = Autoreply.random.nextInt(5) + 2;
	private File fanpohafile;
	private CQImage cmCqImage;
	private String appdirectory = "";
	
	private final String[][] ss = new String[][] { { "丢人", "1581137837" }, { "hop", "2695029036" },
			{ "伞挂", "203569312" }, { "台长", "943486447" }, { "圣师傅", "1211053685" }, { "大鸽", "869750266" },
			{ "水紫", "2994752341" }, { "烧饼", "3035936740" }, { "空格", "839126279" }, { "紫苑", "839126279" },
			{ "雷锋", "3206109525" }, { "麻薯", "1272394657" }, { "记者", "1012539034" }, { "毛玉", "421440146" },
			{ "yhx", "1148729387" }, { "4guo1", "1106176045" }, { "mdzz", "1833661569" }, { "ti", "1244172541" },
			{ "星小渚", "2835449913" }, { "丑二桨", "289580425" }, { "懒瘦", "496276037" }, { "天星厨", "1282443047" },
			{ "逸诺", "942465781" }, { "com", "736461434" }, { "岁月", "183889179" }, { "鸽鸽", "1437560361" },
			{ "大酱", "3427665460" }, { "灵风", "2756253478" }, { "斑点伞", "1254138109" }, { "苍老师", "1391857313" } };

	public fanpohai(String appdirectory) throws IOException {
		this.appdirectory = appdirectory;
		fanpohafile= new File(appdirectory + "fanpohai.txt");
		loadph();
	}

	private void loadph() throws IOException {
		pohaitu = new File(appdirectory + "fan\\").listFiles();
		fts = new FingerPrint[pohaitu.length];
		for (int i = 0; i < fts.length; i++) {
				fts[i] = new FingerPrint(ImageIO.read(pohaitu[i]));
		}
	}

	public boolean check(long fromQQ, long fromGroup, String msg, String appdirectory) throws IOException {
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
				alpohai = Autoreply.random.nextInt(5) + 2;
			}
		}
		cmCqImage = cqCode.getCQImage(msg);
		if (cmCqImage != null) {
			float simi = 0.0f;
			FingerPrint fp1 = new FingerPrint(ImageIO.read(cmCqImage.download(appdirectory + "phtmp.jpg")));
			for (int i = 0; i < fts.length; i++) {
				float tf = fts[i].compare(fp1);
				if (tf > simi) {
					simi = tf;
				}
			}
			if (simi > 0.8f) {
				System.out.printf("\nsim=%f", simi);
				bpohai = true;
			}
		}
		if (fanpohafile.isFile() && fanpohafile.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(fanpohafile), encoding);// 考虑到编码格式
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
		if (bpohai) {
			String folder = "";
			for (int i = 0; i < ss.length; i++) {
				if (fromQQ == Long.parseLong(ss[i][1])) {
					folder = appdirectory + "pohai/" + ss[i][0] + "/";
					break;
				}
			}
			if (folder.equals("")) {
				int ir = Autoreply.random.nextInt(3);
				if (ir == 0) {
					Autoreply.sendGroupMessage(fromGroup, "鬼鬼");
				} else if (ir == 1) {
					Autoreply.sendGroupMessage(fromGroup, "除了迫害你还知道什么");
				} else {
					Autoreply.sendGroupMessage(fromGroup, "草绳");
				}
				return true;
			} else {
				File fo = new File(folder);
				File[] files = fo.listFiles();
				Autoreply.sendGroupMessage(fromGroup, cqCode.image(files[Autoreply.random.nextInt(files.length)]));
				return true;
			}
		}
		return false;
	}

}
