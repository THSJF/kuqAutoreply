package com.meng.bilibili;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meng.Autoreply;
import com.meng.bilibili.main.ArticleInfoBean;
import com.meng.bilibili.main.VideoInfoBean;
import com.meng.config.Base64;
import com.meng.tools.Methods;

public class BiliLinkInfo {

    private final String liveUrl = "live.bilibili.com/";

    public BiliLinkInfo() {
    }

    public boolean checkOgg(long fromGroup, long fromQQ, String msg) {
        if (msg.startsWith("bav:")) {
            Autoreply.sendMessage(fromGroup, fromQQ, processVideo(msg.substring(4)));
            return true;
        }
        if (msg.startsWith("bcv:")) {
            Autoreply.sendMessage(fromGroup, fromQQ, processArtical(msg.substring(4)));
            return true;
        }
        if (msg.startsWith("blv:")) {
            Autoreply.sendMessage(fromGroup, fromQQ, processLive(msg.substring(4)));
            return true;
        }
        if (msg.startsWith("FromUriOpen@bilibili://")) {
            String subedString = null;
            try {
                subedString = new String(Base64.decryptBASE64(msg.substring(23)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = null;
            if (subedString == null) {
                return false;
            }
            if (subedString.startsWith("av")) {
                result = "av" + getVideoId(subedString);
            } else if (subedString.startsWith("cv")) {
                result = "cv" + getArticalId(subedString);
            }
            if (result != null) {
                Autoreply.sendMessage(0, fromQQ, result);
                return true;
            }
        }
        return false;
    }

    public boolean check(long fromGroup, long fromQQ, String msg) {
        String subedUrl;
        if (msg.startsWith("FromUriOpen@bilibili://")) {
            String subedString = null;
            try {
                subedString = new String(Base64.decryptBASE64(msg.substring(23)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            if (subedString == null) {
                return false;
            }
            if (subedString.startsWith("av")) {
                result += "av" + getVideoId(subedString) + "\n";
                result += processVideo(getVideoId(subedString));
            } else if (subedString.startsWith("cv")) {
                result += "cv" + getArticalId(subedString) + "\n";
                result += processArtical(getArticalId(subedString));
            }
            Autoreply.instence.useCount.incBilibiliLink(fromQQ);
            Autoreply.instence.groupCount.incBilibiliLink(fromGroup);
            Autoreply.sendMessage(fromGroup, 0, result);
            return !msg.contains("[CQ:share,url=");
        } else {
            int ind = msg.indexOf("http");
            int ind1 = msg.indexOf(",text=");
            int ind2 = msg.indexOf(",title=");
            if (ind != -1 && ind1 != -1) {
                subedUrl = msg.substring(ind, ind1);
            } else if (ind != -1 && ind2 != -1) {
                subedUrl = msg.substring(ind, ind2);
            } else {
                subedUrl = msg;
            }
            String result = null;
            if (subedUrl.contains("www.bilibili.com/video/") || subedUrl.contains("b23.tv/av")) {
                result = processVideo(getVideoId(subedUrl));
            } else if (subedUrl.contains("www.bilibili.com/read/")) {
                result = processArtical(getArticalId(subedUrl));
            } else if (subedUrl.contains(liveUrl)) {
                result = processLive(getLiveId(subedUrl));
            }
            if (result != null) {
                Autoreply.instence.useCount.incBilibiliLink(fromQQ);
                Autoreply.instence.groupCount.incBilibiliLink(fromGroup);
                Autoreply.sendMessage(fromGroup, 0, result);
                // 如果不是分享链接就拦截消息
                return !msg.contains("[CQ:share,url=");
            }
        }
        return false;
    }

    private String processVideo(String id) {
        VideoInfoBean videoInfoBean = new Gson().fromJson(Methods.getSourceCode("http://api.bilibili.com/archive_stat/stat?aid=" + id + "&type=jsonp"), VideoInfoBean.class);
        String vidInf = videoInfoBean.toString();
        String html = Methods.getSourceCode("https://www.bilibili.com/video/av" + id);
        int index = html.indexOf("\"pubdate\":") + "\"pubdate\":".length();
        int end = html.indexOf(",\"ctime\"", index);
        long stamp = Long.parseLong(html.substring(index, end));
        int days = (int) ((System.currentTimeMillis() - stamp * 1000) / 86400000);
        if (days == 0) {
            vidInf += "24小时内发布。";
        } else {
            vidInf += days + "天前发布，平均每天" + (Float.parseFloat(videoInfoBean.data.view) / days) + "次播放。";
        }
        return vidInf;
    }

    private String processArtical(String id) {
        return new Gson().fromJson(Methods.getSourceCode("https://api.bilibili.com/x/article/viewinfo?id=" + id + "&mobi_app=pc&jsonp=jsonp"), ArticleInfoBean.class).toString();
    }

    private String processLive(String id) {
        String json = Methods.getSourceCode("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + id);
        String userName = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject().get("info").getAsJsonObject().get("uname").getAsString();
        String html = Methods.getSourceCode("https://live.bilibili.com/" + id);
        String jsonInHtml = html.substring(html.indexOf("{\"roomInitRes\":"), html.lastIndexOf("}") + 1);
        JsonObject data = new JsonParser().parse(jsonInHtml).getAsJsonObject().get("baseInfoRes").getAsJsonObject().get("data").getAsJsonObject();
        return "房间号:" + id + "\n主播:" + userName + "\n房间标题:" + data.get("title").getAsString() +
                "\n分区:" + data.get("parent_area_name").getAsString() + "-" + data.get("area_name").getAsString() +
                "\n标签:" + data.get("tags").getAsString();
    }

    private String getVideoId(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = url.indexOf("av") + 2; i < url.length(); ++i) {
            if (url.charAt(i) >= 48 && url.charAt(i) <= 57) {
                stringBuilder.append(url.charAt(i));
            } else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    private String getArticalId(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = url.indexOf("cv") + 2; i < url.length(); ++i) {
            if (url.charAt(i) >= 48 && url.charAt(i) <= 57) {
                stringBuilder.append(url.charAt(i));
            } else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    private String getLiveId(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = url.indexOf(liveUrl) + liveUrl.length(); i < url.length(); ++i) {
            if (url.charAt(i) >= 48 && url.charAt(i) <= 57) {
                stringBuilder.append(url.charAt(i));
            } else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    public static String encodeBilibiliURL(long id, boolean av) {
        try {
            return "FromUriOpen@bilibili://" + Base64.encryptBASE64(((av ? "av:" : "cv") + id).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
