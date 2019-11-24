package com.meng.musicProcess;

import com.meng.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.meng.gameData.TouHou.zun.*;
import com.meng.tools.*;

public class MusicManager {
	public static String musicFolder="";
	private HashMap<Long,String> resultMap=new HashMap<>();
	public MusicManager() {
		musicFolder = "C://thbgm/";
	}

	public File createMusicCut(int musicNum, int needSeconeds, long fromQQ) {
		File[] games=new File(musicFolder).listFiles();
		int game=new Random().nextInt(games.length);
		File fmtFile = new File(musicFolder + games[game].getName() + "/thbgm.fmt");
		File resultFile=null;
		THfmt thfmt = new THfmt(fmtFile);
        thfmt.load();
		MusicInfo muiscInfo=thfmt.musicInfos[musicNum];
		byte[] music=new byte[needSeconeds * muiscInfo.bitsPerSample * muiscInfo.channels * muiscInfo.rate / 8];
		readFile(music, getStartBytes(musicNum, thfmt, needSeconeds), games[game].getName());
		WavHeader wavHeader=new WavHeader();
		byte[] finalFile=Methods.mergeArray(wavHeader.getWavHeader(musicNum, thfmt, needSeconeds), music);
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
			switch(games[game].getName()){
				case "th10":
					resultMap.put(fromQQ, TH10GameData.musicName[musicNum]);
					break;
				case "th15":
					resultMap.put(fromQQ, TH15GameData.musicName[musicNum]);
					break;
			}
		
		return resultFile;
	}

	public void judgeAnswer(long fromGroup, long fromQQ, String msg) {
		if (resultMap.get(fromQQ) == null) {
			return;
		}
		if (isContainChinese(msg)) {
			String userAnswer=msg.replaceAll("[^\u4e00-\u9fa5]", "");
			String answer=resultMap.get(fromQQ).replaceAll("[^\u4e00-\u9fa5]", "");
			if (userAnswer.equals(answer)) {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答正确");
			} else {
				String orignal=resultMap.get(fromQQ);
				String replaced=orignal.replaceAll("[^\u4e00-\u9fa5]", "");
				if (orignal.equals(replaced)) {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + resultMap.get(fromQQ));			
				} else {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + orignal + ",你也可以回答:" + replaced);
				}
			}		
		} else {
			String userAnswer=msg.replaceAll("[^a-zA-Z\\s]", "");
			String answer=resultMap.get(fromQQ).replaceAll("[^a-zA-Z\\s]", "");
			if (userAnswer.equalsIgnoreCase(answer)) {
				Autoreply.sendMessage(fromGroup, fromQQ, "回答正确");
			} else {
				String orignal=resultMap.get(fromQQ);
				String replaced=orignal.replaceAll("[^\u4e00-\u9fa5]", "");
				if (orignal.equalsIgnoreCase(replaced)) {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + resultMap.get(fromQQ));			
				} else {
					Autoreply.sendMessage(fromGroup, fromQQ, "回答错误,答案是:" + orignal + ",你也可以回答:" + replaced);
				}
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
