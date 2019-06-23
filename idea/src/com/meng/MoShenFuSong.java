package com.meng;

import java.io.File;

public class MoShenFuSong extends Thread {
	private long fromGroup = 0;
	private int flag = 0;

	public MoShenFuSong(long fromGroup, int flag) {
		this.fromGroup = fromGroup;
		this.flag = flag;
	}

	@Override
	public void run() {
		File[] files = (new File(Autoreply.appDirectory + "膜神复诵/")).listFiles();
		File[] filesFFF = (new File(Autoreply.appDirectory + "发发发/")).listFiles();
		switch (flag) {
		case 0:
			try {
				for (int i = 0; i < 4; ++i) {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					sleep(2000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				for (int i = 0; i < 5; ++i) {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				for (int i = 0; i < 6; ++i) {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				for (int i = 0; i < 8; ++i) {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					sleep(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 4:
			if (fromGroup == 822438633L) {
				try {
					for (int i = 0; i < 68; ++i) {
						Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 5:
			try {
				for (int i = 0; i < 5; ++i) {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				}
			} catch (Exception e) {
			}
			break;
		}
	}
}
