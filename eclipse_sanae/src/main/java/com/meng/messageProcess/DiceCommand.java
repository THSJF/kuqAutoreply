package com.meng.messageProcess;
import com.meng.tools.*;
import com.meng.*;
import static com.meng.Autoreply.sendMessage;
public class DiceCommand {


	public void userCmd(long fromGroup, long fromQQ, String msg) {
		if (Autoreply.instence.CC.getAt(msg) != -1000 && !Methods.isAtme(msg)) {
			return;
		}
		if (!msg.startsWith(".")) {
			return;
		}
		if (msg.equals(".jrrp")) {

			return;
		}
		if (msg.startsWith(".nn ")) {

			return;
		}
		if (msg.startsWith(".welcome ")) {

			return;
		}
	}

	public void adminCmd(final long fromGroup, long fromQQ, String msg) {
		if (Autoreply.instence.CC.getAt(msg) != -1000 && !Methods.isAtme(msg)) {
			return;
		}
		if (!msg.startsWith(".")) {
			return;
		}
		if (msg.equals(".早苗说话") || msg.equals(".bot on")) {
			if (Autoreply.instence.botOff.contains(fromGroup)) {
				Autoreply.instence.botOff.remove(fromGroup);
				sendMessage(fromGroup, 0, "稳了");
				return ;
			}
		}
		if (msg.equals(".早苗闭嘴") || msg.equals(".bot off")) {
			Autoreply.instence.botOff.add(fromGroup);
			sendMessage(fromGroup, 0, "好吧");
			return ;
		}
		if (msg.equals(".dissmiss 2528419891") || msg.equals(".dissmiss")) {
			Autoreply.instence.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						Autoreply.sendMessage(fromGroup, 0, "我很快就会离开这里");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {}
						Autoreply.CQ.setGroupLeave(fromGroup, false);
					}
				});
		}
	}
}
