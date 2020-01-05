package com.meng.tools;

import com.meng.*;
import com.meng.gameData.TouHou.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.text.*;
import java.util.*;
import org.jsoup.*;
import org.meowy.cqp.jcq.entity.*;

public class Tools {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static class FileTool {
		public static String readString(String fileName) {
			return readString(new File(fileName));
		}
		public static String readString(File f) {
			String s = "{}";
			try {      
				if (!f.exists()) {
					f.createNewFile();
				}
				long filelength = f.length();
				byte[] filecontent = new byte[(int) filelength];
				FileInputStream in = new FileInputStream(f);
				in.read(filecontent);
				in.close();
				s = new String(filecontent, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return s;
		}
	}

	public static class Hash {
		public static String toMD5(String str) {
			try {
				byte[] strTemp = str.getBytes();
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				mdTemp.update(strTemp);
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}
		public static String toMD5(File file) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				return toMD5(inputStream);
			} catch (Exception e) {
				return null;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public static String toMD5(InputStream inputStream) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				byte[] buffer = new byte[1024];
				int numRead = 0;
				while ((numRead = inputStream.read(buffer)) > 0) {
					mdTemp.update(buffer, 0, numRead);
				}
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}
		private static String toHexString(byte[] md) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
			int j = md.length;
			char str[] = new char[j * 2];
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
				str[i * 2 + 1] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
	}

	public static class Network {
		public static Map<String, String> cookieToMap(String value) {
			Map<String, String> map = new HashMap<>();
			String[] values = value.split("; ");
			for (String val : values) {
				String[] vals = val.split("=");
				if (vals.length == 2) {
					map.put(vals[0], vals[1]);
				} else if (vals.length == 1) {
					map.put(vals[0], "");
				}
			}
			return map;
		}
		public static String getRealUrl(String surl) throws Exception {
			URL url = new URL(surl);
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String nurl = conn.getURL().toString();
			System.out.println("realUrl" + nurl);
			in.close();
			return nurl;
		}
		public static String getSourceCode(String url) {
			return getSourceCode(url, null);
		}
		public static String getSourceCode(String url, String cookie) {
			Connection.Response response = null;
			Connection connection;
			try {
				connection = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET);
				connection.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
				if (cookie != null) {
					connection.cookies(cookieToMap(cookie));
				}
				response = connection.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (response != null) {
				return response.body();
			} else {
				return null;
			}
		}
	}

	public static class CQ {
		public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
			if (Autoreply.ins.getCQCode().getAt(msg) == Autoreply.ins.getCoolQ().getLoginQQ()) {      
				Autoreply.sendMessage(fromGroup, 0, msg.replace("[CQ:at,qq=" + Autoreply.ins.getCoolQ().getLoginQQ() + "]", "[CQ:at,qq=" + fromQQ + "]"));
				return true;
			}
			return false;
		}
		public static void findQQInAllGroup(long fromGroup, long fromQQ, String msg) {
			long findqq;
			try {
				findqq = Long.parseLong(msg.substring(10));
			} catch (Exception e) {
				findqq = Autoreply.ins.getCQCode().getAt(msg);
			}
			if (findqq <= 0) {
				Autoreply.sendMessage(fromGroup, fromQQ, "QQ账号错误");
				return;
			}
			Autoreply.sendMessage(fromGroup, fromQQ, "running");
			HashSet<Group> hashSet = findQQInAllGroup(findqq);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(findqq).append("在这些群中出现");
			for (Group l : hashSet) {
				stringBuilder.append("\n").append(l.getId()).append(l.getName());
			}
			Autoreply.sendMessage(fromGroup, fromQQ, stringBuilder.toString());
		}
		public static HashSet<Group> findQQInAllGroup(long findQQ) {
			List<Group> groups = Autoreply.ins.getCoolQ().getGroupList();
			HashSet<Group> hashSet = new HashSet<>();
			for (Group group : groups) {
				if (group.getId() == 959615179L || group.getId() == 666247478L) {
					continue;
				}
				List<Member> members = Autoreply.ins.getCoolQ().getGroupMemberList(group.getId());
				for (Member member : members) {
					if (member.getQQId() == findQQ) {
						hashSet.add(group);
						break;
					}
				}
			}
			return hashSet;
		}
		public static boolean isAtme(String msg) {
			List<Long> list = Autoreply.ins.getCQCode().getAts(msg);
			long me = Autoreply.ins.getCoolQ().getLoginQQ();
			for (long l : list) {
				if (l == me) {
					return true;
				}
			}
			return false;
		}

		public static String getTime() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}

