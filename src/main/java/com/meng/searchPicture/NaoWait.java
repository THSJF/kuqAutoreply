package com.meng.searchPicture;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.meng.Autoreply;
import com.sobte.cqp.jcq.entity.CQImage;

public class NaoWait {

	private HashSet<Long> userNotSendPicture = new HashSet<>();

	public NaoWait() {
	}

	public void check(long fromGroup, long fromQQ, String msg) {
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null && msg.toLowerCase().startsWith("sp")) {
			try {
				if (fromGroup == -1) {
					Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
				} else {
					Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "少女折寿中……");
				}
				new NAO(fromGroup, fromQQ,
						cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
								Autoreply.random.nextInt() + "pic.jpg")).start();
			} catch (IOException e) {
				System.out.println(e);
			}
		} else if (cqImage == null && msg.toLowerCase().startsWith("sp")) {
			userNotSendPicture.add(fromQQ);
			if (fromGroup == -1) {
				Autoreply.sendPrivateMessage(fromQQ, "需要一张图片");
			} else {
				Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "需要一张图片");
			}
		} else if (cqImage != null) {
			boolean isIn = false;
			long tmpl = 0;
			Iterator<Long> iterator = userNotSendPicture.iterator();
			while (iterator.hasNext()) {
				tmpl = iterator.next();
				if (fromQQ == tmpl) {
					isIn = true;
					userNotSendPicture.remove(tmpl);
					break;
				}
			}
			if (isIn) {
				try {
					if (fromGroup == -1) {
						Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
					} else {
						Autoreply.sendGroupMessage(fromGroup, Autoreply.CC.at(fromQQ) + "少女折寿中……");
					}
					new NAO(fromGroup, fromQQ,
							cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
									Autoreply.random.nextInt() + "pic.jpg")).start();
				} catch (IOException e) {
					Autoreply.sendPrivateMessage(2856986197L, e.toString());
				}
			}

		}
	}

}
