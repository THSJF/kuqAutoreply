package com.meng.barcode;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.meng.Autoreply;
import com.meng.Methods;

public class BarcodeCreator extends Thread {
	private Long fromGroup = -1L;
	private Long fromQQ = -1L;
	private String text = "";

	public BarcodeCreator(Long fromGroup, Long fromQQ, String text) {
		this.fromGroup = fromGroup;
		this.fromQQ = fromQQ;
		this.text = text;
	}

	@Override
	public void run() {
		File files = new File(Autoreply.appDirectory + "barcode/" + fromQQ);
		if (!files.exists()) {
			files.mkdirs();
		}
		File barcode = new File(
				Autoreply.appDirectory + "barcode/" + fromQQ + "/barcode" + Autoreply.random.nextInt() + ".png");
		try {
			ImageIO.write(BarcodeUtils.createQRCode(text.replace("生成QR ", "")), "png", barcode);
			Methods.sendMsg(fromGroup, fromQQ, Autoreply.CC.image(barcode));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
