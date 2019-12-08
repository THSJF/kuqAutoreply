package com.meng.config;

import com.google.gson.reflect.*;
import com.meng.*;
import com.meng.dice.*;
import com.meng.groupChat.*;
import com.meng.groupChat.Sequence.*;
import com.meng.messageProcess.*;
import com.meng.tip.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.util.*;
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
		System.out.println("strMsg:" + p1);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		send(SanaeDataPack.encode(SanaeDataPack._1getConfig));
		System.out.println("连接到鬼人正邪");
		Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						send(SanaeDataPack.encode(SanaeDataPack._24heartBeat));
					} catch (WebsocketNotConnectedException e) {
						System.out.println("和鬼人正邪的连接已断开");
						e.printStackTrace();
						reconnect();
					}
				}
			});
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		SanaeDataPack dataRec=SanaeDataPack.decode(bs.array());
		SanaeDataPack dataToSend=null;
		switch (dataRec.getOpCode()) {
			case SanaeDataPack._2retConfig:
				Type type = new TypeToken<ConfigJavaBean>() {
				}.getType();
				configJavaBean = Autoreply.gson.fromJson(dataRec.readString(), type);
				Autoreply.instence.threadPool.execute(new Runnable(){

						@Override
						public void run() {
							Autoreply.sleeping = false;
							Autoreply.instence.seqManager = new SeqManager();
							Autoreply.instence.groupMemberChangerListener = new GroupMemberChangerListener();
							Autoreply.instence.adminMessageProcessor = new AdminMessageProcessor();
							Autoreply.instence.repeatManager = new RepeaterManager();
							Autoreply.instence.birthdayTip = new BirthdayTip();
							Autoreply.instence.spellCollect = new SpellCollect();
							Autoreply.instence.threadPool.execute(Autoreply.instence.timeTip);
							Autoreply.instence.coinManager = new CoinManager();
							Autoreply.instence.messageTooManyManager = new MessageTooManyManager();
							List<Group> groupList=Autoreply.CQ.getGroupList();
							for (Group g:groupList) {
								List<com.sobte.cqp.jcq.entity.Member> mlist=Autoreply.CQ.getGroupMemberList(g.getId());
								for (com.sobte.cqp.jcq.entity.Member m:mlist) {
									if (m.getQqId() == 1620628713L) {
										Autoreply.instence.SeijiaInThis.add(g.getId());
										break;
									}
								}
								if (!Autoreply.instence.SeijiaInThis.contains(g.getId())) {
									Autoreply.instence.repeatManager.addData(new Repeater(g.getId()));
								}

							}
						}
					});
				System.out.println("load success");
				break;
			case SanaeDataPack._4retOverSpell:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;
			case SanaeDataPack._6retOverPersent:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readInt()));
				break;
			case SanaeDataPack._8retGrandma:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;
			case SanaeDataPack._10retMusicName:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;
			case SanaeDataPack._12retGotSpells:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;
			case SanaeDataPack._14retNeta:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;			
			case SanaeDataPack._29retSeqContent:
				resultMap.put(dataRec.getOpCode(), new TaskResult(dataRec.readString()));
				break;
			case SanaeDataPack._30sendMsg:
				Autoreply.sendMessage(dataRec.readLong(), dataRec.readLong(), dataRec.readString());
				break;
			case SanaeDataPack._32retLiveList:
				StringBuilder sb=new StringBuilder();
				while (dataRec.hasNext()) {
					sb.append(dataRec.readString()).append("正在直播:").append(dataRec.readLong()).append("\n");
				}
				resultMap.put(dataRec.getOpCode(), new TaskResult(sb.toString()));
				break;
			case SanaeDataPack._33liveStart:
				Autoreply.sendMessage(Autoreply.mainGroup, 0, dataRec.readString() + "开始直播" + dataRec.readLong());
				break; 
			case SanaeDataPack._34liveStop:
				Autoreply.sendMessage(Autoreply.mainGroup, 0, dataRec.readString() + "停止直播" + dataRec.readLong());
				break;
			case SanaeDataPack._35speakInliveRoom:
				//直播间说话 string(主播称呼) long(blid) string(说话者称呼) long(说话者bid)
				break; 
			case SanaeDataPack._36newVideo:
				Autoreply.sendMessage(Autoreply.mainGroup, 0, dataRec.readString() + "发布新视频:" + dataRec.readString() + "(av" + dataRec.readLong() + ")");
				break;
			case SanaeDataPack._37newArtical:
				Autoreply.sendMessage(Autoreply.mainGroup, 0, dataRec.readString() + "发布新专栏:" + dataRec.readString() + "(cv" + dataRec.readLong() + ")");
				break;
			default:
				dataToSend = SanaeDataPack.encode(SanaeDataPack._0notification, dataRec);
				dataToSend.write("操作类型错误");
		}
		if (dataToSend != null) {
			try {
				send(dataToSend);
			} catch (WebsocketNotConnectedException e) {
				e.printStackTrace();
				reconnect();
			}
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onError(Exception e) {
		e.printStackTrace();
	}

	public String getOverSpell(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._3getOverSpell).write(fromQQ));
		return BitConverter.toString(getTaskResult(SanaeDataPack._4retOverSpell).data);
	}

	public int getOverPersent(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._5getOverPersent).write(fromQQ));
		return BitConverter.toInt(getTaskResult(SanaeDataPack._6retOverPersent).data);
	}

	public String getGrandma(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._7getGrandma).write(fromQQ));
		return BitConverter.toString(getTaskResult(SanaeDataPack._8retGrandma).data);
	}

	public String getMusicName(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._9getMusicName).write(fromQQ));
		return BitConverter.toString(getTaskResult(SanaeDataPack._10retMusicName).data);
	}

	public String getSpells(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._11getGotSpells).write(fromQQ));
		return BitConverter.toString(getTaskResult(SanaeDataPack._12retGotSpells).data);
	}

	public String getNeta(long fromQQ) {
		send(SanaeDataPack.encode(SanaeDataPack._13getNeta).write(fromQQ));
		return BitConverter.toString(getTaskResult(SanaeDataPack._14retNeta).data);
	}

	public String getSeq() {
		send(SanaeDataPack.encode(SanaeDataPack._28getSeqContent));
		return BitConverter.toString(getTaskResult(SanaeDataPack._29retSeqContent).data);
	}

	public String getLiveList() {
		send(SanaeDataPack.encode(SanaeDataPack._31getLiveList));
		return BitConverter.toString(getTaskResult(SanaeDataPack._32retLiveList).data);
	}

	private TaskResult getTaskResult(int opCode) {
		int time=3000;
		while (resultMap.get(opCode) == null && time-- > 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		TaskResult tr=resultMap.get(opCode);
		resultMap.remove(opCode);
		return tr;
	}

	public void setNickName(long qq, String nickname) {
		if (nickname != null) {
			configJavaBean.nicknameMap.put(qq, nickname);
			send(SanaeDataPack.encode(SanaeDataPack._25setNick).write(qq).write(nickname));
		} else {
			configJavaBean.nicknameMap.remove(qq);
			send(SanaeDataPack.encode(SanaeDataPack._25setNick).write(qq));
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
		send(SanaeDataPack.encode(SanaeDataPack._26addBlack).write(group).write(qq));
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将用户" + qq + "加入黑名单");
        Autoreply.sendMessage(Autoreply.mainGroup, 0, "已将群" + group + "加入黑名单");
		Autoreply.CQ.setGroupLeave(group, false);
    }

	public void send(final SanaeDataPack sdp) {
		Autoreply.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					send(sdp.getData());
				}
			});
	}
}
