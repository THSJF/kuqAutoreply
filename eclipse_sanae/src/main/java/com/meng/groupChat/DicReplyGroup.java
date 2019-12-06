package com.meng.groupChat;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public class DicReplyGroup {

    public long groupNum;
    private HashMap<String, HashSet<String>> dic = new HashMap<>();

    public DicReplyGroup(long group) {
        groupNum = group;
        File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
        if (!dicFile.exists()) {
            DicReplyManager.saveDic(dicFile, dic);
        }
        Type type = new TypeToken<HashMap<String, HashSet<String>>>() {
        }.getType();
        dic = Autoreply.gson.fromJson(Methods.readFileToString(dicFile.getAbsolutePath()), type);
    }

    public boolean checkMsg(long group, long qq, String msg) {
        if (group != groupNum) {
            return false;
        }
        for (String key : dic.keySet()) {
            if (Pattern.matches(".*" + key + ".*", msg.replace(" ", "").trim())) {
                Autoreply.sendMessage(group, qq, (String) dic.get(key).toArray()[Autoreply.instence.random.nextInt(dic.get(key).size())]);
                return true;
            }
        }
        return false;
    }

}
