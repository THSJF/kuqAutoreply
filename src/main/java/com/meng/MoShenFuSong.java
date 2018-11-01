package com.meng;

import java.io.File;

public class MoShenFuSong extends Thread {
	private long fromGroup = 0;

	public MoShenFuSong(long fromGroup) {
		this.fromGroup = fromGroup;
	}

	@Override
	public void run() {
		File[] files = (new File(Autoreply.appDirectory + "膜神复诵/")).listFiles();
		try {
			Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
			sleep(200);
			Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
			sleep(200);
			Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
			sleep(200);
			Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.CC.image((File) Methods.rfa(files)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
