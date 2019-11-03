package com.meng.config.javabeans;

import java.io.*;
import java.util.*;

public class DataPack {

	private byte[] data;
	private int writePointer=0;
	private final short headLength=18;
	private int readPointer;

	/*
	 数据包中所有数字都是long型，除了数据包头中的数字和标记字符串长度的数字
	 数据包中字符串的放置方式是先放置一个字符串在数据包中的字节数，数字为int，后面接着是字符串的数组，所有字符串都是utf-8
	 数据包结构 : | 数据包头 | 数据 |
	 数据包头 : | 包长度(4字节) | 头长度(2) | 数字"1"(2) | 时间戳或者任务标记(8) | 操作类型(2)|
	 数据中的结构见下面操作类型的注释

	 小律影→正邪  小律影发送 鬼人正邪接收
	 正邪→小律影  鬼人正邪发送 小律影接收
	 小律影↔正邪  都可以发送和接收
	 */
	public static final short _0notification=0;				//小律影↔正邪    |包头|字符串|   以下注释中省略"包头"说明
	public static final short _1verify=1;					//小律影→正邪    |qq号(setConnect中设置的qq号)|
	public static final short _2getLiveList=2;				//小律影→正邪    不需要body
	public static final short _3returnLiveList=3;			//正邪→小律影    |包头|json字符串|  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _4liveStart=4;				//正邪→小律影    |直播间号|主播称呼|
	public static final short _5liveStop=5;					//正邪→小律影    |直播间号|主播称呼|
	public static final short _6speakInLiveRoom=6;			//正邪→小律影    |直播间号|主播称呼|说话者称呼,如果配置文件中没有就是用户名|说话者BID|说话内容
	public static final short _7newVideo=7;					//正邪→小律影    |用户名|视频名|AV号
	public static final short _8newArtical=8;				//正邪→小律影    |用户名|专栏名|CV号
	public static final short _9getPersonInfoByName=9;		//小律影→正邪    |称呼|
	public static final short _10getPersonInfoByQQ=10;		//小律影→正邪    |qq号|
	public static final short _11getPersonInfoByBid=11;		//小律影→正邪    |BID|
	public static final short _12getPersonInfoByBiliLive=12;//小律影→正邪    |直播间号|
	public static final short _13returnPersonInfo=13;		//正邪→小律影    |json字符串|    例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _14coinsExchange=14;			//正邪→小律影    |幻币数量|
	public static final short _15groupBan=15; 				//小律影↔正邪    |群号|QQ号|时间(秒)
	public static final short _16groupKick=16;				//小律影↔正邪    |群号|QQ号|是否永久拒绝 0为否 1为是
	public static final short _17heartBeat=17;				//心跳，不需要body
	
	public static DataPack encode(short opCode, long timeStamp) {
		return new DataPack(opCode, timeStamp);
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}

	private DataPack(short opCode, long timeStamp) {
		data = new byte[1024];
		write(getBytes(data.length));
		write(getBytes(headLength));
		write(getBytes((short)1));
		write(getBytes(timeStamp));
		write(getBytes(opCode));
		//write(toEncode);
	}   

	private DataPack(byte[] pack) {
		readPointer = headLength;
		data = pack;
	} 

	public byte[] getData() {
		byte[] retData=new byte[readPointer];
		for (int i=0;i < readPointer;++i) {
			retData[i] = data[i];
		}
		return retData;
	}

	public int getLength() {
		return readInt(data, 0);
	}  

	public short getHeadLength() {
		return readShort(data, 4);
	}

	public long getTimeStamp() {
		return readLong(data, 6);
	}

	public short getVersion() {
		return readShort(data, 14);
	}

	public short getOpCode() {
		return readShort(data, 16);
	}

	public void write(long l) {
		write(getBytes(l));
	}

	public void write(String s) {
		try {
			write(s.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			write("UnsupportedEncodingException".getBytes());
		}
	}

	public long readNum() {
		long l= readLong(data, readPointer);
		readPointer += 8;
		return l;
	}

	public String readString() {
		int length=readInt(data, readPointer);
		readPointer += 4;
		String s=null;
		try {                
			s = new String(data, readPointer, length, "utf-8");
		} catch (UnsupportedEncodingException e) {}
		readPointer += length;
		return s;
	}

	private void write(byte[] bs) {
		if (data.length - readPointer - 1 < bs.length) {
			byte[] newData=new byte[(data.length << 1) + bs.length];
			for (int i=0;i < readPointer;++i) {
				newData[i] = data[i];
			}
			data = newData;
		}
		for (int i=0;i < bs.length;++i) {
			data[writePointer++] = bs[i];
		}
		readPointer += bs.length;
	}

	private byte[] getBytes(int i) {
		byte[] bs=new byte[4];
		bs[0] = (byte) ((i >> 0) & 0xff);
		bs[1] = (byte) ((i >> 8) & 0xff);
		bs[2] = (byte) ((i >> 16) & 0xff);
		bs[3] = (byte) ((i >> 24) & 0xff);
		return bs;	
	}

	private byte[] getBytes(long l) {
		byte[] bs=new byte[8];
		bs[0] = (byte) ((l >> 0) & 0xff);
		bs[1] = (byte) ((l >> 8) & 0xff);
		bs[2] = (byte) ((l >> 16) & 0xff);
		bs[3] = (byte) ((l >> 24) & 0xff);
		bs[4] = (byte) ((l >> 32) & 0xff);
		bs[5] = (byte) ((l >> 40) & 0xff);
		bs[6] = (byte) ((l >> 48) & 0xff);
		bs[7] = (byte) ((l >> 56) & 0xff);
		return bs;	
	}

	private byte[] getBytes(short s) {
		byte[] bs=new byte[2];
		bs[0] = (byte) ((s >> 0) & 0xff);
		bs[1] = (byte) ((s >> 8) & 0xff) ;
		return bs;	
	}

	private short readShort(byte[] data, int pos) {
        return (short) ((data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8);
	}

	private int readInt(byte[] data, int pos) {
        return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
	}

	private long readLong(byte[] data, int pos) {
        return ((data[pos] & 0xffL) << 0) | (data[pos + 1] & 0xffL) << 8 | (data[pos + 2] & 0xffL) << 16 | (data[pos + 3] & 0xffL) << 24 | (data[pos + 4] & 0xffL) << 32 | (data[pos + 5] & 0xffL) << 40 | (data[pos + 6] & 0xffL) << 48 | (data[pos + 7] & 0xffL) << 56;
	}
}
