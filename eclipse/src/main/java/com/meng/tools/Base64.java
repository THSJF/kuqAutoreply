package com.meng.tools;

import java.io.*;

public class Base64 {
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static String encode(byte[] byteData) throws UnsupportedEncodingException {
        return encode(byteData, DEFAULT_ENCODING);
    }

    public static String encode(byte[] byteData, String encoding) throws UnsupportedEncodingException {
        if (byteData == null) { 
			throw new IllegalArgumentException("byteData cannot be null");
		}
        return new String(_encode(byteData), encoding);
    }

    public static byte[] encode(String string) throws UnsupportedEncodingException {
        return encode(string, DEFAULT_ENCODING);
    }

    public static byte[] encode(String string, String encoding) throws UnsupportedEncodingException {
        if (string == null) { 
			throw new IllegalArgumentException("string cannot be null");
		}
        return _encode(string.getBytes(encoding));
    }

    public final static byte[] _encode(byte[] byteData) {
        if (byteData == null) { 
			throw new IllegalArgumentException("byteData cannot be null");
		}
        int iSrcIdx; 
        int iDestIdx; 
		byte[] byteDest = new byte[((byteData.length + 2) / 3) * 4];
        for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < byteData.length - 2; iSrcIdx += 3) {
            byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
            byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
            byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 2] >>> 6) & 003 | (byteData[iSrcIdx + 1] << 2) & 077);
            byteDest[iDestIdx++] = (byte) (byteData[iSrcIdx + 2] & 077);
        }
        if (iSrcIdx < byteData.length) {
            byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
            if (iSrcIdx < byteData.length - 1) {
                byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
                byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] << 2) & 077);
            } else {
                byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] << 4) & 077);
			}
		}
        for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
            if (byteDest[iSrcIdx] < 26) {
                byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'A');
			} else if (byteDest[iSrcIdx] < 52) {
                byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'a' - 26);
			} else if (byteDest[iSrcIdx] < 62) {
                byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + '0' - 52);
			} else if (byteDest[iSrcIdx] < 63) {
                byteDest[iSrcIdx] = '+';
			} else {
                byteDest[iSrcIdx] = '/';
			}
		}
        for (; iSrcIdx < byteDest.length; iSrcIdx++) {
            byteDest[iSrcIdx] = '=';
		}
        return byteDest;
    }

    public static String decode(byte[] encoded) throws UnsupportedEncodingException {
        return decode(encoded, DEFAULT_ENCODING);
    }

    public static String decode(byte[] encoded, String encoding) throws UnsupportedEncodingException {
        if (encoded == null) { 
			throw new IllegalArgumentException("encoded cannot be null"); 
		}
        return new String(_decode(encoded), encoding);
    }

    public final static byte[] decode(String encoded) throws UnsupportedEncodingException {
        return decode(encoded, DEFAULT_ENCODING);
    }

    public final static byte[] decode(String encoded, String encoding) throws IllegalArgumentException, UnsupportedEncodingException {
        if (null == encoded) { 
			throw new IllegalArgumentException("encoded cannot be null");
		}
        return _decode(encoded.getBytes(encoding));
    }

    public final static byte[] _decode(byte[] byteData) throws IllegalArgumentException {
        if (byteData == null) { 
			throw new IllegalArgumentException("byteData cannot be null");
		}
        int iSrcIdx; 
        int reviSrcIdx; 
        int iDestIdx; 
        byte[] byteTemp = new byte[byteData.length];
        for (reviSrcIdx = byteData.length; reviSrcIdx - 1 > 0 && byteData[reviSrcIdx - 1] == '='; reviSrcIdx--) {
            ; // do nothing. I'm just interested in value of reviSrcIdx
        }

        if (reviSrcIdx - 1 == 0)	{ 
			return null; 
		}
        byte byteDest[] = new byte[((reviSrcIdx * 3) / 4)];
        for (iSrcIdx = 0; iSrcIdx < reviSrcIdx; iSrcIdx++) {
            if (byteData[iSrcIdx] == '+') {
                byteTemp[iSrcIdx] = 62;
            } else if (byteData[iSrcIdx] == '/') {
                byteTemp[iSrcIdx] = 63;
			} else if (byteData[iSrcIdx] < '0' + 10) {
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 52 - '0');
            } else if (byteData[iSrcIdx] < ('A' + 26)) {
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] - 'A');
			}  else if (byteData[iSrcIdx] < 'a' + 26) {
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 26 - 'a');
			}
		}

        for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < reviSrcIdx && iDestIdx < ((byteDest.length / 3) * 3); iSrcIdx += 4) {
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 2] << 6) & 0xC0 | byteTemp[iSrcIdx + 3] & 0x3F);
        }

        if (iSrcIdx < reviSrcIdx) {
            if (iSrcIdx < reviSrcIdx - 2) {
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
            } else if (iSrcIdx < reviSrcIdx - 1) {
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
            }  else {
                throw new IllegalArgumentException("Warning: 1 input bytes left to process. This was not Base64 input");
            }
        }
        return byteDest;
    }
}
