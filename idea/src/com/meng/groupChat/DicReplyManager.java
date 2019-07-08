package com.meng.groupChat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.tools.Methods;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class DicReplyManager {

    private HashMap<Long, DicReplyGroup> groupMap = new HashMap<Long, DicReplyGroup>();
    private HashMap<Integer, String> replyPool = new HashMap<Integer, String>();
    private JsonParser parser;
    private String jsonString;

    public DicReplyManager() {
        parser = new JsonParser();
        try {
            jsonString = Methods.readFileToString(Autoreply.appDirectory + "dic\\dic.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addData(DicReplyGroup drp) {
        groupMap.put(drp.groupNum, drp);
    }

    public boolean check(long group, long qq, String msg) {
        // 查找公用词库（完全相同才会触发）
        if (checkPublicDic(group, qq, msg)) {
            return true;
        }
        // 查找群专用词库（触发条件为匹配正则表达式）
        return groupMap.get(group).checkMsg(group, qq, msg);
    }

    @SuppressWarnings("rawtypes")
    private boolean checkPublicDic(long group, long qq, String msg) {
        HashSet<String> replyPool = new HashSet<>();
        try {
            JsonObject obj = parser.parse(jsonString).getAsJsonObject();
            for (Entry<String, JsonElement> stringJsonElementEntry : obj.entrySet()) {
                if (((Entry) stringJsonElementEntry).getKey().toString().equals(msg)) {
                    JsonArray array = (JsonArray) ((Entry) stringJsonElementEntry).getValue();
                    for (Object o : array) {
                        replyPool.add(Methods.removeCharAtStartAndEnd(o.toString()));// 读取出来的数据是带有引号的
                    }
                    Autoreply.sendMessage(group, qq,
                            (String) replyPool.toArray()[Autoreply.instence.random.nextInt(array.size())]);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

}
