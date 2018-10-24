package com.meng.barcode;

import java.io.File;
import com.google.zxing.Result;
import com.meng.Methods;

public class BarcodeDecoder extends Thread {

	private Long fromGroup = -1L;
	private Long fromQQ = -1L;
	private File img = null;

	public BarcodeDecoder(Long fromGroup, Long fromQQ, File img) {
		this.fromGroup = fromGroup;
		this.fromQQ = fromQQ;
		this.img = img;
	}

	@Override
	public void run() {
		Result result = null;
		result = BarcodeUtils.decodeImage(img);
		if (result != null) {
			Methods.sendMsg(fromGroup, fromQQ,
					"二维码类型:" + result.getBarcodeFormat().toString() + "\n内容:" + result.getText());
		} else {
			Methods.sendMsg(fromGroup, fromQQ, "此图片无法识别");
		}
	}
}
