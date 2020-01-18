package com.meng.picProcess.barcode;

import com.google.zxing.*;
import com.google.zxing.aztec.encoder.*;
import com.google.zxing.common.*;
import com.google.zxing.qrcode.decoder.*;
import com.sobte.cqp.jcq.message.*;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Created by xingli on 12/25/15.
 * https://github.com/iluhcm/QrCodeScanner/blob/masterList/app/src/main/java/com/
 * kaola/qrcodescanner/qrcode/utils/QrUtils.java 二维码相关功能类
 */
public class BarcodeUtils {

    public static BufferedImage createQRCode(String text) {
        return createBarcode(text, BarcodeFormat.QR_CODE, 500);
    }

    public static BufferedImage createPDF417(String text) {
        return createBarcode(text, BarcodeFormat.PDF_417, 500);
    }

    public static BufferedImage createBarcode(String text, BarcodeFormat format,
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
            int h = bitMatrix.getHeight();
            int w = bitMatrix.getWidth();

            BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    im.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            return im;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Result decodeImage(File img) {
        BufferedImage image;
        Result result = null;
        try {
            image = ImageIO.read(img);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> map = new HashMap<DecodeHintType, Object>();
            map.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            result = new MultiFormatReader().decode(bitmap, map);
        } catch (Exception e1) {
        }
        return result;
    }
}
