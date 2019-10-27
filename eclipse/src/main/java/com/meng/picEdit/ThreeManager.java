package com.meng.picEdit;

import com.meng.Autoreply;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import com.meng.config.javabeans.*;

public class ThreeManager {

	private HashMap<Long,Boolean> changeMap=new HashMap<>();
	private HashSet<Long> checkSet=new HashSet<>();

    public ThreeManager() {
		checkSet.add(2487765013L);
		checkSet.add(1033317031L);
		checkSet.addAll(Autoreply.instence.configManager.configJavaBean.adminList);
		checkSet.addAll(Autoreply.instence.configManager.configJavaBean.masterList);

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
							Thread.sleep(60000);
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
			downloadHead(qq, "");
			changeMap.put(qq, false);
			return;
		}
		if (downloadHead(qq, "a").length() == headImageFile.length()) {
			changeMap.put(qq, false);
			return;
		}
		changeMap.put(qq, true);
		PersonInfo pi=Autoreply.instence.configManager.getPersonInfoFromQQ(qq);
		String name;
		if (pi == null) {
			name = qq + "";
		} else {
			name = pi.name;
		}
		System.out.println(name + "更换了头像");
	}

	public boolean check(long fromGroup, long fromQQ) {
		if (changeMap.get(fromQQ) != null && changeMap.get(fromQQ)) {
			new JingShenZhiZhuQQManager(fromGroup, 0, Autoreply.instence.CC.at(fromQQ));
			changeMap.put(fromQQ, false);
			return true;
		}
		return false;
	}

	private File downloadHead(long qq, String a) {
        URL url;
		File headImageFile;
        try {
            url = new URL("http://q2.qlogo.cn/headimg_dl?bs=" + qq + "&dst_uin=" + qq + "&dst_uin=" + qq + "&;dst_uin=" + qq + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC");
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
			headImageFile = new File(Autoreply.appDirectory + "user\\" + a + qq + ".jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(headImageFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
			return null;
        }
		return headImageFile;
	}

	public void debug() {
		//changeMap.put(2856986197L, true);
		//Autoreply.sendMessage(Autoreply.mainGroup, 0, "ready");
		for (long qq:checkSet) {
			check(qq);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		}
	}
}
