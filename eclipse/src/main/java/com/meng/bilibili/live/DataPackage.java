package com.meng.bilibili.live;
import java.io.*;

public class DataPackage {
	public byte[] data;
	private int pos=0;

	public int length;
	public short headLen;
    public short version;
	public int op;
	public int seq;
	public String body="";

	public DataPackage(int opCode, String jsonStr) {
		byte[] jsonByte=jsonStr.getBytes();
		data = new byte[16 + jsonByte.length];
		write(getBytes(length = data.length));
		write(getBytes(headLen = (short)16));
		write(getBytes(version = (short)1));
		write(getBytes(op = opCode));
		write(getBytes(seq = 1));
		write(jsonByte);
		body = jsonStr;
	  }   

	public DataPackage(byte[] pack) {
		data = pack;
		length = readInt();
		headLen = readShort();
		version = readShort();
		op = readInt();
		seq = readInt();
		try {
			body = new String(data, 16, length - 16, "utf-8");
		  } catch (UnsupportedEncodingException e) {}
	  }

	private void write(byte[] bs) {
		for (int i=0;i < bs.length;++i) {
			data[pos++] = bs[i];
		  }
	  }

	private byte[] getBytes(int i) {
		byte[] bs=new byte[4];
		bs[0] = (byte) ((i >> 24) & 0xff);
		bs[1] = (byte) ((i >> 16) & 0xff);
		bs[2] = (byte) ((i >> 8) & 0xff);
		bs[3] = (byte) (i & 0xff);
		return bs;	
	  }

	private byte[] getBytes(short s) {
		byte[] bs=new byte[2];
		bs[0] = (byte) ((s >> 8) & 0xff);
		bs[1] = (byte) (s & 0xff) ;
		return bs;	
	  }

	public short readShort() {
        return (short) ((data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0);
	  }

	public int readInt() {
        return (data[pos++] & 0xff) << 24 | (data[pos++] & 0xff) << 16 | (data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0;
	  }

  }

