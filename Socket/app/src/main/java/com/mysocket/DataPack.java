package com.mysocket;
import android.os.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class DataPack {

	private byte[] data;
	public static final short headLength=26;
	private byte[] arrayData=null;
	public RitsukageBean ritsukageBean=null;
	private HashSet<Long> ritsuaheLongSet=null;
	private Gson gson;
	private int writePointer=0;

	/*
	 数据包中所有数字都是long型，除了数据包头中的数字和标记字符串长度的数字
	 数据包中字符串的放置方式是先放置一个字符串在数据包中的字节数，数字为int，后面接着是字符串的数组，所有字符串都是utf-8
	 数据包结构 : | 数据包头 | 数据部分 |
	 数据包头 : | 包长度(4字节) | 头长度(2) | 数字"1"(2) | 时间戳或者任务标记(8) | 发送目标,如果是给小律影发送的数据就是setConnect中设置的qq号(8) | 操作类型(2)|
	 数据为json字符串,key见下面操作类型的注释

	 小律影只需要处理"发送目标"是自己和-1的数据包

	 大部分情况下返回jsonObject
	 jsonObject中字符串第一个叫s1 第二个叫s2 以此类推
	 数字则是n1 n2.....
	 {s1="此生无悔绀珠传",s2="来世愿打星莲船",n1="9961"}
	 假如某指令需要n1参数,而收到的json中没有n1,则把该参数当做0处理即可
	 字符串则是视为空字符串

	 小律影向鬼人正邪发送查询类指令时，鬼人正邪返回的数据包中的时间戳会设置成和收到的数据包中时间戳相同
	 其实并不检查时间戳和实际时间,只是个任务标记

	 小律影→正邪  小律影发送 鬼人正邪接收
	 正邪→小律影  鬼人正邪发送 小律影接收 (其实是广播方式发送,所有连接的客户端都可以收到)
	 小律影↔正邪  都可以发送和接收

	 */

	//小律影↔正邪  普通通知,大部分情况下没什么用   s1:通知文本
	public static final short _0notification=0;
	//小律影→正邪  用于身份验证，暂未使用    n1:qq号(setConnect中设置的qq号)
	public static final short _1verify=1;
	//小律影→正邪  获取正在直播列表  不需要body
	public static final short _2getLiveList=2;
	//正邪→小律影  返回2的查询结果  json数组  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _3returnLiveList=3;
	//正邪→小律影  主播开始直播   n1:直播间号 s1:主播称呼
	public static final short _4liveStart=4;
	//正邪→小律影  主播停止直播   n1:直播间号 s1:主播称呼
	public static final short _5liveStop=5;
	//正邪→小律影  直播观看者在直播间发送的弹幕   n1:直播间号 s1:主播称呼 n2:说话者BID s2:说话者称呼,如果配置文件中没有就是用户名 s3:说话内容
	public static final short _6speakInLiveRoom=6;
	//正邪→小律影  up主发布新视频  s1:用户名 s2:视频名 n1:AV号
	public static final short _7newVideo=7;
	//正邪→小律影  up主发布新专栏  s1:用户名 s2:专栏名 n1:CV号
	public static final short _8newArtical=8;
	//小律影→正邪  从称呼获得人员信息(完全匹配方式查找)  s1:称呼
	public static final short _9getPersonInfoByName=9;
	//小律影→正邪  从qq获得人员信息(完全匹配方式查找)  n1:qq号
	public static final short _10getPersonInfoByQQ=10;
	//小律影→正邪  从bid获得人员信息(完全匹配方式查找)  n1:BID
	public static final short _11getPersonInfoByBid=11;
	//小律影→正邪  从直播间号获得人员信息(完全匹配方式查找)  n1:直播间号
	public static final short _12getPersonInfoByBiliLive=12;
	//正邪→小律影  返回 _9 _10 _11 _12的查询结果  返回结果例:{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]}
	public static final short _13returnPersonInfo=13;
	//正邪→小律影  给指定qq号添加幻币  n1:幻币数量 n2:目标qq号
	public static final short _14coinsAdd=14;
	//小律影↔正邪  qq群中禁言  n1:群号 n2:QQ号 n3:时间(秒)
	public static final short _15groupBan=15;
	//小律影↔正邪  踢出qq群  n1:群号 n2:QQ号 n3:是否永久拒绝 0为否 1为是
	public static final short _16groupKick=16;
	//小律影→正邪  心跳，不需要body 返回一个操作类型为0的通知
	public static final short _17heartBeat=17;
	//小律影→正邪  同在QQ中的"findInAll"指令  n1:qq号
	public static final short _18FindInAll=18;
	//正邪→小律影  返回18的结果   返回结果例[296376859,251059118]
	public static final short _19returnFind=19;
	//小律影→正邪  获得"精神支柱"表情包  n1:qq号
	public static final short _20pic=20;
	//正邪→小律影  返回_20生成的jpg文件 数据部分直接保存到磁盘即可
	public static final short _21returnPic=21;
	//小律影→正邪  获得"神触"表情包  n1:qq号
	public static final short _22pic2=22;
	//正邪→小律影  返回_22生成的jpg文件 数据部分直接保存到磁盘即可
	public static final short _23returnPic2=23;
	//小律影→正邪  获得鬼人正邪".jrrp"中的计算结果  n1:qq号
	public static final short _24MD5Random=24;
	//正邪→小律影  返回_24的结果 n1:计算结果   (0-10000的整数)
	public static final short _25returnMD5Random=25;
	//小律影→正邪  获得鬼人正邪".draw neta"中的计算结果  n1:qq号
	public static final short _26MD5neta=26;
	//正邪→小律影  返回_26的结果 s1:计算结果
	public static final short _27returnMD5neta=27;
	//小律影→正邪  获得鬼人正邪".draw music"中的计算结果  n1:qq号
	public static final short _28MD5music=28;
	//正邪→小律影  返回_28的结果 s1:计算结果
	public static final short _29returnMD5music=29;
	//小律影→正邪  获得鬼人正邪".draw grandma"中的计算结果  n1:qq号
	public static final short _30MD5grandma=30;
	//正邪→小律影  返回_30的结果 s1:计算结果
	public static final short _31returnMD5grandma=31;
	//小律影→正邪  获得鬼人正邪"。jrrp"中的计算结果  n1:qq号
	public static final short _32MD5overSpell=32;
	//正邪→小律影  返回_32的结果 s1:计算结果
	public static final short _33returnMD5overSpell=33;



	public static DataPack encode(short opCode, long timeStamp) {
		return new DataPack(opCode, timeStamp);
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}

	private DataPack(short opCode, long timeStamp) {
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
		data = new byte[headLength];
		ritsukageBean = new RitsukageBean();
		write(getBytes(data.length));
		write(getBytes(headLength));
		write(getBytes((short)1));
		write(getBytes(timeStamp));
		write(getBytes(2057480282L));
		write(getBytes(opCode));
	}   

	private DataPack(byte[] pack) {
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
		data = pack;
		String s=new String(pack, headLength, getLength() - headLength);
		System.out.println(s);
		switch (getOpCode()) {
			case DataPack._19returnFind:
				Type ritsucageLongSetType = new TypeToken<HashSet<Long>>() {
				}.getType();
				ritsuaheLongSet = gson.fromJson(s, ritsucageLongSetType);
				break;
			case DataPack._21returnPic:
				//saveFile(System.currentTimeMillis() + "", data);
				break;
			default:
				ritsukageBean = gson.fromJson(s, RitsukageBean.class);
				
				break;
		}
	} 

	public byte[] getData() {
		byte[] retData=null;
		byte[] byteArray=null;
		if (arrayData != null) {
			byteArray = arrayData;
			retData = new byte[headLength + byteArray.length];	
		} else if (ritsuaheLongSet != null) {
			try {
				byteArray = gson.toJson(ritsuaheLongSet).getBytes("utf-8");
				retData = new byte[headLength + byteArray.length];
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else if (ritsukageBean != null) {
			try {
				byteArray = gson.toJson(ritsukageBean).replaceAll(",\"s[1-9]\":\"\"","").replaceAll(",\"n[1-9]\":0","").getBytes("utf-8");
				retData = new byte[headLength + byteArray.length];
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			try {		
				byteArray = "".getBytes("utf-8");
				retData = new byte[headLength + byteArray.length];
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		for (int i=0;i < headLength;++i) {
			retData[i] = data[i];
		}
		for (int i=0;i < byteArray.length;++i) {
			retData[i + headLength] = byteArray[i];
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

	public void writeLongSet(HashSet<Long> hs) {
		ritsuaheLongSet = hs;
	}
	public void write(int argNum, long l) {
		switch (argNum) {
			case 1:
				ritsukageBean.n1 = l;
				break;
			case 2:
				ritsukageBean.n2 = l;
				break;
			case 3:
				ritsukageBean.n3 = l;
				break;
		}
	}

	public void write(int argNum, String s) {
		switch (argNum) {
			case 1:
				ritsukageBean.s1 = s;
				break;
			case 2:
				ritsukageBean.s2 = s;
				break;
			case 3:
				ritsukageBean.s3 = s;
		}
	}

	public long readNum(int argNum) {
		switch (argNum) {
			case 1:
				return ritsukageBean.n1;
			case 2:
				return ritsukageBean.n2;	
			case 3:
				return ritsukageBean.n3;
		}
		return 0;
	}

	public void writeData(byte[] bs) {
		arrayData = bs;
	}

	public String readString(int argNum) {
		switch (argNum) {
			case 1:
				return ritsukageBean.s1;
			case 2:
				return ritsukageBean.s2;
		}
		return null;
	}

	private void write(byte[] bs) {
		for (int i=0;i < bs.length;++i) {
			data[writePointer++] = bs[i];
		}
	}

	private void saveFile(String name, byte[] bytes) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/1/" + name + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, headLength, bytes.length - headLength);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
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
