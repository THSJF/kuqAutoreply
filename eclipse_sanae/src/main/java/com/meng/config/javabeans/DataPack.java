package com.meng.config.javabeans;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.meng.*;
import java.lang.reflect.*;
import com.google.gson.reflect.*;
import com.meng.tools.*;

public class DataPack {

	private ArrayList<Byte> data=new ArrayList<>();
	private byte[] dataArray;
	private final short headLength=26;
	private int dataPointer=0;

	private final byte typeByte=0;
	private final byte typeShort=1;
	private final byte typeInt=2;
	private final byte typeLong=3;
	private final byte typeFloat=4;
	private final byte typeDouble=5;
	private final byte typeString=6;


	//  普通通知,大部分情况下没什么用   s1:通知文本
	public static final short _0notification=0;
	//  用于身份验证，暂未使用    n1:qq号(setConnect中设置的qq号)
	public static final short _1verify=1;
	//  获取正在直播列表  不需要body
	public static final short _2getLiveList=2;
	//  返回2的查询结果  json数组  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _3returnLiveList=3;
	//  主播开始直播   n1:直播间号 s1:主播称呼
	public static final short _4liveStart=4;
	//  主播停止直播   n1:直播间号 s1:主播称呼
	public static final short _5liveStop=5;
	//  直播观看者在直播间发送的弹幕   n1:直播间号 s1:主播称呼 n2:说话者BID s2:说话者称呼,如果配置文件中没有就是用户名 s3:说话内容
	public static final short _6speakInLiveRoom=6;
	//  up主发布新视频  s1:用户名 s2:视频名 n1:AV号
	public static final short _7newVideo=7;
	//  up主发布新专栏  s1:用户名 s2:专栏名 n1:CV号
	public static final short _8newArtical=8;
	//  从称呼获得人员信息(完全匹配方式查找)  s1:称呼
	public static final short _9getPersonInfoByName=9;
	//  从qq获得人员信息(完全匹配方式查找)  n1:qq号
	public static final short _10getPersonInfoByQQ=10;
	//  从bid获得人员信息(完全匹配方式查找)  n1:BID
	public static final short _11getPersonInfoByBid=11;
	//  从直播间号获得人员信息(完全匹配方式查找)  n1:直播间号
	public static final short _12getPersonInfoByBiliLive=12;
	//  返回 _9 _10 _11 _12的查询结果  返回结果例:{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]}
	public static final short _13returnPersonInfo=13;
	//  给指定qq号添加幻币  n1:幻币数量 n2:目标qq号
	public static final short _14coinsAdd=14;
	//  qq群中禁言  n1:群号 n2:QQ号 n3:时间(秒)
	public static final short _15groupBan=15;
	//  踢出qq群  n1:群号 n2:QQ号 n3:是否永久拒绝 0为否 1为是
	public static final short _16groupKick=16;
	//  心跳，不需要body 返回一个操作类型为0的通知
	public static final short _17heartBeat=17;
	//  同在QQ中的"findInAll"指令  n1:qq号
	public static final short _18FindInAll=18;
	//  返回18的结果   返回结果例[296376859,251059118]
	public static final short _19returnFind=19;
	//  获得"精神支柱"表情包  n1:qq号
	public static final short _20pic=20;
	//  返回_20生成的jpg文件 数据部分直接保存到磁盘即可
	public static final short _21returnPic=21;
	//  获得"神触"表情包  n1:qq号
	public static final short _22pic2=22;
	//  返回_22生成的jpg文件 数据部分直接保存到磁盘即可
	public static final short _23returnPic2=23;
	//  获得鬼人正邪".jrrp"中的计算结果  n1:qq号
	public static final short _24MD5Random=24;
	//  返回_24的结果 n1:计算结果   (0-10000的整数)
	public static final short _25returnMD5Random=25;
	//  获得鬼人正邪".draw neta"中的计算结果  n1:qq号
	public static final short _26MD5neta=26;
	//  返回_26的结果 s1:计算结果
	public static final short _27returnMD5neta=27;
	//  获得鬼人正邪".draw music"中的计算结果  n1:qq号
	public static final short _28MD5music=28;
	//  返回_28的结果 s1:计算结果
	public static final short _29returnMD5music=29;
	//  获得鬼人正邪".draw grandma"中的计算结果  n1:qq号
	public static final short _30MD5grandma=30;
	//  返回_30的结果 s1:计算结果
	public static final short _31returnMD5grandma=31;
	//  获得鬼人正邪"。jrrp"中的计算结果  n1:qq号
	public static final short _32MD5overSpell=32;
	//  返回_32的结果 s1:计算结果
	public static final short _33returnMD5overSpell=33;
	//  发送直播间弹幕 s1:弹幕内容 s2:屑站账号cookie n1:直播间号
	public static final short _34sendDanmaku=34;
	//  设置群名片 n1:群号 n2:目标qq号 s1:名片内容
	public static final short _37setGroupName=37;
	//  设置群头衔  n1:群号 n2:目标qq号 n3:有效时间，单位为秒，无限期写-1 s1:头衔内容
	public static final short _38setSpecialTitle=38;
	/*
	 //  获得原曲认知问题 n1:需要的秒数
	 public static final short _39getMusicQueation=39;
	 // 
	 */
	/*******    希望小律影提供的    *******/
	//  加群审核 n1:加群申请id n2:群号 n3:qq号
	public static final short _35groupAdd=35;
	//  回复加群审核 n1:加群申请id n2:是否同意(0拒绝 1同意) s1:同意或拒绝的理由 s2:此人的称呼(如果有)(优先使用.nn设置的称呼)
	public static final short _36returnGroupAdd=36;



