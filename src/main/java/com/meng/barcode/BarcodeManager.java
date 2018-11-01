package com.meng.barcode;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.Result;
import com.meng.Autoreply;
import com.meng.Methods;
import com.sobte.cqp.jcq.entity.CQImage;

public class BarcodeManager {

	public BarcodeManager() {
	}

	public boolean check(long fromGroup, long fromQQ, String msg) {
		try {
			if (enc(fromGroup, fromQQ, msg)) {
				return true;
			} else if (dec(fromGroup, fromQQ, msg)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean enc(long fromGroup, long fromQQ, String msg) throws Exception {
		File files = new File(Autoreply.appDirectory + "barcode/" + fromQQ);
		if (!files.exists()) {
			files.mkdirs();
		}
		File barcode = new File(
				Autoreply.appDirectory + "barcode/" + fromQQ + "/barcode" + Autoreply.random.nextInt() + ".png");
		if (msg.startsWith("生成QR ")) {
			ImageIO.write(BarcodeUtils.createQRCode(msg.replace("生成QR ", "")), "png", barcode);
		} else if (msg.startsWith("生成PDF417 ")) {
			ImageIO.write(BarcodeUtils.createPDF417(msg.replace("生成PDF417 ", "")), "png", barcode);
		} else {
			return false;
		}
		Methods.sendMsg(fromGroup, fromQQ, Autoreply.CC.image(barcode));
		return true;
	}

	private boolean dec(long fromGroup, long fromQQ, String msg) throws Exception {
		CQImage cqImage = Autoreply.CC.getCQImage(msg);
		if (cqImage != null) {
			Result result = null;
			result = BarcodeUtils.decodeImage(cqImage.download(Autoreply.appDirectory + "barcode/" + fromQQ,
					"barcode" + Autoreply.random.nextInt() + ".png"));
			if (result != null) {
				if (fromGroup == -1) {
					Autoreply.sendPrivateMessage(fromQQ,
							"二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
				} else {
					Autoreply.sendGroupMessage(fromGroup,
							"二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
				}
			} else {
				return false;
			}
			return true;
		}
		return false;
	}

}
