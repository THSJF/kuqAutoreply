package com.meng.groupChat;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public class DicReply {

    private HashMap<String, HashSet<String>> dic = new HashMap<>();

    public DicReply() {
        File dicFile = new File(Autoreply.appDirectory + "dic\\dic.json");
        Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
        }.getType();
        dic = Autoreply.gson.fromJson(Methods.readFileToString(dicFile.getAbsolutePath()), type);
    }

    public boolean check(long group, long qq, String msg) {
        for (String key : dic.keySet()) {
            if (Pattern.matches(".*" + key + ".*", msg.replaceAll("\\s", "").trim())) {
                Autoreply.sendMessage(group, qq, (String) dic.get(key).toArray()[Autoreply.instence.random.nextInt(dic.get(key).size())]);
                return true;
            }
        }
        return false;
    }
}
