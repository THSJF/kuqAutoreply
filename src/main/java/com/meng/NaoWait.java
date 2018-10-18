package com.meng;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.sobte.cqp.jcq.entity.CQImage;

public class NaoWait {

	private HashSet<Long> userNotSendPicture = new HashSet<>();

	public NaoWait() {
	}

	public void check(long fromQQ, String msg) {
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null && msg.toLowerCase().startsWith("snao")) {
			try {
				Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
				new NAO(fromQQ, cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
						Autoreply.random.nextInt() + "pic.jpg")).start();
			} catch (IOException e) {
				Autoreply.sendPrivateMessage(fromQQ, e.toString());
			}
		} else if (cqImage == null && msg.toLowerCase().startsWith("snao")) {
			userNotSendPicture.add(fromQQ);
			Autoreply.sendPrivateMessage(fromQQ, "need a picture");
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
					Autoreply.sendPrivateMessage(fromQQ, "少女折寿中……");
					new NAO(fromQQ, cqImage.download(Autoreply.appDirectory + "picSearch\\" + String.valueOf(fromQQ),
							Autoreply.random.nextInt() + "pic.jpg")).start();
					System.out.println("snao start");
				} catch (IOException e) {
					Autoreply.sendPrivateMessage(fromQQ, e.toString());
				}
			}

		}
	}

}
