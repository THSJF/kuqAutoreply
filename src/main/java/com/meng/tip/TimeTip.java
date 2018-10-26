package com.meng.tip;

import java.util.Calendar;

import com.meng.Autoreply;

public class TimeTip extends Thread {
	// 每两个小时内最速说第一句话时催更
	long groupZuisu = 855927922L;
	long groupZiyuan = 857548607L;
	long groupDNFmoDao = 424838564L;
	long groupXueXi = 312342896;
	long ziyuan = 2198634315L;
	long jizhe = 1012539034L;
	boolean tipedZuisu = true;
	boolean tipedZiyuan = false;
	boolean tipedModao = true;

	public TimeTip() {
	}

	@Override
	public void run() {
		while (true) {
			Calendar c = Calendar.getInstance();
			if (c.get(Calendar.MINUTE) == 0) {
				if (c.get(Calendar.HOUR_OF_DAY) == 6) {
					tipedZiyuan = false;
				}
				if (c.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
					tipedZuisu = false;// 每两个小时重置提醒标记
				}
				if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DATE)
						&& (c.get(Calendar.HOUR_OF_DAY) == 12 || c.get(Calendar.HOUR_OF_DAY) == 16
								|| c.get(Calendar.HOUR_OF_DAY) == 22)) {
					// Autoreply.sendGroupMessage(groupDNFmoDao,
					// "最后一天莉，，，看看冒险团商店");
					Autoreply.sendGroupMessage(groupXueXi, "最后一天莉，，，看看冒险团商店");
				}
				if (c.get(Calendar.DAY_OF_WEEK) == 4 && (c.get(Calendar.HOUR_OF_DAY) == 12
						|| c.get(Calendar.HOUR_OF_DAY) == 16 || c.get(Calendar.HOUR_OF_DAY) == 22)) {
					// Autoreply.sendGroupMessage(groupDNFmoDao,
					// "星期三莉，，，看看成长胶囊");
					Autoreply.sendGroupMessage(groupXueXi, "星期三莉，，，看看成长胶囊");
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
			Autoreply.sendGroupMessage(groupZuisu, Autoreply.CC.at(jizhe) + "今天更新了吗？");
			tipedZuisu = true;
			return true;
		}
		if (!tipedZiyuan && fromGroup == groupZiyuan && fromQQ == ziyuan) {
			Autoreply.sendGroupMessage(groupZiyuan, Autoreply.CC.at(ziyuan) + "昨天援交赚了多少？");
			tipedZiyuan = true;
			return true;
		}
		return false;
	}

}
