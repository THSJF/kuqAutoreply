package com.meng.groupChat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.tools.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class DicReplyGroup {

    private JsonParser parser;
    long groupNum;
    private String jsonString;

    public DicReplyGroup(long group) {
        groupNum = group;
        parser = new JsonParser();
        try {
            File dicFile = new File(Autoreply.appDirectory + "dic\\dic" + group + ".json");
            if (dicFile.exists()) {
                jsonString = Methods.readFileToString(dicFile.getAbsolutePath());
            } else {
                dicFile.createNewFile();
                try {
                    FileOutputStream fos = null;
                    OutputStreamWriter writer = null;
                    fos = new FileOutputStream(dicFile);
                    writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                    writer.write("{}");
                    writer.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jsonString = "{}";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean checkMsg(long group, long qq, String msg) {
        if (group == groupNum) {
            HashSet<String> replyPool = new HashSet<>();
            try {
                JsonObject obj = parser.parse(jsonString).getAsJsonObject();// 谷歌的GSON对象
                for (Entry<String, JsonElement> stringJsonElementEntry : obj.entrySet()) {// 遍历集合
                    if (Pattern.matches(".*" + ((Entry) stringJsonElementEntry).getKey() + ".*", msg.replace(" ", "").trim())) { // 使用了正则表达式查找要进行的回复
                        JsonArray array = (JsonArray) ((Entry) stringJsonElementEntry).getValue(); // 根据词库特点，一个key对应一个数组
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
        }
        return false;
    }

}
