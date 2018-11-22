package com.meng.tip;

import java.util.Calendar;

import com.meng.Autoreply;

public class TimeTip extends Thread {
	// 每两个小时内最速说第一句话时催更
	long groupZuisu = 855927922L;
	long groupZiyuan = 857548607L;
	long groupDNFmoDao = 424838564L;
	long groupXueXi = 312342896L;
	long ziyuan = 2198634315L;
	long jizhe = 1012539034L;
	long diuren = 1581137837L;
	boolean tipedZuisu = true;
	boolean tipedZiyuan = true;
	boolean tipedModao = true;
	boolean tipedDiuRen = false;

	public TimeTip() {
	}

	@Override
	public void run() {
		while (true) {
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				tipedDiuRen = false;
				if (c.get(Calendar.HOUR_OF_DAY) == 6) {
					tipedZiyuan = false;
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tipedZuisu = false;// 每两个小时重置提醒标记
				}
				if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DATE)
						&& (c.get(Calendar.HOUR_OF_DAY) == 12 || c.get(Calendar.HOUR_OF_DAY) == 16
								|| c.get(Calendar.HOUR_OF_DAY) == 22)) {
					Autoreply.sendMessage(groupDNFmoDao, 0, "最后一天莉，，，看看冒险团商店");
					Autoreply.sendMessage(groupXueXi, 0, "最后一天莉，，，看看冒险团商店");
				}
				if (c.get(Calendar.DAY_OF_WEEK) == 4 && (c.get(Calendar.HOUR_OF_DAY) == 12
						|| c.get(Calendar.HOUR_OF_DAY) == 16 || c.get(Calendar.HOUR_OF_DAY) == 22)) {
					Autoreply.sendMessage(groupDNFmoDao, 0, "星期三莉，，，看看成长胶囊");
					Autoreply.sendMessage(groupXueXi, 0, "星期三莉，，，看看成长胶囊");
				}
			}
			try {
				sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean check(long fromGroup, long fromQQ) {
		if (!tipedZuisu && fromGroup == groupZuisu && fromQQ == jizhe) {
			Autoreply.sendMessage(groupZuisu, 0, Autoreply.CC.at(jizhe) + "今天更新了吗？");
			tipedZuisu = true;
			return true;
		}
		if (!tipedDiuRen && fromQQ == diuren) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.CC.at(diuren) + "把你的女装给我交了");
			tipedDiuRen = true;
			return true;
		}
		if (!tipedZiyuan && fromGroup == groupZiyuan && fromQQ == ziyuan) {
			Autoreply.sendMessage(groupZiyuan, 0, Autoreply.CC.at(ziyuan) + "昨天援交赚了多少？");
			tipedZiyuan = true;
			return true;
		}
		return false;
	}

}
