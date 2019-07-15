package com.meng.groupChat;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.meng.Autoreply;
import com.meng.tools.Methods;
import com.meng.tools.UserCounter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

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
        dic = new Gson().fromJson(Methods.readFileToString(dicFile.getAbsolutePath()), type);
    }

    public boolean checkMsg(long group, long qq, String msg) {
        if (group == groupNum) {
            for (String key : dic.keySet()) {
                if (Pattern.matches(".*" + key + ".*", msg.replace(" ", "").trim())) {
                    Autoreply.sendMessage(group, qq, (String) dic.get(key).toArray()[Autoreply.instence.random.nextInt(dic.get(key).size())]);
                    return true;
                }
            }
        }
        return false;
    }

}
