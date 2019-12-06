package com.meng.config;

import com.meng.tools.*;
import java.util.*;

public class SanaeDataPack {

	public ArrayList<Byte> data=new ArrayList<>();
	public byte[] dataArray;
	public final short headLength=28;
	public int dataPointer=0;

	public final byte typeByte=0;
	public final byte typeShort=1;
	public final byte typeInt=2;
	public final byte typeLong=3;
	public final byte typeFloat=4;
	public final byte typeDouble=5;
	public final byte typeString=6;

	public static final int _0notification=0;//通知  string
	public static final int _1getConfig=1;  //获取配置文件
	public static final int _2retConfig=2;//接收配置文件  string(json)
	public static final int _3getOverSpell=3;//获取疮痍符卡 long(qq)
	public static final int _4retOverSpell=4;//接收疮痍符卡 string
	public static final int _5getOverPersent=5;//获取疮痍进度 long(qq)
	public static final int _6retOverPersent=6;//接收疮痍进度 int(0-10000)
	public static final int _7getGrandma=7;//获取draw grandma long(qq)
	public static final int _8retGrandma=8;//接收grandma string
	public static final int _9getMusicName=9;//获取音乐名 long(qq)
	public static final int _10retMusicName=10;//接收音乐名 string
	public static final int _11getGotSpells=11;//获取符卡列表 long(qq)
	public static final int _12retGotSpells=12;//接收符卡列表 string(json)
	public static final int _13getNeta=13;//获取neta long(qq)
	public static final int _14retNeta=14;//接收neta string
	public static final int _15incSpeak=15;//增加发言次数  long(group) long(qq)
	public static final int _16incPic=16;//增加图片次数 long (qq) 
	public static final int _17incBilibili=17;//增加哔哩哔哩链接次数  long(group) long(qq)
	public static final int _18incRepeat=18;//增加复读次数 long (qq)
	public static final int _19incRepeatStart=19;//增加带领复读  long(group) long(qq)
	public static final int _20incRepeatBreak=20;//增加打断复读  long(group) long(qq)
	public static final int _21incBan=21;//增加被禁言次数  long(group) long(qq)
	public static final int _22decTime=22;//减少时间  long(group) long(qq)
	public static final int _23grass=23;//增加种草次数  long(group) long(qq)
	public static final int _24heartBeat=24;//心跳
	public static final int _25setNick=25;//设置nickname long(qq)
	public static final int _26addBlack=26;//添加黑名单
	/*
	 
	 获取直播列表
	 返回直播列表
	 主播开播
	 主播下播
	 有人在直播间说话
	 新视频
	 新专栏
	 从称呼获取personInfo
	 从qq号获取personInfo
	 从bid获取personInfo
	 从blid获取personInfo
	 返回personInfo
	 */


	public static SanaeDataPack encode(int opCode, long timeStamp) {
		return new SanaeDataPack(opCode, timeStamp);
	}

	public static SanaeDataPack encode(int opCode, SanaeDataPack dataPack) {
		return new SanaeDataPack(opCode, dataPack);
	}

	public static SanaeDataPack decode(byte[] bytes) {
		return new SanaeDataPack(bytes);
	}

