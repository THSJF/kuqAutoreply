package com.meng.config.javabeans;

import java.io.*;

public class DataPack {

	private byte[] data;
	private int pos=0;
	private final short headLength=18;
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
        return 0L | ((data[pos] & 0xff) << 0) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24 | (data[pos + 4] & 0xff) << 32 | (data[pos + 5] & 0xff) << 40 | (data[pos + 6] & 0xff) << 48 | (data[pos + 7] & 0xff) << 56;
	}
}
