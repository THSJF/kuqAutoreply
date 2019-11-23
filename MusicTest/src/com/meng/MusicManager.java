package com.meng;

import java.io.*;
import java.util.*;

public class MusicManager {
	public static String musicFolder="";
	public MusicManager() {
		musicFolder = "/sdcard/thbgm/";
	}

	public File init(int musicNum, int needSeconeds) {
		File fmtFile = new File(musicFolder + "th10/thbgm.fmt");
		File resultFile=null;
		THfmt thfmt = new THfmt(fmtFile);
        thfmt.load();
		MusicInfo muiscInfo=thfmt.musicInfos[musicNum];
		int oneSecBytes=muiscInfo.bitsPerSample * muiscInfo.channels * muiscInfo.rate / 8;
		byte[] music=new byte[needSeconeds * oneSecBytes];
		readFile(music, getStartBytes(musicNum, thfmt, needSeconeds), "th10");
		WavHeader wavHeader=new WavHeader();
		byte[] header=wavHeader.getWavHeader(musicNum, thfmt, needSeconeds);
		byte[] finalFile=new byte[music.length + header.length];
		int flag=0;
		for (;flag < header.length;++flag) {
			finalFile[flag] = header[flag];
		}
		for (;flag < music.length;++flag) {
			finalFile[flag] = music[flag];
		}
		try {
			resultFile = new File(musicFolder + "1.wav");
			if (resultFile.exists()) {
				resultFile.delete();
			}
			FileOutputStream fom=new FileOutputStream(resultFile);
			fom.write(finalFile, 0, finalFile.length);
			fom.flush();
			fom.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultFile;
	}

	public int getStartBytes(int musicNum, THfmt thfmt, int needSeconeds) {
		MusicInfo muiscInfo=thfmt.musicInfos[musicNum];
		int oneFrameBytes=muiscInfo.bitsPerSample / 8 * muiscInfo.channels;	
		int startFtame=new Random().nextInt(muiscInfo.length / oneFrameBytes);
		int SecNeedBytes=needSeconeds * muiscInfo.bitsPerSample * muiscInfo.channels * muiscInfo.rate / 8;
		int questionStartBytes=muiscInfo.start + startFtame * oneFrameBytes;
		if (muiscInfo.length - startFtame * oneFrameBytes < SecNeedBytes) {
			questionStartBytes = startFtame * oneFrameBytes - SecNeedBytes;
		}
		return questionStartBytes;
	}

	public byte[] readFile(byte[] data, int offset, String name) {
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(musicFolder + name + "/thbgm.dat", "r");
            randomAccessFile.seek(offset);
            randomAccessFile.readFully(data);
            randomAccessFile.close();
        } catch (Exception e) {
            throw new RuntimeException("bgm read failed");
        }
        return data;
    }

}
