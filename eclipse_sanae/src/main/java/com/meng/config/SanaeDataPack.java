package com.meng.config;

import com.meng.tools.*;
import java.util.*;

public class SanaeDataPack {

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
	public final byte typeBoolean=7;

	public static final int _0notification=0;//通知  string
	public static final int _1getConfig=1;  //获取配置文件
	public static final int _2retConfig=2;//接收配置文件  string(json)
	public static final int _3getOverSpell=3;//获取疮痍符卡 long(qq)
	public static final int _4retOverSpell=4;//接收疮痍符卡 string
	public static final int _5getOverPersent=5;//获取疮痍进度 long(qq)
	public static final int _6retOverPersent=6;//接收疮痍进度 int(0-10000)
	public static final int _7getGrandma=7;//获取draw grandma long(qq)
	public static final int _8retGrandma=8;//接收grandma string
	public static final int _9getMusicName=9;//获取音乐名 long(qq)
	public static final int _10retMusicName=10;//接收音乐名 string
	public static final int _11getGotSpells=11;//获取符卡列表 long(qq)
	public static final int _12retGotSpells=12;//接收符卡列表 string(json)
	public static final int _13getNeta=13;//获取neta long(qq)
	public static final int _14retNeta=14;//接收neta string
	public static final int _15incSpeak=15;//增加发言次数  long(group) long(qq)
	public static final int _16incPic=16;//增加图片次数 long (qq) 
	public static final int _17incBilibili=17;//增加哔哩哔哩链接次数  long(group) long(qq)
	public static final int _18incRepeat=18;//增加复读次数 long (qq)
	public static final int _19incRepeatStart=19;//增加带领复读  long(qq)
	public static final int _20incRepeatBreak=20;//增加打断复读  long(group) long(qq)
	public static final int _21incBan=21;//增加被禁言次数  long(group) long(qq)
	public static final int _22decTime=22;//减少时间  long(group) long(qq)
	public static final int _23grass=23;//增加种草次数  long(group) long(qq)
	public static final int _24heartBeat=24;//心跳
	public static final int _25setNick=25;//设置nickname long(qq)
	public static final int _26addBlack=26;//添加黑名单
	public static final int _27incMengEr=27;//萌二发言
	public static final int _28getSeqContent=28;//获得接龙内容
	public static final int _29retSeqContent=29;//返回接龙内容
	public static final int _30sendMsg=30;//向指定目标发送消息long group long qq string msg
	public static final int _31getLiveList=31;//获取直播列表
	public static final int _32retLiveList=32;//返回直播列表 string(name) long(blid)
	public static final int _33liveStart=33;//开始直播 string(name) long(blid)
	public static final int _34liveStop=34;//停止直播 string(name) long(blid)
	public static final int _35speakInliveRoom=35;//直播间说话 string(主播称呼) long(blid) string(说话者称呼) long(说话者bid)
	public static final int _36newVideo=36;//新视频 string(up) string(vname) long(aid)
	public static final int _37newArtical=37;
	public static final int _38groupBan=38;
	public static final int _39groupKick=39;

	/*

	 */


	public static SanaeDataPack encode(int opCode) {
		return new SanaeDataPack(opCode, System.currentTimeMillis());
	}

	public static SanaeDataPack encode(int opCode, SanaeDataPack dataPack) {
		return new SanaeDataPack(opCode, dataPack);
	}

	public static SanaeDataPack decode(byte[] bytes) {
		return new SanaeDataPack(bytes);
	}