		public static String getTime(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
		}

		public static String getDate() {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}

		public static String getDate(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
		}
		/*  public static String getG_tk(String skey) {
		 int hash = 5381;
		 int flag = skey.length();
		 for (int i = 0; i < flag; i++) {
		 hash = hash + hash * 32 + skey.charAt(i);
		 }
		 return String.valueOf(hash & 0x7fffffff);
		 }*/
	}

	public static class ArrayTool {

		public static TouhouCharacter[] mergeArray(TouhouCharacter[]... charas) {
			int allLen=0;
			for (TouhouCharacter[] bs:charas) {
				allLen += bs.length;
			}
			TouhouCharacter[] finalArray=new TouhouCharacter[allLen];
			int flag=0;
			for (TouhouCharacter[] byteArray:charas) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}

		public static SpellCard[] mergeArray(SpellCard[]... spells) {
			int allLen=0;
			for (SpellCard[] bs:spells) {
				allLen += bs.length;
			}
			SpellCard[] finalArray=new SpellCard[allLen];
			int flag=0;
			for (SpellCard[] byteArray:spells) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}
		public static byte[] mergeArray(byte[]... arrays) {
			int allLen=0;
			for (byte[] bs:arrays) {
				allLen += bs.length;
			}
			byte[] finalArray=new byte[allLen];
			int flag=0;
			for (byte[] byteArray:arrays) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}

		public static String[] mergeArray(String[]... arrays) {
			int allLen=0;
			for (String[] bs:arrays) {
				allLen += bs.length;
			}
			String[] finalArray=new String[allLen];
			int flag=0;
			for (String[] byteArray:arrays) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}
		public static Object rfa(Object[] array) {
			return array[Autoreply.ins.random.nextInt(array.length)];
		}
	}

