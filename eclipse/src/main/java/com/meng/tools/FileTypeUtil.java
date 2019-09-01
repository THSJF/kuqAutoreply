package com.meng.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtil {
    private Map<String, String> fileTypeMap = new HashMap<String, String>();

    public FileTypeUtil() {
        fileTypeMap.put("ffd8ffe000104a464946", "jpg"); //JPEG (jpg)
        fileTypeMap.put("89504e470d0a1a0a0000", "png"); //PNG (png)
        fileTypeMap.put("47494638396126026f01", "gif"); //GIF (gif)
        fileTypeMap.put("49492a00227105008037", "tif"); //TIFF (tif)
        fileTypeMap.put("424d228c010000000000", "bmp"); //16色位图(bmp)
        fileTypeMap.put("424d8240090000000000", "bmp"); //24位位图(bmp)
        fileTypeMap.put("424d8e1b030000000000", "bmp"); //256色位图(bmp)
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == src || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            byte b = src[i];
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public String getFileType(File file) {
        String res = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[3];
            fis.read(b, 0, b.length);
            String fileCode = bytesToHexString(b);
            for (String key : fileTypeMap.keySet()) {
                if (key.toLowerCase().startsWith(fileCode.toLowerCase()) || fileCode.toLowerCase().startsWith(key.toLowerCase())) {
                    res = fileTypeMap.get(key);
                    break;
                }
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public File checkFormat(File file) {
        File ret;
        String type = getFileType(file);
        String fullName = file.getName();
        String parent = file.getParent() + File.separator;
        String[] nameParts = fullName.split("\\.");
        switch (nameParts.length) {
            case 1:
                ret = new File(parent + fullName + "." + type);
                if (ret.exists()) {
                    ret.delete();
                }
                file.renameTo(ret);
                break;
            case 2:
                ret = new File(parent + nameParts[0] + "." + type);
                if (ret.exists()) {
                    ret.delete();
                }
                file.renameTo(ret);
                break;
            default:
                String fName = fullName.substring(0, fullName.lastIndexOf(".") - 1);
                ret = new File(parent + fName + "." + type);
                if (ret.exists()) {
                    ret.delete();
                }
                file.renameTo(ret);
                break;
        }
        return ret;
    }
}
