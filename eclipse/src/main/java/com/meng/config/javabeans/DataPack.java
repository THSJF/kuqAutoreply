package com.meng.config.javabeans;

import java.io.*;

public class DataPack {

	private byte[] data;
	private int pos=0;
	private final int headLength=10;
	public static DataPack encode(int opCode, byte[] data) {
		return new DataPack((short)opCode, data);
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

	public DataPack(short opCode, byte[] toEncode) {
		data = new byte[headLength + toEncode.length];
		write(getBytes(data.length));
		write(getBytes((short)10));
		write(getBytes((short)1));
		write(getBytes(opCode));
		write(toEncode);
	  }   

	public DataPack(byte[] pack) {
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

	public byte[] getBodyData() {
		byte [] bts=new byte[getLength() - headLength];
		for (int i=0;i < bts.length;++i) {
			bts[i] = data[i + 12];
		  }
		return bts;
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

	public short getOpCode() {
		return readShort(data, 8);
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
  }
