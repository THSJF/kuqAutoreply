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

public class DicReplyGroup {

	private JsonParser parser;
	private JsonObject obj;
	private Iterator it;
	private long groupNum;
	private String filePath;
	private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();

	public DicReplyGroup(long group, String filePath) {
		groupNum = group;
		parser = new JsonParser();
		this.filePath = filePath;
	}

	public boolean checkMsg(long group,long qq, String msg) throws IOException {

		if (group == groupNum) {
			obj = parser.parse(readToString(filePath)).getAsJsonObject();
			it = obj.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				if (Pattern.matches(".*" + (String) entry.getKey() + ".*", msg.replace(" ", "").trim())) {
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

		}
		return false;
	}

	public String readToString(String fileName) {
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

	public String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

}
