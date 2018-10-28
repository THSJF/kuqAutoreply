package com.meng.barcode;

import java.util.HashMap;

import com.meng.Autoreply;
import com.meng.Methods;
import com.sobte.cqp.jcq.entity.CQImage;

public class BarcodeManager {
	private HashMap<Long, String> userNotSendPicture = new HashMap<>();

	public BarcodeManager() {
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		if (enc(fromGroup, fromQQ, msg)) {
			return true;
		} else if (dec(fromGroup, fromQQ, msg)) {
			return true;
		}
		return false;
	}

	private boolean enc(long fromGroup, long fromQQ, String msg) {
		if (msg.startsWith("生成QR ") || msg.startsWith("生成PDF417 ")) {
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder(fromGroup, fromQQ, msg);
			barcodeEncoder.start();
			return true;
		}
		return false;
	}

	private boolean dec(long fromGroup, long fromQQ, String msg) {
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null && msg.startsWith("读取二维码")) {
			try {
				new BarcodeDecoder(fromGroup, fromQQ, cqImage.download(Autoreply.appDirectory + "barcode/" + fromQQ,
						"barcode" + Autoreply.random.nextInt() + ".png")).start();
			} catch (Exception e) {
				Methods.sendMsg(fromGroup, fromQQ, e.toString());
			}
			return true;
		} else if (cqImage == null && msg.startsWith("读取二维码")) {
			userNotSendPicture.put(fromQQ, msg);
			Methods.sendMsg(fromGroup, fromQQ, "需要一张图片");
			return true;
		} else if (cqImage != null && userNotSendPicture.get(fromQQ) != null) {
			try {
				new BarcodeDecoder(fromGroup, fromQQ, cqImage.download(Autoreply.appDirectory + "barcode/" + fromQQ,
						"barcode" + Autoreply.random.nextInt() + ".png")).start();
			} catch (Exception e) {
				Methods.sendMsg(fromGroup, fromQQ, e.toString());
			}
			userNotSendPicture.remove(fromQQ);
			return true;
		}
		return false;
	}

}
