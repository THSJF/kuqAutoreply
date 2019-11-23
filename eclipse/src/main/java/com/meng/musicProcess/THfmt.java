package com.meng.musicProcess;

import java.io.*;

public class THfmt {
    private int position = 0;
    private byte[] fileByte;
    public MusicInfo[] musicInfos;
    public String[] names;
    private File fmt;

    public THfmt(File file) {
        if (!file.exists()) {
            throw new RuntimeException("file not found:" + file.getAbsolutePath());
        }
        fmt = file;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileByte = new byte[(int) file.length()];
            fileInputStream.read(fileByte);
        } catch (Exception e) {
            throw new RuntimeException("read file failed:" + file.getAbsolutePath());
        }
        musicInfos = new MusicInfo[(int) (file.length() / 52)];
        names = new String[musicInfos.length];
    }

    public void load() {
        for (int i = 0; i < musicInfos.length; ++i) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.name = readName();
            musicInfo.start = readInt();
            musicInfo.unknown1 = readInt();
            musicInfo.repeatStart = readInt();
            musicInfo.length = readInt();
            musicInfo.format = readShort();
            musicInfo.channels = readShort();
            musicInfo.rate = readInt();
            musicInfo.avgBytesPerSec = readInt();
            musicInfo.blockAlign = readShort();
            musicInfo.bitsPerSample = readShort();
            musicInfo.cbSize = readShort();
            musicInfo.pad = readShort();
            String name = new String(musicInfo.name);
            names[i] = name.substring(0, name.indexOf(0));
            musicInfos[i] = musicInfo;
        } 
    }

    private short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

    private int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

    private byte[] readName() {
        byte[] ba = new byte[16];
        for (int i = 0; i < ba.length; ++i) {
            ba[i] = fileByte[position++];
        }
        return ba;
    }
}
