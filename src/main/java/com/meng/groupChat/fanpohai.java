package com.meng.groupChat;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.meng.Methods;
import com.meng.config.javabeans.PersonInfo;
import com.meng.tools.FingerPrint;
import com.sobte.cqp.jcq.entity.CQImage;

public class fanpohai {
	private FingerPrint[] fts;// 存放图片指纹的数组 用于对比新收到的图片和样本相似度
	private int pohaicishu = 0;// 收到的消息包含迫害二字的次数
	private int alpohai = Autoreply.instence.random.nextInt(5) + 2;// 收到的消息包含迫害二字的次数到达此值也会触发反迫害
	private CQImage cmCqImage;
	private File tmpFile;

	public fanpohai() {
		try {
			File[] pohaitu = new File(Autoreply.appDirectory + "fan\\").listFiles();
			fts = new FingerPrint[pohaitu.length];
			for (int i = 0; i < fts.length; i++) {
				fts[i] = new FingerPrint(ImageIO.read(pohaitu[i]));
			}
			tmpFile = new File(Autoreply.appDirectory + "phtmp.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean check(long fromQQ, long fromGroup, String msg) {
		try {
			boolean bpohai = false;
			// 处理带有迫害二字的消息
			if (msg.contains("迫害") && !msg.contains("反迫害")) {
				pohaicishu++;
				if (pohaicishu == alpohai) {
					bpohai = true;
					pohaicishu = 0;
					alpohai = Autoreply.instence.random.nextInt(2147483647) % 5 + 2;
				}
			}
			// 判定图片相似度
			if (!bpohai) {
				cmCqImage = Autoreply.instence.CC.getCQImage(msg);
				if (cmCqImage != null) {
					float simi = 0.0f;
					FingerPrint fp1 = null;
					if (tmpFile.exists()) {
						tmpFile.delete();
					}
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
			if (bpohai) {
				String folder = "";
				for (PersonInfo bilibiliUser : Autoreply.instence.configManager.configJavaBean.personInfo) {
					if (fromQQ == bilibiliUser.qq) {
						folder = Autoreply.appDirectory + "pohai/" + bilibiliUser.name + "/";
						break;
					}
				}
				File file = new File(folder);
				if (folder.equals("") || !file.exists()) {
					int ir = Autoreply.instence.random.nextInt(2147483647) % 3;
					if (ir == 0) {
						Autoreply.sendMessage(fromGroup, 0, "鬼鬼");
					} else if (ir == 1) {
						Autoreply.sendMessage(fromGroup, 0, "除了迫害和膜你还知道什么");
					} else {
						Autoreply.sendMessage(fromGroup, 0, "草绳");
					}
					return true;
				} else {
					File[] files = file.listFiles();
					Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

}
