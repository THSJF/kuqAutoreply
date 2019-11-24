package com.meng.musicProcess;

public class WavHeader {
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
}
