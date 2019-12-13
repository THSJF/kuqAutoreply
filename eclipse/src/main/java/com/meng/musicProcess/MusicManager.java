package com.meng.musicProcess;

import com.meng.*;
import com.meng.gameData.TouHou.zun.*;
import com.meng.tools.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

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
		byte[] finalFile=Tools.ArrayTool.mergeArray(wavHeader.getWavHeader(musicNum, thfmt, needSeconeds), music);
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
		switch (games[game].getName()) {
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

	class WavHeader {
		private byte[] header;
		private int writePointer=0;
		public byte[] getWavHeader(int num, THfmt fmt, int second) {
			MusicInfo mi=fmt.musicInfos[num];
			int oneSecBytes=mi.bitsPerSample * mi.channels * mi.rate / 8;
			header = new byte[44];
			write("RIFF");	//ckid：4字节 RIFF 标志，大写
			write(second * oneSecBytes + 44 - 8);//cksize：4字节文件长度，这个长度不包括"RIFF"标志(4字节)和文件长度本身所占字节(4字节),即该长度等于整个文件长度 - 8  
			write("WAVE");//fcc type：4字节 "WAVE" 类型块标识, 大写  
			write("fmt ");//ckid：4字节 表示"fmt" chunk的开始,此块中包括文件内部格式信息，小写, 最后一个字符是空格  
			write((int)mi.bitsPerSample);//cksize：4字节，文件内部格式信息数据的大小，过滤字节（一般为00000010H）  
			write(mi.format);//FormatTag：2字节，音频数据的编码方式，1：表示是PCM 编码  
			write(mi.channels);//Channels：2字节，声道数，单声道为1，双声道为2 
			write(mi.rate);//SamplesPerSec：4字节，采样率，如44100  
			write(oneSecBytes);//BytesPerSec：4字节，音频数据传送速率, 单位是字节。其值为采样率×每次采样大小。播放软件利用此值可以估计缓冲区的大小；  
			write(mi.blockAlign);//BlockAlign：2字节，每次采样的大小 = 采样精度*声道数/8(单位是字节); 这也是字节对齐的最小单位, 譬如 16bit 立体声在这里的值是 4 字节。  
			write(mi.bitsPerSample);//BitsPerSample：2字节，每个声道的采样精度; 譬如 16bit 在这里的值就是16。如果有多个声道，则每个声道的采样精度大小都一样的；  
			write("data");//ckid：4字节，数据标志符（data），表示 "data" chunk的开始。此块中包含音频数据，小写；  
			write(second * oneSecBytes);//cksize：音频数据的长度，4字节，audioDataLen = totalDataLen - 36 = fileLenIncludeHeader - 44  
			return header;
		}

		private void write(byte[] bs) {
			for (int i=0;i < bs.length;++i) {
				header[writePointer++] = bs[i];
			}
		}

		private void write(String s) {
			for (int i=0;i < s.length();++i) {
				write(s.charAt(i));
			}
		}

		private void write(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 0) & 0xff);
			bs[1] = (byte) ((i >> 8) & 0xff);
			bs[2] = (byte) ((i >> 16) & 0xff);
			bs[3] = (byte) ((i >> 24) & 0xff);
			write(bs);
		}

		private void write(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 0) & 0xff);
			bs[1] = (byte) ((s >> 8) & 0xff);
			write(bs);
		}

		private void write(char c) {
			header[writePointer++] = (byte) c;
		}

	}
}