	private SanaeDataPack(int opCode, long timeStamp) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(BitConverter.getBytes(0));
		writeByteDataIntoArray(BitConverter.getBytes(headLength));
		writeByteDataIntoArray(BitConverter.getBytes((short)1));
		writeByteDataIntoArray(BitConverter.getBytes(timeStamp));
		writeByteDataIntoArray(BitConverter.getBytes(0L));
		writeByteDataIntoArray(BitConverter.getBytes(opCode));
	}   

	private SanaeDataPack(int opCode, SanaeDataPack dataPack) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(BitConverter.getBytes(0));
		writeByteDataIntoArray(BitConverter.getBytes(headLength));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getVersion()));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getTimeStamp()));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getTarget()));
		writeByteDataIntoArray(BitConverter.getBytes(opCode));
	}   

	private SanaeDataPack(byte[] pack) {
		dataArray = pack;
		dataPointer = headLength;
	} 

	public byte[] getData() {
		byte[] retData=new byte[data.size()];
		for (int i=0;i < data.size();++i) {
			retData[i] = data.get(i);
		}
		byte[] len=BitConverter.getBytes(retData.length);
		retData[0] = len[0];
		retData[1] = len[1];
		retData[2] = len[2];
		retData[3] = len[3];
		dataArray = retData;
		return retData;
	}

	public int getLength() {
		return BitConverter.toInt(dataArray, 0);
	}  

	public short getHeadLength() {
		return BitConverter.toShort(dataArray, 4);
	}

	public short getVersion() {
		return BitConverter.toShort(dataArray, 6);
	}

	public long getTimeStamp() {
		return BitConverter.toLong(dataArray, 8);
	}

	public long getTarget() {
		return BitConverter.toLong(dataArray, 16);
	}

	public int getOpCode() {
		return BitConverter.toShort(dataArray, 24);
	}

	public void writeByteDataIntoArray(byte... bs) {
		for (byte b:bs) {
			data.add(b);
			++dataPointer;
		}
	}

	public void write(byte b) {
		writeByteDataIntoArray(typeByte);
		writeByteDataIntoArray(b);
	}

	public void write(short s) {
		writeByteDataIntoArray(typeShort);
		writeByteDataIntoArray(BitConverter.getBytes(s));
	}

	public void write(int i) {
		writeByteDataIntoArray(typeInt);
		writeByteDataIntoArray(BitConverter.getBytes(i));
	}

	public void write(long l) {
		writeByteDataIntoArray(typeLong);
		writeByteDataIntoArray(BitConverter.getBytes(l));
	}

	public void write(float f) {
		writeByteDataIntoArray(typeFloat);
		writeByteDataIntoArray(BitConverter.getBytes(f));
	}

	public void write(double d) {
		writeByteDataIntoArray(typeDouble);
		writeByteDataIntoArray(BitConverter.getBytes(d));
	}

	public void write(String s) {
		writeByteDataIntoArray(typeString);
		byte[] stringBytes = BitConverter.getBytes(s);
		write(stringBytes.length);
		writeByteDataIntoArray(stringBytes);
	}

	public byte readByte() {
		if (dataArray[dataPointer++] == typeByte) {
			return dataArray[dataPointer++];
		}
		throw new NumberFormatException("not a byte number");
	}

	public short readShort() {
		if (dataArray[dataPointer++] == typeShort) {
			short s = BitConverter.toShort(dataArray, dataPointer);
			dataPointer += 2;
			return s;
		}
		throw new NumberFormatException("not a int number");
	}

	public int readInt() {
		if (dataArray[dataPointer++] == typeInt) {
			int i= BitConverter.toInt(dataArray, dataPointer);
			dataPointer += 4;
			return i;
		}
		throw new NumberFormatException("not a int number");
	}

	public long readLong() {
		if (dataArray[dataPointer++] == typeLong) {
			long l= BitConverter.toLong(dataArray, dataPointer);
			dataPointer += 8;
			return l;
		}
		throw new NumberFormatException("not a long number");
	}

	public float readFloat() {
		if (dataArray[dataPointer++] == typeFloat) {
			float f = BitConverter.toFloat(dataArray, dataPointer);
			dataPointer += 4;
			return f;
		}
		throw new NumberFormatException("not a int number");
	}

	public double readDouble() {
		if (dataArray[dataPointer++] == typeDouble) {
			double d = BitConverter.toDouble(dataArray, dataPointer);
			dataPointer += 8;
			return d;
		}
		throw new NumberFormatException("not a long number");
	}

	public String readString() {
		if (dataArray[dataPointer++] == typeString) {
			int len = readInt();
			String s = BitConverter.toString(dataArray, dataPointer, len);
			dataPointer += len;
			return s;
		}
		throw new NumberFormatException("not a string");
	}

}
