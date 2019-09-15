package com.meng.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import com.meng.config.javabeans.GroupConfig;
import com.meng.config.javabeans.PersonInfo;

public class SocketConfigRunnable implements Runnable {
    private Socket socket = null;
    private ConfigManager configManager;

    public SocketConfigRunnable(ConfigManager configManager, ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SocketConfigManager init failed");
        }
        this.configManager = configManager;
        InetAddress address = socket.getInetAddress();
        System.out.println("当前客户端的IP ： " + address.getHostAddress());
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String string = dataInputStream.readUTF();
            // string = new String(Base64.decryptBASE64(string), "utf-8");
            System.out.println("服务器读取客户端的：" + string);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(processText(string));
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String processText(String string) {
        if (string.equals("getFull")) {
            return configManager.gson.toJson(configManager.configJavaBean);
        }
        String type = string.substring(0, string.indexOf("."));
        String content = string.substring(string.indexOf(".") + 1);
		if(content.contains("砂")){
			return "fafafa";
		}
        switch (NetworkType.valueOf(type)) {
            case addGroup:
                configManager.configJavaBean.groupConfigs.add(configManager.gson.fromJson(content, GroupConfig.class));
                break;
            case addNotReplyUser:
                configManager.configJavaBean.QQNotReply.add(Long.parseLong(content));
                break;
            case addNotReplyWord:
                configManager.configJavaBean.wordNotReply.add(content);
                break;
            case addPersonInfo:
                configManager.configJavaBean.personInfo.add(configManager.gson.fromJson(content, PersonInfo.class));
                break;
            case addMaster:
                configManager.configJavaBean.masterList.add(Long.parseLong(content));
                break;
            case addAdmin:
                configManager.configJavaBean.adminList.add(Long.parseLong(content));
                break;
            case addGroupAllow:
				configManager.addAutoAllow(Long.parseLong(content));
                break;
            case addBlackQQ:
                configManager.configJavaBean.blackListQQ.add(Long.parseLong(content));
                break;
            case addBlackGroup:
                configManager.configJavaBean.blackListGroup.add(Long.parseLong(content));
                break;
            case removeGroup:
                configManager.configJavaBean.groupConfigs.remove(configManager.gson.fromJson(content, GroupConfig.class));
                break;
            case removeNotReplyUser:
                configManager.configJavaBean.QQNotReply.remove(Long.parseLong(content));
                break;
            case removeNotReplyWord:
                configManager.configJavaBean.wordNotReply.remove(content);
                break;
            case removePersonInfo:
                configManager.configJavaBean.personInfo.remove(configManager.gson.fromJson(content, PersonInfo.class));
                break;
            case removeMaster:
                configManager.configJavaBean.masterList.remove(Long.parseLong(content));
                break;
            case removeAdmin:
                configManager.configJavaBean.adminList.remove(Long.parseLong(content));
                break;
            case removeGroupAllow:
                configManager.removeAutoAllow(Long.parseLong(content));
                break;
            case removeBlackQQ:
                configManager.configJavaBean.blackListQQ.remove(Long.parseLong(content));
                break;
            case removeBlackGroup:
                configManager.configJavaBean.blackListGroup.remove(Long.parseLong(content));
                break;
            case setGroup:
                GroupConfig groupConfig = configManager.gson.fromJson(content, GroupConfig.class);
                for (GroupConfig gc : configManager.configJavaBean.groupConfigs) {
                    if (gc.groupNumber == groupConfig.groupNumber) {
                        configManager.configJavaBean.groupConfigs.remove(gc);
                        configManager.configJavaBean.groupConfigs.add(groupConfig);
                        break;
                    }
                }
                break;
            case setNotReplyUser:
                String[] split = content.split(" ");
                long qq = Long.parseLong(split[0]);
                for (long l : configManager.configJavaBean.QQNotReply) {
                    if (l == qq) {
                        configManager.configJavaBean.QQNotReply.remove(l);
                        configManager.configJavaBean.QQNotReply.add(Long.parseLong(split[1]));
                        break;
                    }
                }
                break;
            case setNotReplyWord:
                String[] split2 = content.split(" ");
                for (String s : configManager.configJavaBean.wordNotReply) {
                    if (s.equals(split2[0])) {
                        configManager.configJavaBean.wordNotReply.remove(s);
                        configManager.configJavaBean.wordNotReply.add(split2[1]);
                        break;
                    }
                }
                break;
            case setPersonInfo:
                String[] obj = content.split(" ");
                PersonInfo oldPersonInfo = configManager.gson.fromJson(obj[0], PersonInfo.class);
                PersonInfo newPersonInfo = configManager.gson.fromJson(obj[1], PersonInfo.class);
                for (PersonInfo pi : configManager.configJavaBean.personInfo) {
                    if (pi.name.equals(oldPersonInfo.name) && pi.qq == oldPersonInfo.qq && pi.bid == oldPersonInfo.bid && pi.bliveRoom == oldPersonInfo.bliveRoom) {
                        configManager.configJavaBean.personInfo.remove(oldPersonInfo);
                        break;
                    }
                }
                configManager.configJavaBean.personInfo.add(newPersonInfo);
                break;
            case setMaster:
                String[] splitmaster = content.split(" ");
                long qqm = Long.parseLong(splitmaster[0]);
                for (long l : configManager.configJavaBean.QQNotReply) {
                    if (l == qqm) {
                        configManager.configJavaBean.masterList.remove(l);
                        configManager.configJavaBean.masterList.add(Long.parseLong(splitmaster[1]));
                        break;
                    }
                }
                break;
            case setAdmin:
                String[] splitadmin = content.split(" ");
                long qqa = Long.parseLong(splitadmin[0]);
                for (long l : configManager.configJavaBean.QQNotReply) {
                    if (l == qqa) {
                        configManager.configJavaBean.adminList.remove(l);
                        configManager.configJavaBean.adminList.add(Long.parseLong(splitadmin[1]));
                        break;
                    }
                }
                break;
            case setGroupAllow:
                String[] splitgroup = content.split(" ");
                long qqg = Long.parseLong(splitgroup[0]);
                for (long l : configManager.configJavaBean.QQNotReply) {
                    if (l == qqg) {
                        configManager.removeAutoAllow(l);
                        configManager.addAutoAllow(Long.parseLong(splitgroup[1]));
                        break;
                    }
                }
                break;
            case setBlackQQ:
                String[] splitblackqq = content.split(" ");
                long qqblack = Long.parseLong(splitblackqq[0]);
                for (long l : configManager.configJavaBean.blackListQQ) {
                    if (l == qqblack) {
                        configManager.configJavaBean.blackListQQ.remove(l);
                        configManager.configJavaBean.blackListQQ.add(Long.parseLong(splitblackqq[1]));
                        break;
                    }
                }
                break;
            case setBlackGroup:
                String[] splitblackgroup = content.split(" ");
                long blackGroup = Long.parseLong(splitblackgroup[0]);
                for (long l : configManager.configJavaBean.blackListGroup) {
                    if (l == blackGroup) {
                        configManager.configJavaBean.blackListGroup.remove(l);
                        configManager.configJavaBean.blackListGroup.add(Long.parseLong(splitblackgroup[1]));
                        break;
                    }
                }
                break;
            default:
                return "fafafa";
        }
        configManager.saveConfig();
        return "ok";
    }

}