	public static DataPack encode(short opCode, long timeStamp) {
		return new DataPack(opCode, timeStamp);
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}

	private DataPack(short opCode, long timeStamp) {
		write(BitConverter.getBytes(0));
		write(BitConverter.getBytes(headLength));
		write(BitConverter.getBytes((short)1));
		write(BitConverter.getBytes(timeStamp));
		write(BitConverter.getBytes(0));
		write(BitConverter.getBytes(opCode));
	}   

	private DataPack(byte[] pack) {
		dataArray = pack;
		switch (getOpCode()) {
			case DataPack._0notification:
				break;
			case DataPack._1verify:
				break; 
			case DataPack._2getLiveList:
				break;
			case DataPack._3returnLiveList:
				break;
			case DataPack._4liveStart:
				break; 
			case DataPack._5liveStop:
				break;
			case DataPack._6speakInLiveRoom:
				break; 
			case DataPack._7newVideo:
				break;
			case DataPack._8newArtical:
				break;
			case DataPack._9getPersonInfoByName:	
				break;
			case DataPack._10getPersonInfoByQQ:
				break;
			case DataPack._11getPersonInfoByBid:
				break;
			case DataPack._12getPersonInfoByBiliLive:
				break;
			case DataPack._13returnPersonInfo:
				break;
			case DataPack._14coinsAdd:
				break;
			case DataPack._15groupBan:
				break;
			case DataPack._16groupKick:
				break; 
			case DataPack._17heartBeat:
				break;
			case DataPack._18FindInAll:
				break; 
			case DataPack._19returnFind:
				break;
			case DataPack._20pic:
				break;
			case DataPack._21returnPic:
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

	public short getOpCode() {
		return BitConverter.toShort(dataArray, 24);
	}

	private void write(byte... bs) {
		for (byte b:bs) {
			data.add(b);
			++dataPointer;
		}
	}

	private void writeByte(byte b) {
		write(typeByte);
		write(b);
	}

	private void writeShort(short s) {
		write(typeShort);
		write(BitConverter.getBytes(s));
	}

	private void writeInt(int i) {
		write(typeInt);
		write(BitConverter.getBytes(i));
	}

	private void writeLong(long l) {
		write(typeLong);
		write(BitConverter.getBytes(l));
	}

	private void writeFloat(float f) {
		write(typeFloat);
		write(BitConverter.getBytes(f));
	}

	private void writeDouble(double d) {
		write(typeDouble);
		write(BitConverter.getBytes(d));
	}

	private void writeString(String s) {
		write(typeString);
		writeInt(s.length());
		write(BitConverter.getBytes(s));
	}

	private byte readByte() {
		if (dataArray[dataPointer++] == typeByte) {
			return dataArray[dataPointer++];
		}
		throw new NumberFormatException("not a byte number");
	}

	private short readShort() {
		if (dataArray[dataPointer++] == typeShort) {
			short s = BitConverter.toShort(dataArray, dataPointer);
			dataPointer += 2;
			return s;
		}
		throw new NumberFormatException("not a int number");
	}

	private int readInt() {
		if (dataArray[dataPointer++] == typeInt) {
			int i= BitConverter.toInt(dataArray, dataPointer);
			dataPointer += 4;
			return i;
		}
		throw new NumberFormatException("not a int number");
	}

	private long readLong() {
		if (dataArray[dataPointer++] == typeLong) {
			long l= BitConverter.toLong(dataArray, dataPointer);
			dataPointer += 8;
			return l;
		}
		throw new NumberFormatException("not a long number");
	}

	private float readFloat() {
		if (dataArray[dataPointer++] == typeFloat) {
			float f = BitConverter.toFloat(dataArray, dataPointer);
			dataPointer += 4;
			return f;
		}
		throw new NumberFormatException("not a int number");
	}

	private double readDouble() {
		if (dataArray[dataPointer++] == typeDouble) {
			double d = BitConverter.toDouble(dataArray, dataPointer);
			dataPointer += 8;
			return d;
		}
		throw new NumberFormatException("not a long number");
	}

	private String readString() {
		if (dataArray[dataPointer++] == typeString) {
			int len = readInt();
			String s = BitConverter.toString(dataArray, dataPointer, len);
			dataPointer += len;
			return s;
		}
		throw new NumberFormatException("not a string");
	}

}
