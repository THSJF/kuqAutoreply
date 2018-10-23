package com.meng.searchPicture;

import java.util.HashMap;

import com.meng.Autoreply;
import com.meng.Methods;
import com.sobte.cqp.jcq.entity.CQImage;

public class NaoWait{

	private HashMap<Long, String> userNotSendPicture = new HashMap<>();

	public NaoWait() {
	}

	public void check(long fromGroup, long fromQQ, String msg) {
		if (msg.equalsIgnoreCase("sp.help")) {
			if (fromGroup != -1) {
				Methods.sendMsg(fromGroup, fromQQ, "使用方式已私聊发送");
			}
			Autoreply.sendPrivateMessage(fromQQ,
					"图片搜索功能群聊及私聊有效。使用sp.图片数量.目标网站或asp.图片数量.目标网站搜索图片。其中.图片数量和目标网站都可以省略但如果要选择目标网站必须加上图片数量。使用sp搜索时会自动隐藏相似度过低的图片，而asp搜索会显示全部结果");
			Autoreply.sendPrivateMessage(fromQQ, "例--\n在所有网站中搜索一个结果:sp\n在所有网站中搜索3个结果:sp.3\n在Pixiv中搜索1个结果sp.1.5");
			Autoreply.sendPrivateMessage(fromQQ,
					"网站代号：\n0 H-Magazines\n2 H-Game CG\n3 DoujinshiDB\n5 pixiv Images\n8 Nico Nico Seiga\n9 Danbooru\n10 drawr Images\n11 Nijie Images\n12 Yande.re\n13 Openings.moe\n15 Shutterstock\n16 FAKKU\n18 H-Misc\n19 2D-Market\n20 MediBang\n21 Anime\n22 H-Anime\n23 Movies\n24 Shows\n25 Gelbooru\n26 Konachan\n27 Sankaku Channel\n28 Anime-Pictures.net\n29 e621.net\n30 Idol Complex\n31 bcy.net Illust\n32 bcy.net Cosplay\n33 PortalGraphics.net (Hist)\n34 deviantArt\n35 Pawoo.net\n36 Manga");
			return;
		}
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null && (msg.toLowerCase().startsWith("sp") || msg.toLowerCase().startsWith("asp"))) {
			try {
				Methods.sendMsg(fromGroup, fromQQ, "土豆折寿中……");
				int needPic = 1;
				int database = 999;
				if (msg.startsWith("sp.") || msg.startsWith("asp.")) {
					String[] ss = msg.replaceAll("\\[.*\\]", "").split("[\\.]");
					needPic = Integer.parseInt(ss[1]);
					database = ss.length >= 3 ? Integer.parseInt(ss[2]) : 999;
				}
				new NAO(fromGroup, fromQQ,
						cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
								Autoreply.random.nextInt() + "pic.jpg"),
						msg.toLowerCase().startsWith("asp"), needPic, database).start();
			} catch (Exception e) {
				Methods.sendMsg(fromGroup, fromQQ, e.toString());
			}
		} else if (cqImage == null && (msg.toLowerCase().startsWith("sp.") || msg.toLowerCase().startsWith("asp.")
				|| (msg.equalsIgnoreCase("sp") || msg.equalsIgnoreCase("asp")))) {
			userNotSendPicture.put(fromQQ, msg);
			Methods.sendMsg(fromGroup, fromQQ, "需要一张图片");
		} else if (cqImage != null) {
			if (userNotSendPicture.get(fromQQ) != null) {
				try {
					Methods.sendMsg(fromGroup, fromQQ, "土豆折寿中……");
					int needPic = 1;
					int database = 999;
					if (userNotSendPicture.get(fromQQ).startsWith("sp.")
							|| userNotSendPicture.get(fromQQ).startsWith("asp.")) {
						String[] ss = userNotSendPicture.get(fromQQ).split("\\.");
						needPic = Integer.parseInt(ss[1]);
						database = ss.length >= 3 ? Integer.parseInt(ss[2]) : 999;
					}
					new NAO(fromGroup, fromQQ,
							cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
									Autoreply.random.nextInt() + "pic.jpg"),
							userNotSendPicture.get(fromQQ).startsWith("asp"), needPic, database).start();
				} catch (Exception e) {
					Methods.sendMsg(fromGroup, fromQQ, e.toString());
				}
				userNotSendPicture.remove(fromQQ);
			}

		}
	}
}
