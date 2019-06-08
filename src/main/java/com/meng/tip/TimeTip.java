package com.meng.tip;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.meng.Autoreply;
import com.meng.Methods;

public class TimeTip extends Thread {
	private long groupYuTang = 617745343L;
	private long groupZiyuan = 857548607L;
	private long groupDNFmoDao = 424838564L;
	private long groupXueXi = 312342896L;
	private long groupTaiZhang = 859561731L;
	//private long groupDaiyuQun = 233861874L;
	private long ziyuan = 2198634315L;
	private long alice = 1326051907L;
	private long YYS = 1418780411L;
	private long diuren = 1581137837L;
	private boolean tipedYYS = true;
	private boolean tipedAlice = true;
	private boolean tipedZiyuan = true;
	private boolean tipedDiuRen = false;
//	private boolean tipedDaiYuQun = true;

	public TimeTip() {
	}

	@Override
	public void run() {
		while (true) {
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				tipedDiuRen = false;
				tipedAlice = false;
		//		tipedDaiYuQun = false;
				if (c.get(Calendar.HOUR_OF_DAY) == 6) {
					tipedZiyuan = false;
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 3 == 0) {
					tipedYYS = false;
				}

				if (getTipHour(c)) {
					if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DATE)) {
						Autoreply.sendMessage(groupDNFmoDao, 0, "最后一天莉，，，看看冒险团商店");
						Autoreply.sendMessage(groupXueXi, 0, "最后一天莉，，，看看冒险团商店");
					}
					switch (c.get(Calendar.DAY_OF_WEEK)) {
					case 4:
						Autoreply.sendMessage(groupDNFmoDao, 0, "星期三莉，，，看看成长胶囊");
						Autoreply.sendMessage(groupXueXi, 0, "星期三莉，，，看看成长胶囊");
						break;
					case 5:
						Autoreply.sendMessage(groupTaiZhang, 0, "注意，明天星期五");
						break;
					case 6:
						Autoreply.sendMessage(groupTaiZhang, 0, "注意，今天星期五");
						break;
					}
				}
			}
		//	if (!tipedDaiYuQun) {
		//		try {
		//			Autoreply.sendMessage(groupDaiyuQun, 0,
		//					Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\提醒\\jiemo.jpg")));
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//		tipedDaiYuQun = true;
		//	}
			try {
				sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean getTipHour(Calendar c) {
		return (c.get(Calendar.HOUR_OF_DAY) == 12 || c.get(Calendar.HOUR_OF_DAY) == 16
				|| c.get(Calendar.HOUR_OF_DAY) == 22);
	}

	public boolean check(long fromGroup, long fromQQ) {
		if (!tipedYYS && fromGroup == groupYuTang && fromQQ == YYS) {
			String[] strings = new String[] { "想吃YYS", "想食YYS", "想上YYS", Autoreply.instence.CC.at(1418780411L) + "老婆" };
			Autoreply.sendMessage(groupYuTang, 0, (String) Methods.rfa(strings));
			tipedYYS = true;
			return true;
		}
		if (!tipedAlice && fromQQ == alice) {
			try {
				Autoreply.sendMessage(fromGroup, 0,
						Autoreply.instence.CC.image(new File(Autoreply.appDirectory + "pic\\alice.jpg")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			tipedAlice = true;
			return true;
		}
		// if (!tipedDiuRen && fromQQ == diuren) {
		// Autoreply.sendMessage(fromGroup, 0,
		// Autoreply.instence.CC.at(diuren) + "把你的女装给我交了");
		// tipedDiuRen = true;
		// return true;
		// }
		if (!tipedZiyuan && fromGroup == groupZiyuan && fromQQ == ziyuan) {
			Autoreply.sendMessage(groupZiyuan, 0, Autoreply.instence.CC.at(ziyuan) + "昨天援交赚了多少？");
			tipedZiyuan = true;
			return true;
		}
		return false;
	}

}
