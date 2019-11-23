package com.meng.musicProcess;

import com.meng.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.meng.gameData.TouHou.zun.*;

public class MusicManager {
	public static String musicFolder="";
	private HashMap<Long,String> resultMap=new HashMap<>();
	public MusicManager() {
		musicFolder = "C://thbgm/";
	}

	public File createMusicCut(int musicNum, String gameName, int needSeconeds, long fromQQ) {
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
			resultFile = new File(newFileName);
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
					new File(newFileName).delete();
				}
			});
		resultMap.put(fromQQ, TH10GameData.musicName[musicNum]);
		return resultFile;
	}

	public void judgeAnswer(long fromGroup, long fromQQ, String msg) {
		if(resultMap.get(fromQQ)==null){
			return;
		}
		if (isContainChinese(msg)) {
			String userAnswer=msg.replaceAll("[^\u4e00-\u9fa5]", "");
			String answer=resultMap.get(fromQQ).replaceAll("[^\u4e00-\u9fa5]", "");
			if (userAnswer.equals(answer)) {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答正确");
			} else {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + resultMap.get(fromQQ) + ",你也可以回答:" + resultMap.get(fromQQ).replaceAll("[^\u4e00-\u9fa5]", ""));
			}		
		} else {
			String userAnswer=msg.replaceAll("[^a-zA-Z\\s]", "");
			String answer=resultMap.get(fromQQ).replaceAll("[^a-zA-Z\\s]", "");
			if (userAnswer.equals(answer)) {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答正确");
			} else {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + resultMap.get(fromQQ) + ",你也可以回答:" + resultMap.get(fromQQ).replaceAll("[^\u4e00-\u9fa5]", ""));
			}
		}
		resultMap.remove(fromQQ);
	}

	public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
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
