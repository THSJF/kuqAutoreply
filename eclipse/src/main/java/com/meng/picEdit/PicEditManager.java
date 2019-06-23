package com.meng.picEdit;

public class PicEditManager {
	public PicEditManager() {
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("精神支柱[CQ:at")) {
			new JingShenZhiZhuQQManager(fromGroup, fromQQ, msg);
			return true;
		} else if (msg.startsWith("神触[CQ:at")) {
			new ShenChuQQManager(fromGroup, fromQQ, msg);
			return true;
		}
		return false;
	}
}
