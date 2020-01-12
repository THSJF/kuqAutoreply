package com.addques.sanae;

import com.addques.*;
import java.io.*;
import java.util.*;

public class SanaeDataPack {

	public ArrayList<Byte> data=new ArrayList<>();
	public byte[] dataArray;
	public static final short headLength=28;
	public int dataPointer=0;

	public static final byte typeByte=0;
	public static final byte typeShort=1;
	public static final byte typeInt=2;
	public static final byte typeLong=3;
	public static final byte typeFloat=4;
	public static final byte typeDouble=5;
	public static final byte typeString=6;
	public static final byte typeBoolean=7;
	public static final byte typeFile=8;

	public static final int opNotification=0;//通知  string
	public static final int opAddQuestion=10;//int flag,string ques,int ansCount,string... ans,string reason
	public static final int opAllQuestion=11;
	public static final int opSetQuestion=12;//int flag,string ques,int ansCount,int trueAns,string... ans,string reason
	public static final int opHeartBeat=24;//心跳
	public static final int opQuestionPic=35;//int id     file
	
	public static SanaeDataPack encode(int opCode) {
		return new SanaeDataPack(opCode, System.currentTimeMillis());
	}

	public static SanaeDataPack encode(SanaeDataPack dataPack) {
		return new SanaeDataPack(dataPack);
	}

	public static SanaeDataPack encode(int opCode, SanaeDataPack dataPack) {
		return new SanaeDataPack(dataPack);
	}

	public static SanaeDataPack decode(byte[] bytes) {
		return new SanaeDataPack(bytes);
	}

	private SanaeDataPack(int opCode, long timeStamp) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(BitConverter.getBytes(0));
		writeByteDataIntoArray(BitConverter.getBytes(headLength));
		writeByteDataIntoArray(BitConverter.getBytes((short)5));
		writeByteDataIntoArray(BitConverter.getBytes(timeStamp));
		writeByteDataIntoArray(BitConverter.getBytes(0L));
		writeByteDataIntoArray(BitConverter.getBytes(opCode));
	}   

	private SanaeDataPack(SanaeDataPack dataPack) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(BitConverter.getBytes(0));
		writeByteDataIntoArray(BitConverter.getBytes(headLength));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getVersion()));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getTimeStamp()));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getTarget()));
		writeByteDataIntoArray(BitConverter.getBytes(dataPack.getOpCode()));
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

	private SanaeDataPack writeByteDataIntoArray(byte... bs) {
		for (byte b:bs) {
			data.add(b);
			++dataPointer;
		}
		return this;
	}

	public SanaeDataPack write(byte b) {
		writeByteDataIntoArray(typeByte);
		writeByteDataIntoArray(b);
		return this;
	}

	public SanaeDataPack write(short s) {
		writeByteDataIntoArray(typeShort);
		writeByteDataIntoArray(BitConverter.getBytes(s));
		return this;
	}

	public SanaeDataPack write(int i) {
		writeByteDataIntoArray(typeInt);
		writeByteDataIntoArray(BitConverter.getBytes(i));
		return this;
	}

	public SanaeDataPack write(long l) {
		writeByteDataIntoArray(typeLong);
		writeByteDataIntoArray(BitConverter.getBytes(l));
		return this;
	}

	public SanaeDataPack write(float f) {
		writeByteDataIntoArray(typeFloat);
		writeByteDataIntoArray(BitConverter.getBytes(f));
		return this;
	}

	public SanaeDataPack write(double d) {
		writeByteDataIntoArray(typeDouble);
		writeByteDataIntoArray(BitConverter.getBytes(d));
		return this;
	}

	public SanaeDataPack write(String s) {
		writeByteDataIntoArray(typeString);
		byte[] stringBytes = BitConverter.getBytes(s);
		write(stringBytes.length);
		writeByteDataIntoArray(stringBytes);
		return this;
	}

	public SanaeDataPack write(boolean b) {
		writeByteDataIntoArray(typeBoolean);
		writeByteDataIntoArray(b ?(byte)1: (byte)0);
		return this;
	}

	public SanaeDataPack write(File file) {
		try {
			FileInputStream fin=new FileInputStream(file);
			byte[] bs=new byte[(int)file.length()];
			fin.read(bs, 0, bs.length);
			writeByteDataIntoArray(typeFile);
			write((int)file.length());
			writeByteDataIntoArray(bs);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		return this;
	}

	public File readFile(String folderPath, String name) {
		if (dataArray[dataPointer++] == typeFile) {
			int fileLen=readInt();
			File recFile=new File(folderPath + "/" + name);
			try {
				FileOutputStream fos=new FileOutputStream(recFile);
				fos.write(dataArray, dataPointer, fileLen);
			} catch (Exception e) {
				recFile.delete();
				recFile = null;
			}
			dataPointer += fileLen;
			return recFile;
		}
		throw new RuntimeException("not a file");
	}

	public byte readByte() {
		if (dataArray[dataPointer++] == typeByte) {
			return dataArray[dataPointer++];
		}
		throw new RuntimeException("not a byte number");
	}

	public short readShort() {
		if (dataArray[dataPointer++] == typeShort) {
			short s = BitConverter.toShort(dataArray, dataPointer);
			dataPointer += 2;
			return s;
		}
		throw new RuntimeException("not a short number");
	}

	public int readInt() {
		if (dataArray[dataPointer++] == typeInt) {
			int i= BitConverter.toInt(dataArray, dataPointer);
			dataPointer += 4;
			return i;
		}
		throw new RuntimeException("not a int number");
	}

	public long readLong() {
		if (dataArray[dataPointer++] == typeLong) {
			long l= BitConverter.toLong(dataArray, dataPointer);
			dataPointer += 8;
			return l;
		}
		throw new RuntimeException("not a long number");
	}

	public float readFloat() {
		if (dataArray[dataPointer++] == typeFloat) {
			float f = BitConverter.toFloat(dataArray, dataPointer);
			dataPointer += 4;
			return f;
		}
		throw new RuntimeException("not a float number");
	}

	public double readDouble() {
		if (dataArray[dataPointer++] == typeDouble) {
			double d = BitConverter.toDouble(dataArray, dataPointer);
			dataPointer += 8;
			return d;
		}
		throw new RuntimeException("not a double number");
	}

	public String readString() {
		try {
			if (dataArray[dataPointer++] == typeString) {
				int len = readInt();
				String s = BitConverter.toString(dataArray, dataPointer, len);
				dataPointer += len;
				return s;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		return null;
	}

	public boolean readBoolean() {
		if (dataArray[dataPointer++] == typeBoolean) {
			return dataArray[dataPointer++] == 1;
		}
		throw new RuntimeException("not a boolean value");
	}

	public boolean hasNext() {
		return dataPointer != dataArray.length;
	}
}