	public static class BitConverter {
		public static byte[] getBytes(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 0) & 0xff);
			bs[1] = (byte) ((s >> 8) & 0xff) ;
			return bs;	
		}

		public static byte[] getBytes(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 0) & 0xff);
			bs[1] = (byte) ((i >> 8) & 0xff);
			bs[2] = (byte) ((i >> 16) & 0xff);
			bs[3] = (byte) ((i >> 24) & 0xff);
			return bs;	
		}

		public static byte[] getBytes(long l) {
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

		public static byte[] getBytes(float f) {
			return getBytes(Float.floatToIntBits(f));
		}

		public static byte[] getBytes(Double d) {
			return getBytes(Double.doubleToLongBits(d));
		}

		public static byte[] getBytes(String s) {
			try {
				return s.getBytes(DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static short toShort(byte[] data, int pos) {
			return (short) ((data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8);
		}

		public static short toShort(byte[] data) {
			return toShort(data , 0);
		}

		public static int toInt(byte[] data, int pos) {
			return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
		}

		public static int toInt(byte[] data) {
			return toInt(data, 0);
		}

		public static long toLong(byte[] data, int pos) {
			return ((data[pos] & 0xffL) << 0) | (data[pos + 1] & 0xffL) << 8 | (data[pos + 2] & 0xffL) << 16 | (data[pos + 3] & 0xffL) << 24 | (data[pos + 4] & 0xffL) << 32 | (data[pos + 5] & 0xffL) << 40 | (data[pos + 6] & 0xffL) << 48 | (data[pos + 7] & 0xffL) << 56;
		}

		public static long toLong(byte[] data) {
			return toLong(data , 0);
		}

		public static float toFloat(byte[] data, int pos) {
			return Float.intBitsToFloat(toInt(data, pos));
		}

		public static float toFloat(byte[] data) {
			return toFloat(data , 0);
		}

		public static double toDouble(byte[] data, int pos) {
			return Double.longBitsToDouble(toLong(data, pos));
		}

		public static double toDouble(byte[] data) {
			return toDouble(data , 0);
		}

		public static String toString(byte[] data, int pos, int byteCount) {
			try {
				return new String(data, pos, byteCount, DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static String toString(byte[] data) {
			return toString(data, 0, data.length);
		}
	}


	public static class Base64 {
		public static final byte[] encode(String str) {
			try {
				return encode(str.getBytes(DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		public static final byte[] encode(byte[] byteData) {
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int iDestIdx; 
			byte[] byteDest = new byte[((byteData.length + 2) / 3) * 4];
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < byteData.length - 2; iSrcIdx += 3) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 2] >>> 6) & 003 | (byteData[iSrcIdx + 1] << 2) & 077);
				byteDest[iDestIdx++] = (byte) (byteData[iSrcIdx + 2] & 077);
			}
			if (iSrcIdx < byteData.length) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				if (iSrcIdx < byteData.length - 1) {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] << 2) & 077);
				} else {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] << 4) & 077);
				}
			}
			for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
				if (byteDest[iSrcIdx] < 26) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'A');
				} else if (byteDest[iSrcIdx] < 52) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'a' - 26);
				} else if (byteDest[iSrcIdx] < 62) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + '0' - 52);
				} else if (byteDest[iSrcIdx] < 63) {
					byteDest[iSrcIdx] = '+';
				} else {
					byteDest[iSrcIdx] = '/';
				}
			}
			for (; iSrcIdx < byteDest.length; iSrcIdx++) {
				byteDest[iSrcIdx] = '=';
			}
			return byteDest;
		}

		public final static byte[] decode(String str) throws IllegalArgumentException {
			byte[] byteData = null;
			try {
				byteData = str.getBytes(DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int reviSrcIdx; 
			int iDestIdx; 
			byte[] byteTemp = new byte[byteData.length];
			for (reviSrcIdx = byteData.length; reviSrcIdx - 1 > 0 && byteData[reviSrcIdx - 1] == '='; reviSrcIdx--) {
				; // do nothing. I'm just interested in value of reviSrcIdx
			}
			if (reviSrcIdx - 1 == 0)	{ 
				return null; 
			}
			byte byteDest[] = new byte[((reviSrcIdx * 3) / 4)];
			for (iSrcIdx = 0; iSrcIdx < reviSrcIdx; iSrcIdx++) {
				if (byteData[iSrcIdx] == '+') {
					byteTemp[iSrcIdx] = 62;
				} else if (byteData[iSrcIdx] == '/') {
					byteTemp[iSrcIdx] = 63;
				} else if (byteData[iSrcIdx] < '0' + 10) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 52 - '0');
				} else if (byteData[iSrcIdx] < ('A' + 26)) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] - 'A');
				}  else if (byteData[iSrcIdx] < 'a' + 26) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 26 - 'a');
				}
			}
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < reviSrcIdx && iDestIdx < ((byteDest.length / 3) * 3); iSrcIdx += 4) {
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 2] << 6) & 0xC0 | byteTemp[iSrcIdx + 3] & 0x3F);
			}
			if (iSrcIdx < reviSrcIdx) {
				if (iSrcIdx < reviSrcIdx - 2) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				} else if (iSrcIdx < reviSrcIdx - 1) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				}  else {
					throw new IllegalArgumentException("Warning: 1 input bytes left to process. This was not Base64 input");
				}
			}
			return byteDest;
		}
	}
}
