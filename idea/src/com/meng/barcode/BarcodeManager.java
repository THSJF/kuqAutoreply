package com.meng.barcode;

import java.io.File;
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
		File barcode = new File(Autoreply.appDirectory + "barcode/" + fromQQ + "/barcode"
				+ Autoreply.instence.random.nextInt() + ".png");
		if (msg.startsWith("生成QR ")) {
			ImageIO.write(BarcodeUtils.createQRCode(msg.replace("生成QR ", "")), "png", barcode);
		} else if (msg.startsWith("生成PDF417 ")) {
			ImageIO.write(BarcodeUtils.createPDF417(msg.replace("生成PDF417 ", "")), "png", barcode);
		} else {
			return false;
		}
		Methods.sendMsg(fromGroup, fromQQ, Autoreply.instence.CC.image(barcode));
		barcode.delete();
		return true;
	}

	private boolean dec(long fromGroup, long fromQQ, String msg) throws Exception {
		CQImage cqImage = Autoreply.instence.CC.getCQImage(msg);
		if (cqImage != null) {
			File barcode = cqImage.download(Autoreply.appDirectory + "barcode/" + fromQQ,
					"barcode" + Autoreply.instence.random.nextInt() + ".png");
			Result result = null;
			result = BarcodeUtils.decodeImage(barcode);
			if (result != null) {
				Autoreply.sendMessage(fromGroup, fromQQ,
						"二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
				barcode.delete();
				return true;
			}
		}
		return false;
	}

}
