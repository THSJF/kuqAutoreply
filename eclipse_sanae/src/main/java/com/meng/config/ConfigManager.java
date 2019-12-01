package com.meng.config;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.config.javabeans.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.java_websocket.exceptions.*;

public class ConfigManager extends WebSocketClient {
    public ConfigJavaBean configJavaBean = new ConfigJavaBean();
    public Gson gson = new Gson();

	public ConfigManager(URI uri) {
		super(uri);		
	}

	@Override
	public void onMessage(String p1) {

	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		DataPack dp=DataPack.encode(DataPack._1getConfig, System.currentTimeMillis());
		send(dp.getData());
		Autoreply.sendMessage(807242547L, 0, "连接到鬼人正邪");
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		DataPack dataPackRecieved=DataPack.decode(bs.array());
		DataPack dataToSend=null;
		long group=0;
		long qq=0;
		switch (dataPackRecieved.getOpCode()) {
			case DataPack._2retConfig:
				Type type = new TypeToken<ConfigJavaBean>() {
				}.getType();
				configJavaBean = gson.fromJson(dataPackRecieved.readString(), type);	
				break;
			case DataPack._4retOverSpell:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				String spell=dataPackRecieved.readString();
				break;
			case DataPack._6retOverPersent:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				int persent=dataPackRecieved.readInt();//0-10000
				break;
			case DataPack._8retGrandma:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				String grandma=dataPackRecieved.readString();
				break;
			case DataPack._10retMusicName:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				String musicName=dataPackRecieved.readString();
				break;
			case DataPack._12retGotSpells:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				String jsonStr=dataPackRecieved.readString();
				break;
			case DataPack._14retNeta:
				group = dataPackRecieved.readLong();
				qq = dataPackRecieved.readLong();
				String neta=dataPackRecieved.readString();
				break;
			default:
				dataToSend = DataPack.encode(DataPack._0notification, dataPackRecieved);
				dataToSend.writeString("操作类型错误");
		}
		if (dataToSend != null) {
			try{
			send(dataToSend.getData());
			}catch(WebsocketNotConnectedException e){
				Autoreply.sendMessage(807242547L, 0, "和鬼人正邪的连接已断开");
				reconnect();
			}
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onError(Exception e) {

	}

	public boolean containsGroup(long group) {
		for (GroupConfig gf:configJavaBean.groupConfigs) {
			if (gf.groupNumber == group) {
				return true;
			}
		}
		return false;
	}

	public void setNickName(long qq, String nickname) {
		if (nickname != null) {
			configJavaBean.nicknameMap.put(qq, nickname);
		} else {
			configJavaBean.nicknameMap.remove(qq);
		}
	}

	public String getNickName(long qq) {
		String nick=null;
		nick = configJavaBean.nicknameMap.get(qq);
		if (nick == null) {
			PersonInfo pi=getPersonInfoFromQQ(qq);
			if (pi == null) {
				nick = Autoreply.CQ.getStrangerInfo(qq).getNick();
			} else {
				nick = pi.name;
			}
		}
		return nick;
	}

	public String getNickName(long group, long qq) {
		String nick=null;
		nick = configJavaBean.nicknameMap.get(qq);
		if (nick == null) {
			PersonInfo pi=getPersonInfoFromQQ(qq);
			if (pi == null) {
				nick = Autoreply.CQ.getGroupMemberInfo(group, qq).getNick();
			} else {
				nick = pi.name;
			}
		}
		return nick;
	}

    public boolean isMaster(long fromQQ) {
        return configJavaBean.masterList.contains(fromQQ);
    }

    public boolean isAdmin(long fromQQ) {
        return configJavaBean.adminList.contains(fromQQ) || configJavaBean.masterList.contains(fromQQ);
    }

    public boolean isGroupAutoAllow(long fromQQ) {
        return configJavaBean.groupAutoAllowList.contains(fromQQ) || configJavaBean.adminList.contains(fromQQ) || configJavaBean.masterList.contains(fromQQ);
    }

    public GroupConfig getGroupConfig(long fromGroup) {
        for (GroupConfig gc : configJavaBean.groupConfigs) {
            if (fromGroup == gc.groupNumber) {
                return gc;
            }
        }
        return null;
    }

    public boolean isNotReplyGroup(long fromGroup) {
        GroupConfig groupConfig = getGroupConfig(fromGroup);
        return groupConfig == null || !groupConfig.reply;
    }

    public boolean isNotReplyQQ(long qq) {
        return configJavaBean.QQNotReply.contains(qq) || configJavaBean.blackListQQ.contains(qq);
    }

    public boolean isBlackQQ(long qq) {
        return configJavaBean.blackListQQ.contains(qq);
    }

    public boolean isBlackGroup(long qq) {
        return configJavaBean.blackListGroup.contains(qq);
    }

    public boolean isNotReplyWord(String word) {
        for (String nrw : configJavaBean.wordNotReply) {
            if (word.contains(nrw)) {
                return true;
            }
        }
        return false;
    }

    public PersonInfo getPersonInfoFromQQ(long qq) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.qq == qq) {
                return pi;
            }
        }
        return null;
    }

    public PersonInfo getPersonInfoFromName(String name) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.name.equals(name)) {
                return pi;
            }
        }
        return null;
    }

    public PersonInfo getPersonInfoFromBid(long bid) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.bid == bid) {
                return pi;
            }
        }
        return null;
    }

	public PersonInfo getPersonInfoFromLiveId(long lid) {
        for (PersonInfo pi : configJavaBean.personInfo) {
            if (pi.bliveRoom == lid) {
                return pi;
			}
		}
        return null;
	}

    public void addBlack(long group, final long qq) {
        configJavaBean.blackListQQ.add(qq);
        configJavaBean.blackListGroup.add(group);
        for (GroupConfig groupConfig : configJavaBean.groupConfigs) {
            if (groupConfig.groupNumber == group) {
                configJavaBean.groupConfigs.remove(groupConfig);
                break;
            }
        }

        Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					//    HashSet<Group> groups = Methods.findQQInAllGroup(qq);
					//   for (Group g : groups) {
                    // if (Methods.ban(g.getId(), qq, 300)) {
                    //    sendMessage(g.getId(), 0, "不要问为什么你会进黑名单，你干了什么自己知道");
                    //   }
					//    }
				}
			});
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将用户" + qq + "加入黑名单");
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将群" + group + "加入黑名单");
    }

}
