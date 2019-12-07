package com.meng;

import com.google.gson.*;
import com.meng.config.*;
import com.meng.dice.*;
import com.meng.groupChat.*;
import com.meng.groupChat.Sequence.*;
import com.meng.messageProcess.*;
import com.meng.tip.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import com.sobte.cqp.jcq.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Autoreply extends JcqAppAbstract implements ICQVer, IMsg, IRequest {

    public static Autoreply instence;
    public Random random = new Random();
	public RepeaterManager repeatManager;
    public TimeTip timeTip = new TimeTip();
	public DicReplyManager dicReplyManager;
    public CQCodeManager CQcodeManager = new CQCodeManager();
	public ConfigManager configManager;
	public AdminMessageProcessor adminMessageProcessor;
    public GroupMemberChangerListener groupMemberChangerListener;
    public SeqManager seqManager;
	public SpellCollect spellCollect;
    public ExecutorService threadPool = Executors.newCachedThreadPool();

    public static String lastSend = " ";
    public static String lastSend2 = "  ";
    public static boolean tipedBreak = false;
    public static boolean sleeping = true;

	public static final String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";
    public HashSet<Long> botOff = new HashSet<>();
	public CoinManager coinManager;
	public BirthdayTip birthdayTip;

	public DiceImitate diceImitate=new DiceImitate();
	public static final long mainGroup=807242547L;
	public static Gson gson;
	public MessageTooManyManager messageTooManyManager;
    
    public static void main(String[] args) {
        CQ = new CoolQ(1000);
        Autoreply demo = new Autoreply();
        demo.startup();
        demo.enable();
    }

    @Override
    public String appInfo() {
        return CQAPIVER + "," + "com.meng.autoreply";
    }

    @Override
    public int startup() {
        // 获取应用数据目录(无需储存数据时，请将此行注释)
        instence = this;
        appDirectory = CQ.getAppDirectory();
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
        // 返回如：D:\CoolQ\app\com.sobte.cqp.jcq\app\com.example.demo\
        System.out.println("开始加载");
		long startTime = System.currentTimeMillis();
        groupMemberChangerListener = new GroupMemberChangerListener();
        adminMessageProcessor = new AdminMessageProcessor();
        dicReplyManager = new DicReplyManager();
        repeatManager = new RepeaterManager();
		seqManager = new SeqManager();
		birthdayTip = new BirthdayTip();
		spellCollect = new SpellCollect();
		threadPool.execute(timeTip);
		coinManager = new CoinManager();
		messageTooManyManager = new MessageTooManyManager();
		try {
			configManager = new ConfigManager(new URI("ws://123.207.65.93:9760"));
			configManager.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        System.out.println("加载完成,用时" + (System.currentTimeMillis() - startTime));
        return 0;
    }

    @Override
    public int exit() {
		threadPool.shutdownNow();
		System.exit(0);
        return 0;
    }

    @Override
    public int enable() {
        enable = true;
        return 0;
    }

    @Override
    public int disable() {
        enable = false;
        return 0;
    }

    /**
     * 私聊消息 (Type=21)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType 子类型，11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
     * @param msgId   消息ID
     * @param fromQQ  来源QQ
     * @param msg     消息内容
     * @param font    字体
     * @return 返回值*不能*直接返回文本 如果要回复消息，请调用api发送<br>
     * 这里 返回 {@link IMsg#MSG_INTERCEPT MSG_INTERCEPT} - 截断本条消息，不再继续处理
     * <br>
     * 注意：应用优先级设置为"最高"(10000)时，不得使用本返回值<br>
     * 如果不回复消息，交由之后的应用/过滤器处理，这里 返回 {@link IMsg#MSG_IGNORE MSG_IGNORE} -
     * 忽略本条消息
     */
    @Override
    public int privateMsg(int subType, final int msgId, final long fromQQ, final String msg, int font) {
        // 这里处理消息
        // if (fromQQ != 2856986197L) {
        // return MSG_IGNORE;
        // }
        if (configManager.isNotReplyQQ(fromQQ) || configManager.isNotReplyWord(msg)) {
            return MSG_IGNORE;
        }
        Autoreply.instence.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					if (configManager.isMaster(fromQQ)) {
						String[] strings = msg.split("\\.", 3);
						if (strings[0].equals("send")) {
							sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
							return;
						}
					}
				}
			});
        return MSG_IGNORE;
    }

    /**
     * 群消息 (Type=2)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType       子类型，目前固定为1
     * @param msgId         消息ID
     * @param fromGroup     来源群号
     * @param fromQQ        来源QQ号
     * @param fromAnonymous 来源匿名者
     * @param msg           消息内容
     * @param font          字体
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg, int font) {
		if (fromGroup != 807242547L)
		return MSG_IGNORE;
		if(msg.equals("s1")){
			configManager.send(SanaeDataPack.encode(SanaeDataPack._1getConfig).write(fromQQ));
		}
		configManager.send(SanaeDataPack.encode(SanaeDataPack._15incSpeak).write(fromGroup).write(fromQQ));
		if (messageTooManyManager.checkMsgTooMany(fromGroup, fromQQ, msg)) {
			return MSG_IGNORE;
		}
        if (msg.equals(".admin enable") && Autoreply.instence.configManager.isAdmin(fromQQ)) {
            GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
            if (groupConfig == null) {
                Autoreply.sendMessage(fromGroup, fromQQ, "本群没有默认配置");
                return MSG_IGNORE;
            }
            groupConfig.reply = true;
            Autoreply.sendMessage(fromGroup, fromQQ, "已由admin启用");
			return MSG_IGNORE;
        }

        if (msg.equals(".admin disable") && Autoreply.instence.configManager.isAdmin(fromQQ)) {
            GroupConfig groupConfig = Autoreply.instence.configManager.getGroupConfig(fromGroup);
            if (groupConfig == null) {
                Autoreply.sendMessage(fromGroup, fromQQ, "本群没有默认配置");
                return MSG_IGNORE;
            }
            groupConfig.reply = false;
            Autoreply.sendMessage(fromGroup, fromQQ, "已由admin停用");
			return MSG_IGNORE;
        }

        if (configManager.isNotReplyQQ(fromQQ)) {
            return MSG_IGNORE;
        }
        if (configManager.isNotReplyWord(msg)) {
            return MSG_IGNORE;
        }
        if (adminMessageProcessor.check(fromGroup, fromQQ, msg)) {
            return MSG_IGNORE;
        }
        if (configManager.isNotReplyGroup(fromGroup)) {
            return MSG_IGNORE;
        }
        threadPool.execute(new MsgRunnable(fromGroup, fromQQ, msg, msgId));
        return MSG_IGNORE;
    }

    /**
     * 讨论组消息 (Type=4)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype     子类型，目前固定为1
     * @param msgId       消息ID
     * @param fromDiscuss 来源讨论组
     * @param fromQQ      来源QQ号
     * @param msg         消息内容
     * @param font        字体
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int discussMsg(int subtype, int msgId, long fromDiscuss, long fromQQ, String msg, int font) {
        // 这里处理消息

        return MSG_IGNORE;
    }

    /**
     * 群文件上传事件 (Type=11)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subType   子类型，目前固定为1
     * @param sendTime  发送时间(时间戳)// 10位时间戳
     * @param fromGroup 来源群号
     * @param fromQQ    来源QQ号
     * @param file      上传文件信息
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int groupUpload(int subType, int sendTime, long fromGroup, long fromQQ, String file) {
        // GroupFile com.meng.groupFile = CQ.getGroupFile(file);
        // if (com.meng.groupFile == null) { // 解析群文件信息，如果失败直接忽略该消息
        // return MSG_IGNORE;
        // }
        if (configManager.isNotReplyGroup(fromGroup)) {
            return MSG_IGNORE;
        }
		//   fileInfoManager.check(subType, sendTime, fromGroup, fromQQ, file);
        return MSG_IGNORE;
    }

    /**
     * 群事件-管理员变动 (Type=101)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype        子类型，1/被取消管理员 2/被设置管理员
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param beingOperateQQ 被操作QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int groupAdmin(int subtype, int sendTime, long fromGroup, long beingOperateQQ) {
        // 这里处理消息
        if (configManager.isNotReplyGroup(fromGroup)) {
            return MSG_IGNORE;
        }
        if (subtype == 1) {
            sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "你绿帽子没莉");
        } else if (subtype == 2) {
            sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "群主给了你个绿帽子");
        }
        return MSG_IGNORE;
    }

    /**
     * 群事件-群成员减少 (Type=102)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype        子类型，1/群员离开 2/群员被踢
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param fromQQ         操作者QQ(仅子类型为2时存在)
     * @param beingOperateQQ 被操作QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int groupMemberDecrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        // 这里处理消息
        groupMemberChangerListener.checkDecrease(subtype, sendTime, fromGroup, fromQQ, beingOperateQQ);
        return MSG_IGNORE;
    }

    /**
     * 群事件-群成员增加 (Type=103)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype        子类型，1/管理员已同意 2/管理员邀请
     * @param sendTime       发送时间(时间戳)
     * @param fromGroup      来源群号
     * @param fromQQ         操作者QQ(即管理员QQ)
     * @param beingOperateQQ 被操作QQ(即加群的QQ)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int groupMemberIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
        // 这里处理消息
        if (beingOperateQQ == CQ.getLoginQQ()) {
            return MSG_IGNORE;
        }
        groupMemberChangerListener.checkIncrease(subtype, sendTime, fromGroup, fromQQ, beingOperateQQ);
        return MSG_IGNORE;
    }

    /**
     * 好友事件-好友已添加 (Type=201)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype  子类型，目前固定为1
     * @param sendTime 发送时间(时间戳)
     * @param fromQQ   来源QQ
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int friendAdd(int subtype, int sendTime, long fromQQ) {
        // 这里处理消息

        return MSG_IGNORE;
    }

    /**
     * 请求-好友添加 (Type=301)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype      子类型，目前固定为1
     * @param sendTime     发送时间(时间戳)
     * @param fromQQ       来源QQ
     * @param msg          附言
     * @param responseFlag 反馈标识(处理请求用)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int requestAddFriend(int subtype, int sendTime, final long fromQQ, String msg, final String responseFlag) {
        // 这里处理消息
        if (configManager.isNotReplyQQ(fromQQ)) {
            threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						CQ.setFriendAddRequest(responseFlag, REQUEST_REFUSE, "");
						sendMessage(0, 2856986197L, "拒绝了" + fromQQ + "加为好友");
					}
				});
            return MSG_IGNORE;
        }
        /*
         * REQUEST_ADOPT 通过 REQUEST_REFUSE 拒绝
         */
        //    QQInfo qInfo = CQ.getStrangerInfo(fromQQ);
        //    CQ.setFriendAddRequest(responseFlag, REQUEST_ADOPT, qInfo.getNick()); //
        // sendMessage(0, fromQQ, "本体2856986197");
        sendMessage(0, 2856986197L, fromQQ + "把我加为好友");
        // 同意好友添加请求
        return MSG_IGNORE;
    }

    /**
     * 请求-群添加 (Type=302)<br>
     * 本方法会在酷Q【线程】中被调用。<br>
     *
     * @param subtype      子类型，1/他人申请入群 2/自己(即登录号)受邀入群
     * @param sendTime     发送时间(时间戳)
     * @param fromGroup    来源群号
     * @param fromQQ       来源QQ
     * @param msg          附言
     * @param responseFlag 反馈标识(处理请求用)
     * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
     */
    @Override
    public int requestAddGroup(int subtype, int sendTime, final long fromGroup, final long fromQQ, String msg, final String responseFlag) {
        // 这里处理消息

        /*
         * REQUEST_ADOPT 通过 REQUEST_REFUSE 拒绝 REQUEST_GROUP_ADD 群添加
         * REQUEST_GROUP_INVITE 群邀请
         */
        if (subtype == 1) {
            if (configManager.isBlackQQ(fromQQ)) {
                CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_REFUSE, "黑名单用户");
                return MSG_IGNORE;
            }
            PersonInfo personInfo = configManager.getPersonInfoFromQQ(fromQQ);
            if (personInfo != null) {
                CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT, null);
            } else if (configManager.isGroupAutoAllow(fromQQ)) {
                CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT, null);
                sendMessage(fromGroup, 0, "此账号在自动允许列表中，已同意进群");
            } else {
                sendMessage(fromGroup, 0, "有人申请加群，绿帽赶紧瞅瞅");
            }
        } else if (subtype == 2) {
            if (configManager.isBlackQQ(fromQQ) || configManager.isBlackGroup(fromGroup)) {
                threadPool.execute(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_REFUSE, "");
							sendMessage(0, 2856986197L, "拒绝了" + fromQQ + "邀请我加入群" + fromGroup);
						}
					});
                return MSG_IGNORE;
            }
			if (configManager.isMaster(fromQQ) || configManager.containsGroup(fromGroup)) {
				CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);
				sendMessage(0, 2856986197L, "Master" + fromQQ + "邀请我加入群" + fromGroup);			
				return MSG_IGNORE;
			}
            sendMessage(0, 2856986197L, fromQQ + "邀请我加入群" + fromGroup);
        }
        /*
         * if (fromGroup == 859561731L) { // 台长群 return MSG_IGNORE; }
         *
         * if (subtype == 1) { // 本号为群管理，判断是否为他人申请入群 if (fromQQ == 3035936740L |
         * fromQQ == 169901502L | fromQQ == 2963261413L | fromQQ == 946433685L)
         * { CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD,
         * REQUEST_REFUSE, "烧饼禁入"); } else { CQ.setGroupAddRequest(responseFlag,
         * REQUEST_GROUP_ADD, REQUEST_ADOPT, null);// 同意入群
         * sendMessage(fromGroup, 0, "新人的验证信息------\n" + msg); } } else if
         * (subtype == 2) { CQ.setGroupAddRequest(responseFlag,
         * REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);// 同意进受邀群 sendMessage(0,
         * 2856986197L, fromQQ + "邀请我加入群" + fromGroup); }
         */
        return MSG_IGNORE;
    }
    private int sendGroupMessage(long fromGroup, long fromQQ, String msg) {
        if (sleeping) {
            return -1;
        }
        // 处理词库中为特殊消息做的标记
        try {
            if (msg.startsWith("red:")) {
                return -1;
            }
            String[] stri = msg.split(":");
            switch (stri[0]) {
                case "image":
					return CQ.sendGroupMsg(fromGroup, stri[2].replace("--image--", instence.CC.image(new File(appDirectory + stri[1]))));
                case "atFromQQ":
					return CQ.sendGroupMsg(fromGroup, instence.CC.at(fromQQ) + stri[1]);
                case "atQQ":
					return CQ.sendGroupMsg(fromGroup, instence.CC.at(Long.parseLong(stri[1])) + stri[2]);
                case "imageFolder":
					File[] files = (new File(appDirectory + stri[1])).listFiles();
                    return CQ.sendGroupMsg(fromGroup, stri[2].replace("--image--", instence.CC.image((File) Methods.rfa(files))));
                default:
					return CQ.sendGroupMsg(fromGroup, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int sendPrivateMessage(long fromQQ, String msg) {
        if (sleeping) {
            return -1;
        }
        // 处理词库中为特殊消息做的标记
        String[] stri = msg.split(":");
        switch (stri[0]) {
            case "image":
                try {
					return CQ.sendPrivateMsg(fromQQ, stri[2].replace("--image--", instence.CC.image(new File(appDirectory + stri[1]))));
				} catch (IOException e) {}
				return -1;
            case "imageFolder":
                File[] files = (new File(appDirectory + stri[1])).listFiles();
                if (files != null) {
                    try {
						return CQ.sendPrivateMsg(fromQQ, stri[2].replace("--image--", instence.CC.image((File) Methods.rfa(files))));
					} catch (IOException e) {}
                }

                break;
            default:
                return CQ.sendPrivateMsg(fromQQ, msg);
        }
        return -1;
    }

    public void loadGroupDic() {
        dicReplyManager.clear();
        for (GroupConfig groupConfig : configManager.configJavaBean.groupConfigs) {
            if (groupConfig.isDic()) {
                dicReplyManager.addData(new DicReplyGroup(groupConfig.groupNumber));
            }
        }
    }

    public static int sendMessage(long fromGroup, long fromQQ, String msg) {
        return sendMessage(fromGroup, fromQQ, msg, false);
    }

    public static int sendMessage(long fromGroup, long fromQQ, String msg, boolean isTip) {
        int value = -1;
        if (fromGroup == 0 || fromGroup == -1) {
            value = instence.sendPrivateMessage(fromQQ, msg);
        } else {
            if (isTip) {
                value = instence.sendGroupMessage(fromGroup, fromQQ, msg);
            } else {
                if (msg.equals(lastSend) && lastSend.equals(lastSend2)) {
                    if (!tipedBreak) {
                        tipedBreak = true;
                        value = instence.sendGroupMessage(fromGroup, fromQQ, "打断");
                    }
                } else {
                    tipedBreak = false;
                    value = instence.sendGroupMessage(fromGroup, fromQQ, msg);
                }
                lastSend2 = lastSend;
                lastSend = msg;
            }
        }
        return value;
    }
}
