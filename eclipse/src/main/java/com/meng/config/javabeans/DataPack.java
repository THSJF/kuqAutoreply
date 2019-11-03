package com.meng.config.javabeans;

import java.io.*;

public class DataPack {

	private byte[] data;
	private int pos=0;
	private final short headLength=18;
	private int dataPointer;

	/*数据包中所有数字都是long型，除了数据包头中的数字和标记字符串长度的数字*/
	/*数据包中字符串的放置方式是先放置一个字符串在数据包中的字节数，数字为int，后面接着是字符串的数组，所有字符串都是utf-8
	/*数据包结构 : | 数据包头 | 数据 |
	/*数据包头 : | 包长度(4字节) | 头长度(2) | 数字"1"(2) | 时间戳或者任务标记(8) | 操作类型(2)| */
	/*数据中的结构见下面操作类型的注释*/

	public static final short _0notification=0; //|包头|字符串|   以下注释中省略"包头"说明
	public static final short _1verify=1; //|qq号(setConnect中设置的qq号)|
	public static final short _2getLiveList=2; //不需要body
	public static final short _3returnLiveList=3;//包头|json字符串|  例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _4liveStart=4;// |直播间号|主播称呼|
	public static final short _5liveStop=5;// |直播间号|主播称呼|
	public static final short _6speakInLiveRoom=6;// |直播间号|主播称呼|说话者称呼,如果配置文件中没有就是用户名|说话者BID|说话内容
	public static final short _7newVideo=7;// |用户名|视频名|AV号
	public static final short _8newArtical=8;// |用户名|专栏名|CV号
	public static final short _9getPersonInfoByName=9;// |称呼|
	public static final short _10getPersonInfoByQQ=10;// |qq号|
	public static final short _11getPersonInfoByBid=11;// |BID|
	public static final short _12getPersonInfoByBiliLive=12;// |直播间号|
	public static final short _13returnPersonInfo=13;//|json字符串|    例:[{"name":"闲者","qq":"877247145","bid":12007285,"bliveRoom":1954885,"tipIn":[],"tip":[true,true,false]},{"name":"懒瘦","qq":"496276037","bid":15272850,"bliveRoom":3144622,"tipIn":[],"tip":[true,true,false]}]
	public static final short _14coinsExchange=14;// |幻币数量|
	public static final short _15groupBan=15; // |群号|QQ号|时间(秒)
	public static final short _16groupKick=16;// |群号|QQ号|是否永久拒绝 0为否 1为是

	public static DataPack encode(int opCode, byte[] data) {
		return new DataPack((short)opCode, System.currentTimeMillis(), data);
	}

	public static DataPack encode(int opCode, long timeStamp, byte[] data) {
		return new DataPack((short)opCode, timeStamp, data);
	}

	public static DataPack encode(int opCode, String str) {
		try {
			return encode(opCode, str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static DataPack decode(byte[] bytes) {
		return new DataPack(bytes);
	}
	/*opCode:
	 -1 : message
	 0 to 26 : configV3.json
	 27 : onConnect
	 28 : bilibili live
	 29 : new video and artical
	 30 : bilibili danmaku	
	 31 : hina bilibili live room
	 32 : heart beat
	 */  
	private DataPack(short opCode, long timeStamp, byte[] toEncode) {
		data = new byte[headLength + toEncode.length];
		write(getBytes(data.length));
		write(getBytes(headLength));
		write(getBytes((short)1));
		write(getBytes(timeStamp));
		write(getBytes(opCode));
		write(toEncode);
	}   

	private DataPack(byte[] pack) {
		dataPointer = headLength;
		data = pack;
	} 

	public String getString() {
		try {
			return new String(data, headLength, getLength() - headLength, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public byte[] getData() {
		return data;
	}

	/*public byte[] getBodyData() {
	 byte [] bts=new byte[getLength() - headLength];
	 for (int i=0;i < bts.length;++i) {
	 bts[i] = data[i + 12];
	 }
	 return bts;
	 }
	 */  

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

	private void write(long l) {
		write(getBytes(l));
	}

	private void write(String s) {
		try {
			write(s.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			write("UnsupportedEncodingException".getBytes());
		}
	}

	private long readNum() {
		long l= readLong(data, dataPointer);
		dataPointer += 8;
		return l;
	}

	private String readString() {
		int length=readInt(data, dataPointer);
		dataPointer += 4;
		String s=null;
		try {                
			s = new String(data, dataPointer, length, "utf-8");
		} catch (UnsupportedEncodingException e) {}
		dataPointer += length;
		return s;
	}

	private void write(byte[] bs) {
		for (int i=0;i < bs.length;++i) {
			data[pos++] = bs[i];
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

	public short readShort(byte[] data, int pos) {
        return (short) ((data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8);
	}

	public int readInt(byte[] data, int pos) {
        return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
	}

	public long readLong(byte[] data, int pos) {
        return ((data[pos] & 0xffL) << 0) | (data[pos + 1] & 0xffL) << 8 | (data[pos + 2] & 0xffL) << 16 | (data[pos + 3] & 0xffL) << 24 | (data[pos + 4] & 0xffL) << 32 | (data[pos + 5] & 0xffL) << 40 | (data[pos + 6] & 0xffL) << 48 | (data[pos + 7] & 0xffL) << 56;
	}
}
