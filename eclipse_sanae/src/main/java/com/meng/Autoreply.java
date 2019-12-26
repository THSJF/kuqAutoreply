package com.meng;

import com.google.gson.*;
import com.meng.config.*;
import com.meng.dice.*;
import com.meng.gameData.TouHou.*;
import com.meng.groupChat.*;
import com.meng.messageProcess.*;
import com.meng.tip.*;
import com.meng.tools.*;
import com.sobte.cqp.jcq.entity.*;
import com.sobte.cqp.jcq.event.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Autoreply extends JcqAppAbstract implements ICQVer, IMsg, IRequest {

    public static Autoreply ins;
    public Random random = new Random();
	public TimeTip timeTip = new TimeTip();
	public CQCodeManager CQcodeManager = new CQCodeManager();
	public AdminMessageProcessor adminMessageProcessor;
    public GroupMemberChangerListener groupMemberChangerListener;
	public SpellCollect spellCollect;
    public ExecutorService threadPool = Executors.newCachedThreadPool();

    public static boolean sleeping = true;

	public static final String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";
    public HashSet<Long> SeijiaInThis = new HashSet<>();
	public FaithManager faithManager;
	public BirthdayTip birthdayTip;

	public DiceImitate diceImitate;
	public static final long mainGroup=807242547L;
	public static Gson gson;
	public MessageTooManyManager messageTooManyManager;
	public DicReply dicReply;
	public GroupCounter groupCounter;
	public GuessSpell guessSpell;

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
        ins = this;
        appDirectory = CQ.getAppDirectory();
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
        // 返回如：D:\CoolQ\app\com.sobte.cqp.jcq\app\com.example.demo\
        System.out.println("开始加载");
		long startTime = System.currentTimeMillis();
		groupCounter = new GroupCounter();
		TouHouDataManager.ins = new TouHouDataManager();
		try {
			ConfigManager.ins = new ConfigManager(new URI("ws://123.207.65.93:9760"));
			ConfigManager.ins.connect();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		guessSpell = new GuessSpell();
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
        if (ConfigManager.ins.isNotReplyQQ(fromQQ) || ConfigManager.ins.isNotReplyWord(msg)) {
            return MSG_IGNORE;
        }
        if (ConfigManager.ins.isMaster(fromQQ)) {
			String[] strings = msg.split("\\.", 3);
			if (strings[0].equals("send")) {
				sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
			}
		}
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
		//	if (fromGroup != 807242547L){
		//		return MSG_IGNORE;
		//}
		groupCounter.addSpeak(fromGroup, 1);
		if (!Autoreply.ins.SeijiaInThis.contains(fromGroup)) {
			ConfigManager.ins.send(SanaeDataPack.encode(SanaeDataPack._15incSpeak).write(fromGroup).write(fromQQ));
		}
		if (messageTooManyManager.checkMsgTooMany(fromGroup, fromQQ, msg)) {
			return MSG_IGNORE;
		}
        if (ConfigManager.ins.isNotReplyQQ(fromQQ)) {
            return MSG_IGNORE;
        }
        if (ConfigManager.ins.isNotReplyWord(msg)) {
            return MSG_IGNORE;
        }
        if (adminMessageProcessor.check(fromGroup, fromQQ, msg)) {
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
		/*    if (subtype == 1) {
		 sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "你绿帽子没莉");
		 } else if (subtype == 2) {
		 sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "群主给了你个绿帽子");
		 }*/
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
        if (ConfigManager.ins.isNotReplyQQ(fromQQ)) {
            CQ.setFriendAddRequest(responseFlag, REQUEST_REFUSE, "");
			sendMessage(0, 2856986197L, "拒绝了" + fromQQ + "加为好友");
            return MSG_IGNORE;
        }
        /*
         * REQUEST_ADOPT 通过 REQUEST_REFUSE 拒绝
         */
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
            if (ConfigManager.ins.isBlackQQ(fromQQ)) {
                CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_REFUSE, "黑名单用户");
				sendMessage(fromGroup, 0, "拒绝了黑名单用户" + fromQQ + "的加群申请");
				return MSG_IGNORE;
            }
			sendMessage(fromGroup, 0, "有人申请加群，管理员快看看吧");
        } else if (subtype == 2) {
            if (ConfigManager.ins.isBlackQQ(fromQQ) || ConfigManager.ins.isBlackGroup(fromGroup)) {
                CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_REFUSE, "");
				sendMessage(0, 2856986197L, "拒绝了" + fromQQ + "邀请我加入群" + fromGroup);
                return MSG_IGNORE;
            }
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);
			sendMessage(0, 2856986197L, fromQQ + "邀请我加入群" + fromGroup);
			dicReply.addReply(fromGroup);
			RepeaterManager.ins.addRepeater(fromGroup);
        }
        return MSG_IGNORE;
    }

    public static int sendMessage(long fromGroup, long fromQQ, String msg) {
		if (sleeping) {
            return -1;
        }
        int value=-1;
        if (fromGroup == 0 || fromGroup == -1) {
            value = CQ.sendPrivateMsg(fromQQ, msg);
        } else {
			value = CQ.sendGroupMsg(fromGroup, msg);
			ins.groupCounter.addSpeak(fromGroup, 1);
        }
        return value;
    }

	public static int sendMessage(long fromGroup, long fromQQ, String[] msg) {
		return sendMessage(fromGroup, fromQQ, (String)Tools.ArrayTool.rfa(msg));
    }

	public static int sendMessage(long fromGroup, long fromQQ, ArrayList<String> msg) {
		return sendMessage(fromGroup, fromQQ, msg.get(ins.random.nextInt(msg.size())));
    }
}
