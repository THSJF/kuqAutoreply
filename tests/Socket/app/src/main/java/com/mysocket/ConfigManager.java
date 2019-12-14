package com.mysocket;

import com.google.gson.*;
import com.google.gson.reflect.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;

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
		SanaeDataPack dp=SanaeDataPack.encode(SanaeDataPack._1getConfig, System.currentTimeMillis());
	//	send(dp.getData());
		MainActivity.instence.showToast("连接到鬼人正邪");
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		SanaeDataPack dataPackRecieved=SanaeDataPack.decode(bs.array());
		SanaeDataPack dataToSend=null;
		switch (dataPackRecieved.getOpCode()) {
			case SanaeDataPack._2retConfig:
				Type type = new TypeToken<ConfigJavaBean>() {
				}.getType();
				String s=dataPackRecieved.readString();
				configJavaBean = new Gson().fromJson(s, type);	
				MainActivity.instence.recieved.add(s);
				break;
			case SanaeDataPack._4retOverSpell:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readString()));
				break;
			case SanaeDataPack._6retOverPersent:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readInt()));
				break;
			case SanaeDataPack._8retGrandma:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readString()));
				break;
			case SanaeDataPack._10retMusicName:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readString()));
				break;
			case SanaeDataPack._12retGotSpells:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readString()));
				break;
			case SanaeDataPack._14retNeta:
				resultMap.put(dataPackRecieved.getOpCode(), new TaskResult(dataPackRecieved.readString()));
				break;
			default:
				dataToSend = SanaeDataPack.encode(SanaeDataPack._0notification, dataPackRecieved);
				dataToSend.write("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend.getData());
			} catch (WebsocketNotConnectedException e) {
				MainActivity.instence.showToast("和鬼人正邪的连接已断开");
				reconnect();
			}
		}
		MainActivity.instence.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity.instence.adp.notifyDataSetChanged();
				}	
			});
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onError(Exception e) {

	}

	public String getOverSpell(long fromQQ) {
		SanaeDataPack dp = SanaeDataPack.encode(SanaeDataPack._3getOverSpell, System.currentTimeMillis());
		dp.write(fromQQ);
		send(dp.getData());
		while (resultMap.get(SanaeDataPack._4retOverSpell) == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		TaskResult tr=resultMap.get(SanaeDataPack._4retOverSpell);
		resultMap.remove(SanaeDataPack._4retOverSpell);
		return BitConverter.toString(tr.data);
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
}
