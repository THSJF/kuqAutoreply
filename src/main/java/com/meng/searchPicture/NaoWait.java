package com.meng.searchPicture;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
				new NAO(fromGroup, fromQQ,
						cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
								Autoreply.random.nextInt() + "pic.jpg"),
						msg.toLowerCase().startsWith("asp")).start();
			} catch (IOException e) {
				System.out.println(e);
			}
		} else if (cqImage == null && (msg.equals("sp") || msg.equals("asp"))) {
			if (msg.toLowerCase().startsWith("sp")) {
				userNotSendPicture.put(fromQQ, "sp");
			} else {
				userNotSendPicture.put(fromQQ, "asp");
			}
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
					new NAO(fromGroup, fromQQ,
							cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
									Autoreply.random.nextInt() + "pic.jpg"),
							userNotSendPicture.get(fromQQ).equals("asp")).start();
				} catch (IOException e) {
					Autoreply.sendPrivateMessage(2856986197L, e.toString());
				}
			}

		}
	}

}