	private SanaeDataPack(int opCode, long timeStamp) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(Tools.BitConverter.getBytes(0));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(headLength));
		writeByteDataIntoArray(Tools.BitConverter.getBytes((short)1));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(timeStamp));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(0L));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(opCode));
	}   

	private SanaeDataPack(int opCode, SanaeDataPack dataPack) {
		//length(4) headLength(2) version(2) time(8) target/from(8) opCode(4)
		writeByteDataIntoArray(Tools.BitConverter.getBytes(0));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(headLength));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(dataPack.getVersion()));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(dataPack.getTimeStamp()));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(dataPack.getTarget()));
		writeByteDataIntoArray(Tools.BitConverter.getBytes(opCode));
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
		byte[] len=Tools.BitConverter.getBytes(retData.length);
		retData[0] = len[0];
		retData[1] = len[1];
		retData[2] = len[2];
		retData[3] = len[3];
		dataArray = retData;
		return retData;
	}

	public int getLength() {
		return Tools.BitConverter.toInt(dataArray, 0);
	}  

	public short getHeadLength() {
		return Tools.BitConverter.toShort(dataArray, 4);
	}

	public short getVersion() {
		return Tools.BitConverter.toShort(dataArray, 6);
	}

	public long getTimeStamp() {
		return Tools.BitConverter.toLong(dataArray, 8);
	}

	public long getTarget() {
		return Tools.BitConverter.toLong(dataArray, 16);
	}

	public int getOpCode() {
		return Tools.BitConverter.toShort(dataArray, 24);
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
		writeByteDataIntoArray(Tools.BitConverter.getBytes(s));
		return this;
	}

	public SanaeDataPack write(int i) {
		writeByteDataIntoArray(typeInt);
		writeByteDataIntoArray(Tools.BitConverter.getBytes(i));
		return this;
	}

	public SanaeDataPack write(long l) {
		writeByteDataIntoArray(typeLong);
		writeByteDataIntoArray(Tools.BitConverter.getBytes(l));
		return this;
	}

	public SanaeDataPack write(float f) {
		writeByteDataIntoArray(typeFloat);
		writeByteDataIntoArray(Tools.BitConverter.getBytes(f));
		return this;
	}

	public SanaeDataPack write(double d) {
		writeByteDataIntoArray(typeDouble);
		writeByteDataIntoArray(Tools.BitConverter.getBytes(d));
		return this;
	}

	public SanaeDataPack write(String s) {
		writeByteDataIntoArray(typeString);
		byte[] stringBytes = Tools.BitConverter.getBytes(s);
		write(stringBytes.length);
		writeByteDataIntoArray(stringBytes);
		return this;
	}

	public SanaeDataPack write(boolean b) {
		writeByteDataIntoArray(typeBoolean);
		writeByteDataIntoArray(b ?(byte)1: (byte)0);
		return this;
	}

	public byte readByte() {
		if (dataArray[dataPointer++] == typeByte) {
			return dataArray[dataPointer++];
		}
		throw new NumberFormatException("not a byte number");
	}

	public short readShort() {
		if (dataArray[dataPointer++] == typeShort) {
			short s = Tools.BitConverter.toShort(dataArray, dataPointer);
			dataPointer += 2;
			return s;
		}
		throw new NumberFormatException("not a short number");
	}

	public int readInt() {
		if (dataArray[dataPointer++] == typeInt) {
			int i= Tools.BitConverter.toInt(dataArray, dataPointer);
			dataPointer += 4;
			return i;
		}
		throw new NumberFormatException("not a int number");
	}

	public long readLong() {
		if (dataArray[dataPointer++] == typeLong) {
			long l= Tools.BitConverter.toLong(dataArray, dataPointer);
			dataPointer += 8;
			return l;
		}
		throw new NumberFormatException("not a long number");
	}

	public float readFloat() {
		if (dataArray[dataPointer++] == typeFloat) {
			float f = Tools.BitConverter.toFloat(dataArray, dataPointer);
			dataPointer += 4;
			return f;
		}
		throw new NumberFormatException("not a float number");
	}

	public double readDouble() {
		if (dataArray[dataPointer++] == typeDouble) {
			double d = Tools.BitConverter.toDouble(dataArray, dataPointer);
			dataPointer += 8;
			return d;
		}
		throw new NumberFormatException("not a double number");
	}

	public String readString() {
		try {
			if (dataArray[dataPointer++] == typeString) {
				int len = readInt();
				String s = Tools.BitConverter.toString(dataArray, dataPointer, len);
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
		throw new NumberFormatException("not a boolean value");
	}

	public boolean hasNext() {
		return dataPointer != dataArray.length;
	}
}
