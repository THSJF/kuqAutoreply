package com.meng.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sobte.cqp.jcq.message.CQCode;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * Created by xingli on 12/25/15.
 * https://github.com/iluhcm/QrCodeScanner/blob/master/app/src/main/java/com/
 * kaola/qrcodescanner/qrcode/utils/QrUtils.java 二维码相关功能类
 */
public class QrUtils {

	public static BufferedImage createQRCode(String text) {
		return createBarcode(text, BarcodeFormat.QR_CODE, 0xff000000, 0xffffffff, 500);
	}

	public static BufferedImage createBarcode(String text, BarcodeFormat format, int true_dot_argb, int false_dot_argb,
			int size) {
		try {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			if (format == BarcodeFormat.AZTEC) {// 错误校正词的最小百分比
				hints.put(EncodeHintType.ERROR_CORRECTION, Encoder.DEFAULT_AZTEC_LAYERS);// 默认，可以不设
			} else if (format == BarcodeFormat.PDF_417) {
				hints.put(EncodeHintType.ERROR_CORRECTION, 2);// 纠错级别，允许为0到8。默认2，可以不设
			} else {
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			}
			BitMatrix bitMatrix = new MultiFormatWriter().encode(CQCode.decode(text), format, size, size, hints);
			int H = bitMatrix.getHeight();
			int W = bitMatrix.getWidth();

			BufferedImage im = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < W; x++) {
				for (int y = 0; y < H; y++) {
					im.setRGB(x, y, bitMatrix.get(x, y) == true ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
				}
			}
			return im;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
