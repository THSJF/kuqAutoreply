package com.meng.barcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AwesomeQRCode {
    /**
     * For more information Welcome QR code, refer to: https://en.wikipedia.org/wiki/QR_code
     * BYTE_EPT: Empty block
     * BYTE_DTA: Data block
     * BYTE_POS: Position block
     * BYTE_AGN: Align block
     * BYTE_TMG: Timing block
     * BYTE_PTC: Protector block, translucent layer (custom block, this is not included in QR code's standards)
     */
    private static final int BYTE_EPT = 0x0;
    private static final int BYTE_DTA = 0x1;
    private static final int BYTE_POS = 0x2;
    private static final int BYTE_AGN = 0x3;
    private static final int BYTE_TMG = 0x4;
    private static final int BYTE_PTC = 0x5;

    /**
     * This default value makes data blocks appear smaller.
     */
    private static float DEFAULT_DTA_DOT_SCALE = 0.3f;
    private static int DEFAULT_BINARIZING_THRESHOLD = 128;

    /**
     * Create a QR matrix and render it use given configs.
     *
     * @param contents        Contents to encode.
     * @param size            Width as well as the height of the output QR code, includes margin.
     * @param dataDotScale    Scale the data blocks and makes them appear smaller.
     * @param colorLight      Color of empty space. Will be OVERRIDE by autoColor. (BYTE_EPT)
     * @param backgroundImage The background image to embed in the QR code. If null, no background image will be embedded.
     * @return Bitmap of QR code
     * @throws IllegalArgumentException Refer to the messages below.
     */
    public static BufferedImage create(String contents, int size, float dataDotScale, int colorLight, BufferedImage backgroundImage) {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Error: contents is empty. (contents.isEmpty())");
        }
        if (size < 0) {
            throw new IllegalArgumentException("Error: a negative size is given. (size < 0)");
        }
        if (dataDotScale < 0 || dataDotScale > 1) {
            throw new IllegalArgumentException("Error: an illegal data dot scale is given. (dataDotScale < 0 || dataDotScale > 1)");
        }
        ByteMatrix byteMatrix = getBitMatrix(contents);
        if (byteMatrix != null) {
            return render(byteMatrix, size, dataDotScale, colorLight, backgroundImage);
        }
        return null;
    }

    private static BufferedImage render(ByteMatrix byteMatrix, int innerRenderedSize, float dataDotScale, int colorLight, BufferedImage backgroundImage) {
        int nCount = byteMatrix.getWidth();
        float nWidth = (float) innerRenderedSize / nCount;
        float nHeight = (float) innerRenderedSize / nCount;

        BufferedImage backgroundImageScaled = new BufferedImage(innerRenderedSize, innerRenderedSize, BufferedImage.TYPE_INT_ARGB);
        if (backgroundImage != null) {
            backgroundImageScaled.getGraphics().drawImage(backgroundImage.getScaledInstance(innerRenderedSize, innerRenderedSize, Image.SCALE_SMOOTH), 0, 0, null);
        }
        BufferedImage renderedBitmap = new BufferedImage(innerRenderedSize, innerRenderedSize, BufferedImage.TYPE_INT_ARGB);
        renderedBitmap.getGraphics().drawImage(backgroundImageScaled.getScaledInstance(renderedBitmap.getWidth(), renderedBitmap.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
        Graphics2D graphics2d = (Graphics2D) renderedBitmap.getGraphics();

        Color paintDark = new Color(getDominantColor(backgroundImageScaled));
        Color paintLight = new Color(colorLight);
        Color paintProtector = new Color(255, 255, 255, 120);

        for (int row = 0; row < byteMatrix.getHeight(); row++) {
            for (int col = 0; col < byteMatrix.getWidth(); col++) {
                switch (byteMatrix.get(col, row)) {
                    case BYTE_AGN:
                    case BYTE_POS:
                    case BYTE_TMG:
                        graphics2d.setColor(paintDark);
                        graphics2d.drawRect(
                                (int) (col * nWidth),
                                (int) (row * nHeight),
                                (int) ((col + 1) * nWidth),
                                (int) ((row + 1) * nHeight));
                        break;
                    case BYTE_DTA:
                        graphics2d.setColor(paintDark);
                        graphics2d.drawRect(
                                (int) ((col + 0.5f * (1 - dataDotScale)) * nWidth),
                                (int) ((row + 0.5f * (1 - dataDotScale)) * nHeight),
                                (int) ((col + 0.5f * (1 + dataDotScale)) * nWidth),
                                (int) ((row + 0.5f * (1 + dataDotScale)) * nHeight));
                        break;
                    case BYTE_PTC:
                        graphics2d.setColor(paintProtector);
                        graphics2d.drawRect(
                                (int) (col * nWidth),
                                (int) (row * nHeight),
                                (int) ((col + 1.0f) * nWidth),
                                (int) ((row + 1.0f) * nHeight));
                        break;
                    case BYTE_EPT:
                        graphics2d.setColor(paintLight);
                        graphics2d.drawRect(
                                (int) ((col + 0.5f * (1 - dataDotScale)) * nWidth),
                                (int) ((row + 0.5f * (1 - dataDotScale)) * nHeight),
                                (int) ((col + 0.5f * (1 + dataDotScale)) * nWidth),
                                (int) ((row + 0.5f * (1 + dataDotScale)) * nHeight));
                        break;
                }
            }
        }
        return renderedBitmap;
    }

    private static ByteMatrix getBitMatrix(String contents) {
        try {
            QRCode qrCode = getProtoQRCode(contents, ErrorCorrectionLevel.H);
            int[] agnCenter = qrCode.getVersion().getAlignmentPatternCenters();
            ByteMatrix byteMatrix = qrCode.getMatrix();
            int matSize = byteMatrix.getWidth();
            for (int row = 0; row < matSize; row++) {
                for (int col = 0; col < matSize; col++) {
                    if (isTypeAGN(col, row, agnCenter, true)) {
                        if (byteMatrix.get(col, row) != BYTE_EPT) {
                            byteMatrix.set(col, row, BYTE_AGN);
                        } else {
                            byteMatrix.set(col, row, BYTE_PTC);
                        }
                    } else if (isTypePOS(col, row, matSize, true)) {
                        if (byteMatrix.get(col, row) != BYTE_EPT) {
                            byteMatrix.set(col, row, BYTE_POS);
                        } else {
                            byteMatrix.set(col, row, BYTE_PTC);
                        }
                    } else if (isTypeTMG(col, row, matSize)) {
                        if (byteMatrix.get(col, row) != BYTE_EPT) {
                            byteMatrix.set(col, row, BYTE_TMG);
                        } else {
                            byteMatrix.set(col, row, BYTE_PTC);
                        }
                    }
                    if (isTypePOS(col, row, matSize, false)) {
                        if (byteMatrix.get(col, row) == BYTE_EPT) {
                            byteMatrix.set(col, row, BYTE_PTC);
                        }
                    }
                }
            }
            return byteMatrix;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param contents             Contents to encode.
     * @param errorCorrectionLevel ErrorCorrectionLevel
     * @return QR code object.
     * @throws WriterException Refer to the messages below.
     */
    private static QRCode getProtoQRCode(String contents, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }
        Map<EncodeHintType, Object> hintMap = new HashMap<>();

        hintMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hintMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        return Encoder.encode(contents, errorCorrectionLevel, hintMap);
    }

    private static boolean isTypeAGN(int x, int y, int[] agnCenter, boolean edgeOnly) {
        if (agnCenter.length == 0) {
            return false;
        }
        int edgeCenter = agnCenter[agnCenter.length - 1];
        for (int agnY : agnCenter) {
            for (int agnX : agnCenter) {
                if (edgeOnly && agnX != 6 && agnY != 6 && agnX != edgeCenter && agnY != edgeCenter) {
                    continue;
                }
                if ((agnX == 6 && agnY == 6) || (agnX == 6 && agnY == edgeCenter) || (agnY == 6 && agnX == edgeCenter)) {
                    continue;
                }
                if (x >= agnX - 2 && x <= agnX + 2 && y >= agnY - 2 && y <= agnY + 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isTypePOS(int x, int y, int size, boolean inner) {
        if (inner) {
            return ((x < 7 && (y < 7 || y >= size - 7)) || (x >= size - 7 && y < 7));
        } else {
            return ((x <= 7 && (y <= 7 || y >= size - 8)) || (x >= size - 8 && y <= 7));
        }
    }

    private static boolean isTypeTMG(int x, int y, int size) {
        return ((y == 6 && (x >= 8 && x < size - 8)) || (x == 6 && (y >= 8 && y < size - 8)));
    }


    private static int getDominantColor(BufferedImage bitmap) {
        int red = 0, green = 0, blue = 0, c = 0;
        int r, g, b;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getHeight(); x++) {
                int color = bitmap.getRGB(x, y);
                r = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                b = color & 0xFF;
                if (r > 200 || g > 200 || b > 200) {
                    continue;
                }
                red += r;
                green += g;
                blue += b;
                c++;
            }
        }
        c++;
        red = Math.max(0, Math.min(0xFF, red / c));
        green = Math.max(0, Math.min(0xFF, green / c));
        blue = Math.max(0, Math.min(0xFF, blue / c));
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

}

