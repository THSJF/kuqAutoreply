package com.meng.config;

import com.google.gson.reflect.*;
import com.meng.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;
import com.meng.tools.*;

public class ConfigManager extends WebSocketClient {
    public ConfigJavaBean configJavaBean = new ConfigJavaBean();

	private ConcurrentHashMap<Integer,TaskResult> resultMap=new ConcurrentHashMap<>();

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
		switch (dataPackRecieved.getOpCode()) {
			case DataPack._2retConfig:
				Type type = new TypeToken<ConfigJavaBean>() {
				}.getType();
				configJavaBean = Autoreply.gson.fromJson(dataPackRecieved.readString(), type);	
				break;
			case DataPack._4retOverSpell:
				TaskResult tr_4=new TaskResult(BitConverter.getBytes(dataPackRecieved.readString()));
				resultMap.put(DataPack._4retOverSpell, tr_4);
				break;
			case DataPack._6retOverPersent:
				int persent=dataPackRecieved.readInt();//0-10000
				break;
			case DataPack._8retGrandma:
				String grandma=dataPackRecieved.readString();
				break;
			case DataPack._10retMusicName:
				String musicName=dataPackRecieved.readString();
				break;
			case DataPack._12retGotSpells:
				String jsonStr=dataPackRecieved.readString();
				break;
			case DataPack._14retNeta:
				String neta=dataPackRecieved.readString();
				break;
			default:
				dataToSend = DataPack.encode(DataPack._0notification, dataPackRecieved);
				dataToSend.writeString("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend.getData());
			} catch (WebsocketNotConnectedException e) {
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

	public String getOverSpell(long fromQQ) {
		DataPack dp = DataPack.encode(DataPack._3getOverSpell, System.currentTimeMillis());
		dp.writeLong(fromQQ);
		send(dp.getData());
		while (resultMap.get(DataPack._3getOverSpell) == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		TaskResult tr=resultMap.get(DataPack._3getOverSpell);
		resultMap.remove(DataPack._3getOverSpell);
		return BitConverter.toString(tr.data);
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
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将用户" + qq + "加入黑名单");
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将群" + group + "加入黑名单");
    }

}
