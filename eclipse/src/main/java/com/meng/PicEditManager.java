package com.meng;

import com.meng.*;
import com.meng.picProcess.JingShenZhiZhuManager;
import com.meng.picProcess.ShenChuManager;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class PicEditManager {

	public static PicEditManager ins;
	public JingShenZhiZhuManager jszzm=new JingShenZhiZhuManager();
	public ShenChuManager scm=new ShenChuManager();

	public PicEditManager() {
		ins = this;
	}
	
	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("精神支柱[CQ:at")) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(jingShenZhiZhuByAt(fromGroup, fromQQ, msg)));
			return true;
		} else if (msg.startsWith("神触[CQ:at")) {
			Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(shenChuByAt(fromGroup, fromQQ, msg)));
			return true;
		} else if (Autoreply.instence.configManager.isMaster(fromQQ)) {
			if (msg.startsWith("精神支柱[CQ:image")) {
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(jingShenZhiZhuByPic(fromGroup, fromQQ, msg)));
                return true;
			} else if (msg.startsWith("神触[CQ:image")) {
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.image(shenChuByPic(fromGroup, fromQQ, msg)));
				return true;
			}
		}
		return false;
	}

	public File jingShenZhiZhuByPic(final long fromGroup, long fromQQ, final String msg) {
		CQImage cm = Autoreply.instence.CC.getCQImage(msg);
		if (cm == null) {
			return null;
		}
		try {
			return jszzm.create(cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg"));
		} catch (IOException e) {}	
		return null;
	}

	public File jingShenZhiZhuByAt(final long fromGroup, final long fromQQ, final String msg) {
		long id = Autoreply.instence.CC.getAt(msg);
		if (id == -1000 || id == -1) {
			id = fromQQ;
		}
		return jszzm.create(downloadHead(new File(Autoreply.appDirectory + "user\\" + id + ".jpg"), "http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin=" + id + "&;dst_uin=" + id + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC"), id);
	}

	public File shenChuByAt(final long fromGroup, final long fromQQ, final String msg) {
		long id = Autoreply.instence.CC.getAt(msg);
		if (id == -1000 || id == -1) {
			id = fromQQ;
		}
		return scm.create(downloadHead(new File(Autoreply.appDirectory + "user\\" + id + ".jpg"), "http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin=" + id + "&;dst_uin=" + id + "&spec=5&url_enc=0&referer=bu_interface&term_type=PC"), id);
	}

	public File shenChuByPic(final long fromGroup, long fromQQ, final String msg) {
		CQImage cm = Autoreply.instence.CC.getCQImage(msg);
		if (cm == null) {
			return null;
		}
		try {
			return jszzm.create(cm.download(Autoreply.appDirectory + "jingshenzhizhu\\" + System.currentTimeMillis() + ".jpg"));
		} catch (IOException e) {}	
		return null;
	}

	private File downloadHead(File image, String headUrl) {
        URL url;
        try {
            url = new URL(headUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(image);
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
        }
        return image;
    }
}
