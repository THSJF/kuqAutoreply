package com.meng.tip;

import com.meng.*;
import com.meng.tools.*;
import java.util.*;

public class FileTipManager implements Runnable {

    public HashSet<FileTipUploader> dataMap = new HashSet<>();

    private String[] stringsC5 = new String[]{"更新了吗", "出来打牌", "在？看看牌", "把你的打牌图给我交了"};
    private String[] strings = new String[]{"更新了吗", "出来更新"};

    public FileTipManager() {
    }

    // 新文件上传时
    public void onUploadFile(long groupNumber, long qqNumber) {
        for (FileTipUploader tftu : dataMap) {
            if (tftu.groupNumber == groupNumber && tftu.QQNumber == qqNumber) {
                tftu.fileLastUpload = System.currentTimeMillis();
            }
        }
    }

	public void addTip(long groupNumber, long QQNumber) {
		dataMap.add(new FileTipUploader(groupNumber, QQNumber));
	}

    @Override
    public void run() {
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            for (FileTipUploader tftu : dataMap) {
                if (System.currentTimeMillis() - tftu.fileLastUpload > 86400000 && System.currentTimeMillis() - tftu.fileLastTipTime > 7200000) {
                    if (tftu.groupNumber == 807242547L && tftu.QQNumber == 1592608126L) {
                        Autoreply.sendMessage(tftu.groupNumber, 0, Autoreply.instence.CC.at(tftu.QQNumber) + Tools.ArrayTool.rfa(stringsC5));
                        tftu.fileLastTipTime = System.currentTimeMillis();
                    } else {
                        Autoreply.sendMessage(tftu.groupNumber, 0, Autoreply.instence.CC.at(tftu.QQNumber) + Tools.ArrayTool.rfa(strings));
                        tftu.fileLastTipTime = System.currentTimeMillis();
                    }
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

	private class FileTipUploader {
		public long groupNumber = 0;
		public long QQNumber = 0;
		public long fileLastUpload = 0;
		public long fileLastTipTime = 0;

		public FileTipUploader(long groupNumber, long QQNumber) {
			this.groupNumber = groupNumber;
			this.QQNumber = QQNumber;
		}
	}
}
