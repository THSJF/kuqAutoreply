package com.meng.musicProcess;

import java.io.*;
import java.util.*;
import com.meng.*;

public class MusicManager {
	public static String musicFolder="";
	public MusicManager() {
		musicFolder = "C://thbgm/";
	}

	public File createMusicCut(int musicNum, String gameName, int needSeconeds) {
		File fmtFile = new File(musicFolder + gameName + "/thbgm.fmt");
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
		final String newFileName="C://Users/Administrator/Desktop/酷Q Pro/data/record/" + System.currentTimeMillis() + ".wav";
		try {
			resultFile = new File(musicFolder + newFileName);
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
		Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {}
					new File(musicFolder + newFileName).delete();
				}
			});
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
