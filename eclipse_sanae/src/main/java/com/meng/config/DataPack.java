package com.meng.config;

import com.meng.tools.*;
import java.util.*;

public class DataPack {

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

	public static final int _0notification=0;//通知
	public static final int _1getConfig=1;  //获取配置文件
	public static final int _2retConfig=2;//接收配置文件
	public static final int _3getOverSpell=3;//获取疮痍符卡
	public static final int _4retOverSpell=4;//接收疮痍
	public static final int _5getOverPersent=5;//获取疮痍进度
	public static final int _6retOverPersent=6;//接收疮痍进度
	public static final int _7getGrandma=7;//获取draw grandma
	public static final int _8retGrandma=8;//接收grandma
	public static final int _9getMusicName=9;//获取音乐名
	public static final int _10retMusicName=10;//接收音乐名
	public static final int _11getGotSpells=11;//获取符卡列表
	public static final int _12retGotSpells=12;//接收符卡列表
	public static final int _13getNeta=13;//获取neta
	public static final int _14retNeta=14;//接收neta
	public static final int _15incSpeak=15;//增加发言次数
	public static final int _16incPic=16;//增加图片次数
	public static final int _17incBilibili=17;//增加哔哩哔哩链接次数
	public static final int _18incRepeat=18;//增加复读次数
	public static final int _19incRepeatStart=19;//增加带领复读
	public static final int _20incRepeatBreak=20;//增加打断复读
	public static final int _21incBan=21;//增加被禁言次数
	public static final int _22decTime=22;//减少时间
	public static final int _23grass=23;//增加种草次数
	public static final int _24heartBeat=24;//心跳

	/*获取直播列表
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


	public static DataPack encode(int opCode, long timeStamp) {
		return new DataPack(opCode, timeStamp);
	}

	public static DataPack encode(int opCode, DataPack dataPack) {
		return new DataPack(opCode, dataPack);
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}

	public DataPack(int opCode, long timeStamp) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		write(BitConverter.getBytes(0));
		write(BitConverter.getBytes(headLength));
		write(BitConverter.getBytes((short)1));
		write(BitConverter.getBytes(timeStamp));
		write(BitConverter.getBytes(0L));
		write(BitConverter.getBytes(opCode));
	}   

	public DataPack(int opCode, DataPack dataPack) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		write(BitConverter.getBytes(0));
		write(BitConverter.getBytes(headLength));
		write(BitConverter.getBytes(dataPack.getVersion()));
		write(BitConverter.getBytes(dataPack.getTimeStamp()));
		write(BitConverter.getBytes(dataPack.getTarget()));
		write(BitConverter.getBytes(opCode));
	}   

	public DataPack(byte[] pack) {
		dataArray = pack;
		dataPointer = headLength;
		switch (getOpCode()) {
			case _0notification:
				break;
			case _1getConfig:
				break; 
			case _2retConfig:
				break;
			case _3getOverSpell:
				break;
			case _4retOverSpell:
				break; 
			case _5getOverPersent:
				break;
			case _6retOverPersent:
				break; 
			case _7getGrandma:
				break;
			case _8retGrandma:
				break;
			case _9getMusicName:	
				break;
			case _10retMusicName:
				break;
			case _11getGotSpells:
				break;
			case _12retGotSpells:
				break;
			case _13getNeta:
				break;
			case _14retNeta:
				break;
			case _15incSpeak:
				break;
			case _16incPic:
				break; 
			case _17incBilibili:
				break;
			case _18incRepeat:
				break; 
			case _19incRepeatStart:
				break;
			case _20incRepeatBreak:
				break;
			case _21incBan:
				break;	
			case _22decTime:
				break;
			case _23grass:
				break;
			case _24heartBeat:
				break;	
			default:
				break;
		}
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

	public void write(byte... bs) {
		for (byte b:bs) {
			data.add(b);
			++dataPointer;
		}
	}

	public void writeByte(byte b) {
		write(typeByte);
		write(b);
	}

	public void writeShort(short s) {
		write(typeShort);
		write(BitConverter.getBytes(s));
	}

	public void writeInt(int i) {
		write(typeInt);
		write(BitConverter.getBytes(i));
	}

	public void writeLong(long l) {
		write(typeLong);
		write(BitConverter.getBytes(l));
	}

	public void writeFloat(float f) {
		write(typeFloat);
		write(BitConverter.getBytes(f));
	}

	public void writeDouble(double d) {
		write(typeDouble);
		write(BitConverter.getBytes(d));
	}

	public void writeString(String s) {
		write(typeString);
		byte[] stringBytes = BitConverter.getBytes(s);
		writeInt(stringBytes.length);
		write(stringBytes);
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
