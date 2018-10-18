package com.meng;

import java.util.HashMap;

public class FileTipManager extends Thread {

	private int mapFlag = 0;
	private HashMap<Integer, FileTipUploader> dataMap = new HashMap<Integer, FileTipUploader>();

	String[] stringsC5 = new String[] { "更新了吗", "出来打牌", "在？看看牌", "把你的打牌图给我交了" };
	String[] strings = new String[] { "更新了吗", "出来更新" };

	public FileTipManager() {
	}

	public void addData(FileTipUploader r) {
		dataMap.put(mapFlag, r);
		mapFlag++;
	}

	// 新文件上传时
	public void onUploadFile(long groupNumber, long QQNumber) {
		for (int i = 0; i < mapFlag; i++) {
			FileTipUploader tftu = dataMap.get(i);
			if (tftu.getGroupNumber() == groupNumber && tftu.getQQNumber() == QQNumber) {
				tftu.setFileLastUpload(System.currentTimeMillis());
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < mapFlag; i++) {
				FileTipUploader tftu = dataMap.get(i);
				// System.currentTimeMillis()获取的是从1970年到当前过了多少毫秒
				if (System.currentTimeMillis() - tftu.getFileLastUpload() > 86400000// 一天为86400000毫秒
						&& System.currentTimeMillis() - tftu.getfileLastTipTime() > 3600000 * 3) {// 一小时为3600000毫秒
					if (tftu.getGroupNumber() == 807242547L && tftu.getQQNumber() == 1592608126L) {
						Autoreply.sendGroupMessage(tftu.getGroupNumber(),
								Autoreply.CC.at(tftu.getQQNumber()) + Methods.rfa(stringsC5));
						tftu.setfileLastTipTime(System.currentTimeMillis());
					} else {
						Autoreply.sendGroupMessage(tftu.getGroupNumber(),
								Autoreply.CC.at(tftu.getQQNumber()) + Methods.rfa(strings));
						tftu.setfileLastTipTime(System.currentTimeMillis());
					}
				}
			}
			try {
				sleep(60000);// 线程休眠60000毫秒
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
