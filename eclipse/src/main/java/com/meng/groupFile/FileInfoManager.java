package com.meng.groupFile;

import com.google.gson.*;
import com.google.gson.annotations.*;
import com.meng.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.util.*;

public class FileInfoManager {
    public FileInfoManager() {
		
    }

    public void check(int subType, int sendTime, long fromGroup, long fromQQ, String file) {
        GroupFile groupFile = Autoreply.CQ.getGroupFile(file);
        if (groupFile == null) { // 解析群文件信息，如果失败直接忽略该消息
            return;
        }
        String url1 = "http://qun.qzone.qq.com/cgi-bin/group_share_list?uin=" + Autoreply.CQ.getLoginQQ() + "&groupid="
			+ fromGroup + "&bussinessid=0&" + "r=" + (Autoreply.instence.random.nextInt() / 100000000000000L)
			+ "&charset=utf-8&g_tk=" + Autoreply.CQ.getCsrfToken();
        String json1 = Methods.getSourceCode(url1, Autoreply.CQ.getCookies());
        FileList fileList = new Gson().fromJson(
			json1.substring(json1.indexOf("{"), json1.lastIndexOf("}") + 1).replace("\\", ""), FileList.class);
        // FileItem fileItem = fileList.getFileItemByName(com.meng.groupFile.getName());
        FileList.FileListData.FileItem fileItem = fileList.data.item.get(0);
        String url2 = "http://qun.qzone.qq.com/cgi-bin/group_share_get_downurl?uin=" + Autoreply.CQ.getLoginQQ()
			+ "&groupid=" + fromGroup + "&pa=" + Autoreply.instence.naiManager.encode(fileItem.filepath) + "&r="
			+ (Autoreply.instence.random.nextInt() / 100000000000000L) + "&charset=utf-8&g_tk=" + Autoreply.CQ.getCsrfToken();
        String json2 = Methods.getSourceCode(url2, Autoreply.CQ.getCookies());
        JsonObject jsonObject = new JsonParser()
			.parse(json2.substring(json2.indexOf("{"), json2.lastIndexOf("}") + 1).replace("\\", ""))
			.getAsJsonObject();
        String downloadUrl = jsonObject.get("data").getAsJsonObject().get("url").getAsString();
        Autoreply.sendMessage(fromGroup, 0,
							  "可通过" + downloadUrl + "/" + Autoreply.instence.naiManager.encode(groupFile.getName()) + "下载文件");
    }

	public class FileList {
		public int code;
		public FileListData data;
		@SerializedName("default")
		public int _default;
		public String message;
		public int subcode;

		public ArrayList<FileList.FileListData.FileItem> getFileItemByName(String name) {
			ArrayList<FileList.FileListData.FileItem> fileItems = new ArrayList<>();
			for (FileList.FileListData.FileItem fileItem : data.item) {
				if (fileItem.filename.equals(name)) {
					fileItems.add(fileItem);
				}
			}
			return fileItems;
		}

		public class FileListData {
			public long UsedHigh;
			public long UsedLow;
			public long ability;
			public long capacity;
			public int grouplevel;
			public ArrayList<FileItem> item;
			public long surplus_capacity;

			public class FileItem {
				public int auditflag;
				public int busid;
				public int createtime;
				public int downloadtimes;
				public long filelenhight;
				public long filelenlow;
				public String filename;
				public String filepath;
				public long filesize;
				public String localname;
				public int modifytime;
				public String ownernick;
				public long owneruin;
				public int ttl;
				public long uploadlenhigh;
				public long uploadlenlow;
				public String uploadnick;
				public long uploadsize;
				public long uploaduin;
			}
		}
	}
}
