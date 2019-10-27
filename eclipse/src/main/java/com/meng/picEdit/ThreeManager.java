package com.meng.picEdit;

import com.meng.Autoreply;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

public class ThreeManager {

	private HashMap<Long,Boolean> changeMap=new HashMap<>();
	private HashSet<Long> checkSet=new HashSet<>();

    public ThreeManager() {
		checkSet.add(2487765013L);

        Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						for (long qq:checkSet) {
							check(qq);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}
						}
						try {
							Thread.sleep(300000);
						} catch (InterruptedException e) {

						}
					}
				}
			});
    }

	private void check(long qq) {
		if (changeMap.get(qq) != null && changeMap.get(qq)) {
			return;
		}
        File folder = new File(Autoreply.appDirectory + "user\\");
        if (!folder.exists()) {
            folder.mkdirs();
        }
		File headImageFile = new File(Autoreply.appDirectory + "user\\" + qq + ".jpg");
		if (!headImageFile.exists()) {
			changeMap.put(qq, false);
			return;
		}
		try {
			URL url = new URL("http://q2.qlogo.cn/headimg_dl?bs=" + qq + "&dst_uin=" + qq + "&dst_uin=" + qq + "&;dst_uin=" + qq + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC");
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            if (dataInputStream.available() == headImageFile.length()) {
				changeMap.put(qq, false);
				return;
            }
        } catch (IOException e) {
            e.printStackTrace();
			changeMap.put(qq, false);
			return;
        }
		changeMap.put(qq, true);

	}

	public boolean check(long fromGroup, long fromQQ) {
		if (changeMap.get(fromQQ) != null && changeMap.get(fromQQ)) {
			new JingShenZhiZhuQQManager(fromGroup, 0, Autoreply.instence.CC.at(fromQQ));
			changeMap.put(fromQQ, false);
			return true;
		}
		return false;
	}
	public void debug() {
		changeMap.put(2856986197L, true);
		Autoreply.sendMessage(Autoreply.mainGroup,0,"ready");
	}
}
