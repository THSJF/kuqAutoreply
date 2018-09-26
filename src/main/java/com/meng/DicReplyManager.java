package com.meng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DicReplyManager {

	private HashMap<Integer, DicReplyGroup> groupMap = new HashMap<Integer, DicReplyGroup>();
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();
	private int mapFlag = 0;
	

	private JsonParser parser;
	private JsonObject obj;
	private Iterator it;
	private String filePath;

	public DicReplyManager(String filePath) {
		parser = new JsonParser();
		this.filePath = filePath;
	}

	public void addData(DicReplyGroup drp) {
		groupMap.put(mapFlag, drp);
		mapFlag++;
	}

	public boolean check(long group,long qq, String msg) throws IOException {
		boolean b=false;
		if (b==false) {
			b=b|checkPublicDic(group, qq, msg);
		}
		for (int i = 0; i < mapFlag; i++) {
			b=b|groupMap.get(i).checkMsg(group,qq, msg);
		}
		return b;
	}
	
	private boolean checkPublicDic(long group,long qq, String msg) throws IOException {
			obj = parser.parse(readToString(filePath)).getAsJsonObject();
			it = obj.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				if (((String) entry.getKey()).equalsIgnoreCase(msg.replace(" ", "").replace("\n", "").trim())) {
					JsonArray array = (JsonArray) entry.getValue();
					int arraySize = array.size();
					if (arraySize != 0) {
						int k = 0;
						for (; k < arraySize; k++) {
							String string = removeCharAt(array.get(k).toString(), 0);
							replyPool.put(k, removeCharAt(string, string.length() - 1));
						}
						Autoreply.sendGroupMessage(group,qq, replyPool.get(Autoreply.random.nextInt(k)));
						replyPool.clear();
						return true;
					}
				}

			}

		return false;

	}

	private String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	private String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

}
