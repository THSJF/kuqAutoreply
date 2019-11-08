package com.meng.config.javabeans;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.meng.*;

public class DataPack {

	private byte[] data;
	private final short headLength=26;
	private RitsukageBean ritsukageBean=null;
	private HashSet<PersonInfo> ritsukageSet=null;
	private HashSet<Long> ritsuaheLongSet=null;
	private PersonInfo ritsukagePersonInfo=null;
	private Gson gson;
	private int writePointer=0;

	/*
	 数据包中所有数字都是long型，除了数据包头中的数字和标记字符串长度的数字
	 数据包中字符串的放置方式是先放置一个字符串在数据包中的字节数，数字为int，后面接着是字符串的数组，所有字符串都是utf-8
	 数据包结构 : | 数据包头 | 数据 |
	 数据包头 : | 包长度(4字节) | 头长度(2) | 数字"1"(2) | 时间戳或者任务标记(8) | 发送目标,如果是给小律影发送的数据就是setConnect中设置的qq号(8) | 操作类型(2)|
	 数据为json字符串,key见下面操作类型的注释

	 小律影→正邪  小律影发送 鬼人正邪接收
	 正邪→小律影  鬼人正邪发送 小律影接收
	 小律影↔正邪  都可以发送和接收

	 大部分情况下返回jsonObject
	 jsonObject中字符串第一个叫s1 第二个叫s2 以此类推
	 数字则是n1 n2.....
	 {s1="此生无悔绀珠传",s2="来世愿打星莲船",n1=9961}

	 小律影向鬼人正邪发送查询类指令时，鬼人正邪返回的数据包中的时间戳会设置成和收到的数据包中时间戳相同
	 其实并不检查时间戳和实际时间,只是个任务标记

	 */

	public static final short _0notification=0;				//小律影↔正邪    s1:通知文本
	public static final short _1verify=1;					//小律影→正邪    n1:qq号(setConnect中设置的qq号)
	public static final short _2getLiveList=2;				//小律影→正邪    不需要body
	public static final short _3returnLiveList=3;			//正邪→小律影    json数组  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _4liveStart=4;				//正邪→小律影    n1:直播间号 s1:主播称呼
	public static final short _5liveStop=5;					//正邪→小律影    n1:直播间号 s1:主播称呼
	public static final short _6speakInLiveRoom=6;			//正邪→小律影    n1:直播间号 s1:主播称呼 n2:说话者BID s2:说话者称呼,如果配置文件中没有就是用户名 s3:说话内容
	public static final short _7newVideo=7;					//正邪→小律影    s1:用户名 s2:视频名 n1:AV号
	public static final short _8newArtical=8;				//正邪→小律影    s1:用户名 s2:专栏名 n1:CV号
	public static final short _9getPersonInfoByName=9;		//小律影→正邪    s1:称呼
	public static final short _10getPersonInfoByQQ=10;		//小律影→正邪    n1:qq号
	public static final short _11getPersonInfoByBid=11;		//小律影→正邪    n1:BID
	public static final short _12getPersonInfoByBiliLive=12;//小律影→正邪    n1:直播间号
	public static final short _13returnPersonInfo=13;		//正邪→小律影    json数组  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _14coinsAdd=14;				//正邪→小律影    n1:幻币数量 n2:目标qq号
	public static final short _15groupBan=15; 				//小律影↔正邪    n1:群号 n2:QQ号 n3:时间(秒)
	public static final short _16groupKick=16;				//小律影↔正邪    n1:群号 n2:QQ号 n3:是否永久拒绝 0为否 1为是
	public static final short _17heartBeat=17;				//小律影→正邪    心跳，不需要body
	public static final short _18FindInAll=18;				//小律影→正邪    n1:qq号
	public static final short _19returnFind=19;				//正邪→小律影    返回18的结果(群号json数组)  例[296376859,251059118]

	public static DataPack encode(short opCode, long timeStamp) {
		return new DataPack(opCode, timeStamp);
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}

	private DataPack(short opCode, long timeStamp) {
		gson = Autoreply.gson;
		data = new byte[headLength];
		ritsukageBean = new RitsukageBean();
		write(getBytes(data.length));
		write(getBytes(headLength));
		write(getBytes((short)1));
		write(getBytes(timeStamp));
		write(getBytes(Autoreply.instence.configManager.configJavaBean.ogg));
		write(getBytes(opCode));
	}   

	private DataPack(byte[] pack) {
		gson = Autoreply.gson;
		data = pack;
		String s=new String(pack, headLength, getLength() - headLength);
		System.out.println(s);
		ritsukageBean = gson.fromJson(s, RitsukageBean.class);
	} 

	public byte[] getData() {
		byte[] retData=null;
		byte[] bs=null;
		if (ritsukageSet != null) {
			retData = new byte[headLength + gson.toJson(ritsukageSet).length()];	
			try {
				bs = gson.toJson(ritsukageSet).getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else if (ritsukagePersonInfo != null) {	
			retData = new byte[headLength + gson.toJson(ritsukagePersonInfo).length()];
			try {
				bs = gson.toJson(ritsukagePersonInfo).getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else if (ritsuaheLongSet != null) {
			retData = new byte[headLength + gson.toJson(ritsuaheLongSet).length()];
			try {
				bs = gson.toJson(ritsuaheLongSet).getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			retData = new byte[headLength + gson.toJson(ritsukageBean).length()];
			try {
				bs = gson.toJson(ritsukageBean).getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		for (int i=0;i < headLength;++i) {
			retData[i] = data[i];
		}
		for (int i=0;i < bs.length;++i) {
			retData[i + headLength] = bs[i];
		}
		byte[] len=getBytes(retData.length);
		retData[0] = len[0];
		retData[1] = len[1];
		retData[2] = len[2];
		retData[3] = len[3];
		return retData;
	}

	public int getLength() {
		return readInt(data, 0);
	}  

	public short getHeadLength() {
		return readShort(data, 4);
	}

	public short getVersion() {
		return readShort(data, 6);
	}

	public long getTimeStamp() {
		return readLong(data, 8);
	}

	public long getTarget() {
		return readLong(data, 16);
	}


	public short getOpCode() {
		return readShort(data, 24);
	}

	public void write(PersonInfo pi) {
		ritsukagePersonInfo = pi;
	}

	public void write(HashSet<PersonInfo> hs) {
		ritsukageSet = hs;
	}

	public void write(HashSet<Long> hs) {
		ritsuaheLongSet = hs;
	}

	public void write1(long l) {
		ritsukageBean.n1 = l;
	}

	public void write2(long l) {
		ritsukageBean.n2 = l;
	}

	public void write3(long l) {
		ritsukageBean.n3 = l;
	}

	public void write1(String s) {
		ritsukageBean.s1 = s;
	}

	public void write2(String s) {
		ritsukageBean.s2 = s;
	}

	public long readNum1() {
		return ritsukageBean.n1;
	}

	public long readNum2() {
		return ritsukageBean.n2;
	}

	public long readNum3() {
		return ritsukageBean.n3;
	}

	public String readString1() {
		return ritsukageBean.s1;
	}

	public String readString2() {
		return ritsukageBean.s2;
	}

	private void write(byte[] bs) {
		for (int i=0;i < bs.length;++i) {
			data[writePointer++] = bs[i];
		}
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
