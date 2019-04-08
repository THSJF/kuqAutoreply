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
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(2000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(2000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(2000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(1000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(1000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(1000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(1000);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(500);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(500);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(500);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(500);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(500);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				sleep(100);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 4:
			if (fromGroup == 822438633L) {
				try {
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
					Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(files)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 5:
			try {
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				sleep(200);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				sleep(200);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				sleep(200);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				sleep(200);
				Autoreply.CQ.sendGroupMsg(fromGroup, Autoreply.instence.CC.image((File) Methods.rfa(filesFFF)));
				sleep(200);
			} catch (Exception e) {
			}
			break;
		}
	}
}
