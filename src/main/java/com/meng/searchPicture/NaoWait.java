package com.meng.searchPicture;

import java.util.HashMap;

import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class NaoWait {

	private HashMap<Long, String> userNotSendPicture = new HashMap<>();

	public NaoWait() {
	}

	public void check(long fromGroup, long fromQQ, String msg) {
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null && (msg.toLowerCase().startsWith("sp") || msg.toLowerCase().startsWith("asp"))) {
			try {
				if (fromGroup == -1) {
					Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
				} else {
					Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "少女折寿中……");
				}
				int needPic = 1;
				if (msg.startsWith("sp.") || msg.startsWith("asp.")) {
					String[] ss = msg.split("[.\\[]");
					needPic = Integer.parseInt(ss[1]);
				}
				new NAO(fromGroup, fromQQ,
						cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
								Autoreply.random.nextInt() + "pic.jpg"),
						msg.toLowerCase().startsWith("asp"), needPic).start();
			} catch (Exception e) {
				if (fromGroup == -1) {
					Autoreply.sendPrivateMessage(fromQQ, e.toString());
				} else {
					Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + e.toString());
				}
			}
		} else if (cqImage == null && (msg.toLowerCase().startsWith("sp.") || msg.toLowerCase().startsWith("asp.")
				|| (msg.equalsIgnoreCase("sp") || msg.equalsIgnoreCase("asp")))) {
			userNotSendPicture.put(fromQQ, msg);
			if (fromGroup == -1) {
				Autoreply.sendPrivateMessage(fromQQ, "需要一张图片");
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "需要一张图片");
			}
		} else if (cqImage != null) {
			if (userNotSendPicture.get(fromQQ) != null) {
				try {
					if (fromGroup == -1) {
						Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
					} else {
						Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "少女折寿中……");
					}
					int needPic = 1;
					if (userNotSendPicture.get(fromQQ).startsWith("sp.")
							|| userNotSendPicture.get(fromQQ).startsWith("asp.")) {
						String[] ss = userNotSendPicture.get(fromQQ).split("\\.");
						needPic = Integer.parseInt(ss[1]);
					}
					new NAO(fromGroup, fromQQ,
							cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
									Autoreply.random.nextInt() + "pic.jpg"),
							userNotSendPicture.get(fromQQ).startsWith("asp"), needPic).start();
				} catch (Exception e) {
					if (fromGroup == -1) {
						Autoreply.sendPrivateMessage(fromQQ, e.toString());
					} else {
						Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + e.toString());
					}
				}
				userNotSendPicture.remove(fromQQ);
			}

		}
	}

}
