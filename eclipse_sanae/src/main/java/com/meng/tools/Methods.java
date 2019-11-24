package com.meng.tools;

import com.meng.*;
import com.sobte.cqp.jcq.entity.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;
import org.jsoup.*;

public class Methods {

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
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(files)))));
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
            File folder = (File) rfa(files);
            File[] pics = folder.listFiles();
            Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(pics)))));
            Autoreply.instence.useCount.incSetu(fromQQ);
            Autoreply.instence.groupCount.incSetu(fromGroup);
            Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
        } else if (msg.endsWith("色图")) {
            File[] files = (new File(Autoreply.appDirectory + "setu/" + msg.replace("色图", ""))).listFiles();
            if (files != null && files.length > 0) {
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(files)))));
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
            File folder = (File) rfa(files);
            File[] pics = folder.listFiles();
            Autoreply.instence.useCount.incSetu(fromQQ);
            Autoreply.instence.groupCount.incSetu(fromGroup);
            Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
            Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(pics)))));
        } else if (msg.endsWith("女装")) {
            File[] files = (new File(Autoreply.appDirectory + "nvzhuang/" + msg.replace("女装", ""))).listFiles();
            if (files != null && files.length > 0) {
                Autoreply.instence.useCount.incSetu(fromQQ);
                Autoreply.instence.groupCount.incSetu(fromGroup);
                Autoreply.instence.useCount.incSetu(Autoreply.CQ.getLoginQQ());
                Autoreply.instence.threadPool.execute(new DeleteMessageRunnable(Autoreply.sendMessage(fromGroup, fromQQ, Autoreply.instence.CC.image((File) Methods.rfa(files)))));
            }
            return true;
        }

        return false;
    }

    // randomFromArray 随机返回数组中的一项
    public static Object rfa(Object[] array) {
        return array[Autoreply.instence.random.nextInt(2147483647) % array.length];
    }

    // 有@的时候
    public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
        if (msg.contains("~") || msg.contains("～")) {
            return false;
        }
        if (Autoreply.instence.CC.getAt(msg) == Autoreply.CQ.getLoginQQ()) {
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

    // 读取文本文件
    public static String readFileToString(String fileName) {
        String s = "{}";
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            long filelength = file.length();
            byte[] filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            s = new String(filecontent, StandardCharsets.UTF_8);
        } catch (Exception e) {

        }
        return s;
    }

	// 读取文本文件
    public static String readFileToString(File f) {
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

        }
        return s;
    }

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

	public static String stringToMD5(String str) {

		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			return toHexString(mdTemp.digest());
		} catch (Exception e) {
			return null;
		}
	}

	public static String fileNameToMD5(String fileName) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
			return streamToMD5(inputStream);
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

	public static String streamToMD5(InputStream inputStream) {
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

	/*  public static String getG_tk(String skey) {
	 int hash = 5381;
	 int flag = skey.length();
	 for (int i = 0; i < flag; i++) {
	 hash = hash + hash * 32 + skey.charAt(i);
	 }
	 return String.valueOf(hash & 0x7fffffff);
	 }*/
}
