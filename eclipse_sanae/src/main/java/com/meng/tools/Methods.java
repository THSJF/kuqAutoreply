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

    public static Object rfa(Object[] array) {
        return array[Autoreply.instence.random.nextInt(array.length)];
    }

    // 有@的时候
    public static boolean checkAt(long fromGroup, long fromQQ, String msg) {
        if (Autoreply.instence.CC.getAt(msg) == Autoreply.CQ.getLoginQQ()) {      
            // 过滤特定的文字
            // @消息发送者并复读内容
            Autoreply.sendMessage(fromGroup, 0, msg.replace("[CQ:at,qq=" + Autoreply.CQ.getLoginQQ() + "]", "[CQ:at,qq=" + fromQQ + "]"));
            return true;
        }
        return false;
    }

    // 读取文本文件
    public static String readFileToString(String fileName) {
        return readFileToString(new File(fileName));
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
	/*  public static String getG_tk(String skey) {
	 int hash = 5381;
	 int flag = skey.length();
	 for (int i = 0; i < flag; i++) {
	 hash = hash + hash * 32 + skey.charAt(i);
	 }
	 return String.valueOf(hash & 0x7fffffff);
	 }*/
}
