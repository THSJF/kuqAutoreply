package com.meng;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.sobte.cqp.jcq.entity.Anonymous;
import com.sobte.cqp.jcq.entity.CQDebug;
import com.sobte.cqp.jcq.entity.GroupFile;
import com.sobte.cqp.jcq.entity.ICQVer;
import com.sobte.cqp.jcq.entity.IMsg;
import com.sobte.cqp.jcq.entity.IRequest;
import com.sobte.cqp.jcq.entity.Member;
import com.sobte.cqp.jcq.event.JcqAppAbstract;

/**
 * 本文件是JCQ插件的主类<br>
 * <br>
 * <p>
 * 注意修改json中的class来加载主类，如不设置则利用appid加载，最后一个单词自动大写查找<br>
 * 例：appid(com.example.demo) 则加载类 com.example.Demo<br>
 * 文档地址： https://gitee.com/Sobte/JCQ-CoolQ <br>
 * 帖子：https://cqp.cc/t/37318 <br>
 * 辅助开发变量: {@link JcqAppAbstract#CQ CQ}({@link com.sobte.cqp.jcq.entity.CoolQ
 * 酷Q核心操作类}), {@link JcqAppAbstract#CC CC}(
 * {@link com.sobte.cqp.jcq.message.CQCode 酷Q码操作类}), 具体功能可以查看文档
 */
public class Autoreply extends JcqAppAbstract implements ICQVer, IMsg, IRequest {

	public static boolean enable = true;
	public static Random random = new Random();
	private Banner banner = new Banner(CQ);
	private RecoderManager recoderManager = new RecoderManager();
	private RollPlane rollPlane = new RollPlane();
	private DicReplyManager dicReplyManager;
	private LivingManager lCheckV2 = new LivingManager();
	private fanpohai fph;

	/**
	 * 用main方法调试可以最大化的加快开发效率，检测和定位错误位置<br/>
	 * 以下就是使用Main方法进行测试的一个简易案例
	 *
	 * @param args
	 *            系统参数
	 */
	public static void main(String[] args) {
		// CQ此变量为特殊变量，在JCQ启动时实例化赋值给每个插件，而在测试中可以用CQDebug类来代替他
		CQ = new CQDebug();// new CQDebug("应用目录","应用名称") 可以用此构造器初始化应用的目录
		CQ.logInfo("[JCQ] TEST Demo", "测试启动");// 现在就可以用CQ变量来执行任何想要的操作了
		// 要测试主类就先实例化一个主类对象
		Autoreply demo = new Autoreply();
		// 下面对主类进行各方法测试,按照JCQ运行过程，模拟实际情况
		demo.startup();// 程序运行开始 调用应用初始化方法
		demo.enable();// 程序初始化完成后，启用应用，让应用正常工作
		// 开始模拟发送消息
		// 模拟私聊消息
		// 开始模拟QQ用户发送消息，以下QQ全部编造，请勿添加
		/*
		 * demo.privateMsg(0, 10001, 2234567819L, "小姐姐约吗", 0);
		 * demo.privateMsg(0, 10002, 2222222224L, "喵呜喵呜喵呜", 0);
		 * demo.privateMsg(0, 10003, 2111111334L, "可以给我你的微信吗", 0);
		 * demo.privateMsg(0, 10004, 3111111114L, "今天天气真好", 0);
		 * demo.privateMsg(0, 10005, 3333333334L, "你好坏，都不理我QAQ", 0); // 模拟群聊消息
		 * // 开始模拟群聊消息 demo.groupMsg(0, 10006, 3456789012L, 3333333334L, "",
		 * "菜单", 0); demo.groupMsg(0, 10008, 3456789012L, 11111111114L, "",
		 * "小喵呢，出来玩玩呀", 0); demo.groupMsg(0, 10009, 427984429L, 3333333334L, "",
		 * "[CQ:at,qq=2222222224] 来一起玩游戏，开车开车", 0); demo.groupMsg(0, 10010,
		 * 427984429L, 3333333334L, "", "好久不见啦 [CQ:at,qq=11111111114]", 0);
		 * demo.groupMsg(0, 10011, 427984429L, 11111111114L, "",
		 * "qwq 有没有一起开的\n[CQ:at,qq=3333333334]你玩嘛", 0); // ...... //
		 * 依次类推，可以根据实际情况修改参数，和方法测试效果 // 以下是收尾触发函数 // demo.disable();//
		 * 实际过程中程序结束不会触发disable，只有用户关闭了此插件才会触发 demo.exit();// 最后程序运行结束，调用exit方法
		 */
	}

	public String appInfo() {
		// 应用AppID,规则见 http://d.cqp.me/Pro/开发/基础信息#appid
		String AppID = "com.meng.autoreply";// 记住编译后的文件和json也要使用appid做文件名
		return CQAPIVER + "," + AppID;
	}

	/**
	 * 酷Q启动 (Type=1001)<br>
	 * 本方法会在酷Q【主线程】中被调用。<br>
	 * 请在这里执行插件初始化代码。<br>
	 * 请务必尽快返回本子程序，否则会卡住其他插件以及主程序的加载。
	 *
	 * @return 请固定返回0
	 */
	public int startup() {
		// 获取应用数据目录(无需储存数据时，请将此行注释)
		appDirectory = CQ.getAppDirectory();
		dicReplyManager = new DicReplyManager(appDirectory + "dic.json");
		addGroupDic(appDirectory);
		try {
			fph = new fanpohai(appDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addRecorder();
		livingCheck();
		// 返回如：D:\CoolQ\app\com.sobte.cqp.jcq\app\com.example.demo\
		// 应用的所有数据、配置【必须】存放于此目录，避免给用户带来困扰。
		return 0;
	}

	/**
	 * 酷Q退出 (Type=1002)<br>
	 * 本方法会在酷Q【主线程】中被调用。<br>
	 * 无论本应用是否被启用，本函数都会在酷Q退出前执行一次，请在这里执行插件关闭代码。
	 *
	 * @return 请固定返回0，返回后酷Q将很快关闭，请不要再通过线程等方式执行其他代码。
	 */
	public int exit() {
		return 0;
	}

	/**
	 * 应用已被启用 (Type=1003)<br>
	 * 当应用被启用后，将收到此事件。<br>
	 * 如果酷Q载入时应用已被启用，则在 {@link #startup startup}(Type=1001,酷Q启动)
	 * 被调用后，本函数也将被调用一次。<br>
	 * 如非必要，不建议在这里加载窗口。
	 *
	 * @return 请固定返回0。
	 */
	public int enable() {
		enable = true;
		return 0;
	}

	/**
	 * 应用将被停用 (Type=1004)<br>
	 * 当应用被停用前，将收到此事件。<br>
	 * 如果酷Q载入时应用已被停用，则本函数【不会】被调用。<br>
	 * 无论本应用是否被启用，酷Q关闭前本函数都【不会】被调用。
	 *
	 * @return 请固定返回0。
	 */
	public int disable() {
		enable = false;
		return 0;
	}

	/**
	 * 私聊消息 (Type=21)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subType
	 *            子类型，11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
	 * @param msgId
	 *            消息ID
	 * @param fromQQ
	 *            来源QQ
	 * @param msg
	 *            消息内容
	 * @param font
	 *            字体
	 * @return 返回值*不能*直接返回文本 如果要回复消息，请调用api发送<br>
	 *         这里 返回 {@link IMsg#MSG_INTERCEPT MSG_INTERCEPT} - 截断本条消息，不再继续处理
	 *         <br>
	 *         注意：应用优先级设置为"最高"(10000)时，不得使用本返回值<br>
	 *         如果不回复消息，交由之后的应用/过滤器处理，这里 返回 {@link IMsg#MSG_IGNORE MSG_IGNORE} -
	 *         忽略本条消息
	 */
	public int privateMsg(int subType, int msgId, long fromQQ, String msg, int font) {
		// 这里处理消息

		CQ.sendPrivateMsg(fromQQ, "类型" + subType + "\n内容：" + msg + "\nID：" + msgId + "\n字体：" + font);
		String[] strings = msg.split("\\.");
		if (strings[0].equalsIgnoreCase("send") && fromQQ == 2856986197L) {
			sendGroupMessage(Long.parseLong(strings[1]), strings[2]);
		}

		if (strings[0].equalsIgnoreCase("live")) {
			boolean b = false;
			for (int i = 0; i < lCheckV2.getMapFlag(); i++) {
				sendMsg(lCheckV2.getPerson(i), Long.parseLong(strings[1]));
				b = b || lCheckV2.getPerson(i).isLiving();
			}
			if (!b) {
				sendGroupMessage(Long.parseLong(strings[1]), "惊了 居然没有飞机佬直播");
			}
		}
		return MSG_IGNORE;
	}

	/**
	 * 群消息 (Type=2)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subType
	 *            子类型，目前固定为1
	 * @param msgId
	 *            消息ID
	 * @param fromGroup
	 *            来源群号
	 * @param fromQQ
	 *            来源QQ号
	 * @param fromAnonymous
	 *            来源匿名者
	 * @param msg
	 *            消息内容
	 * @param font
	 *            字体
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int groupMsg(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg,
			int font) {
		// 如果消息来自匿名者
		if (fromQQ == 80000000L && !fromAnonymous.equals("")) {
			// 将匿名用户信息放到 anonymous 变量中
			Anonymous anonymous = CQ.getAnonymous(fromAnonymous);
		}

		// 解析CQ码案例 如：[CQ:at,qq=100000]
		// 解析CQ码 常用变量为 CC(CQCode) 此变量专为CQ码这种特定格式做了解析和封装
		// CC.analysis();// 此方法将CQ码解析为可直接读取的对象
		// 解析消息中的QQID
		// long qqId = CC.getAt(msg);// 此方法为简便方法，获取第一个CQ:at里的QQ号，错误时为：-1000
		// List<Long> qqIds = CC.getAts(msg); // 此方法为获取消息中所有的CQ码对象，错误时返回 已解析的数据
		// 解析消息中的图片
		// CQImage image = CC.getCQImage(msg);//
		// 此方法为简便方法，获取第一个CQ:image里的图片数据，错误时打印异常到控制台，返回 null
		// List<CQImage> images = CC.getCQImages(msg);//
		// 此方法为获取消息中所有的CQ图片数据，错误时打印异常到控制台，返回 已解析的数据

		// 这里处理消息

		// JsonArray array = obj.getAsJsonArray(msg);

		System.out.println(msg);
		try {
			if (MainSwitch.checkSwitch(fromGroup, msg))
				return MSG_IGNORE;
			if (MainSwitch.checkMo(fromGroup, msg))
				return MSG_IGNORE;
			if (MainSwitch.checkAt(fromGroup, fromQQ, msg, CC))
				return MSG_IGNORE;
			if (MainSwitch.checkMo(fromGroup, msg, CC, appDirectory))
				return MSG_IGNORE;
			if (MainSwitch.checkLink(fromGroup, msg))
				return MSG_IGNORE;
			if (rollPlane.check(fromGroup, msg))
				return MSG_IGNORE;
			if (banner.checkBan(fromQQ, fromGroup, msg))
				return MSG_IGNORE;
			if (recoderManager.check(fromGroup, msg, CC, appDirectory))
				return MSG_IGNORE;
			if (fph.check(fromQQ, fromGroup, msg, appDirectory))
				return MSG_IGNORE;
			if (dicReplyManager.check(fromGroup, fromQQ, msg))
				return MSG_IGNORE;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (msg.equals(".live")) {
			boolean b = false;
			for (int i = 0; i < lCheckV2.getMapFlag(); i++) {
				sendMsg(lCheckV2.getPerson(i), fromGroup);
				b = b || lCheckV2.getPerson(i).isLiving();
			}
			sendGroupMessage(fromGroup, b ? "消息发送完毕" : "惊了 居然没有飞机佬直播");
			return MSG_IGNORE;
		}
		return MSG_IGNORE;

	}

	/**
	 * 讨论组消息 (Type=4)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，目前固定为1
	 * @param msgId
	 *            消息ID
	 * @param fromDiscuss
	 *            来源讨论组
	 * @param fromQQ
	 *            来源QQ号
	 * @param msg
	 *            消息内容
	 * @param font
	 *            字体
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int discussMsg(int subtype, int msgId, long fromDiscuss, long fromQQ, String msg, int font) {
		// 这里处理消息

		return MSG_IGNORE;
	}

	/**
	 * 群文件上传事件 (Type=11)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subType
	 *            子类型，目前固定为1
	 * @param sendTime
	 *            发送时间(时间戳)// 10位时间戳
	 * @param fromGroup
	 *            来源群号
	 * @param fromQQ
	 *            来源QQ号
	 * @param file
	 *            上传文件信息
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int groupUpload(int subType, int sendTime, long fromGroup, long fromQQ, String file) {
		GroupFile groupFile = CQ.getGroupFile(file);
		if (groupFile == null) { // 解析群文件信息，如果失败直接忽略该消息
			return MSG_IGNORE;
		}
		// 这里处理消息
		sendGroupMessage(fromGroup, "发点小电影啊");
		return MSG_IGNORE;
	}

	/**
	 * 群事件-管理员变动 (Type=101)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，1/被取消管理员 2/被设置管理员
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromGroup
	 *            来源群号
	 * @param beingOperateQQ
	 *            被操作QQ
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int groupAdmin(int subtype, int sendTime, long fromGroup, long beingOperateQQ) {
		// 这里处理消息
		if (subtype == 1) {
			sendGroupMessage(fromGroup, CC.at(beingOperateQQ) + "你绿帽子没莉");
		} else if (subtype == 2) {
			sendGroupMessage(fromGroup, CC.at(beingOperateQQ) + "群主给了你个绿帽子");
		}
		return MSG_IGNORE;
	}

	/**
	 * 群事件-群成员减少 (Type=102)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，1/群员离开 2/群员被踢
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromGroup
	 *            来源群号
	 * @param fromQQ
	 *            操作者QQ(仅子类型为2时存在)
	 * @param beingOperateQQ
	 *            被操作QQ
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int groupMemberDecrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
		// 这里处理消息
		if (beingOperateQQ == 2856986197L) {
			CQ.setGroupLeave(fromGroup, false);
			return MSG_IGNORE;
		}
		if (subtype == 1) {
			sendGroupMessage(fromGroup, beingOperateQQ + "跑莉");
		} else if (subtype == 2) {
			sendGroupMessage(fromGroup, beingOperateQQ + "被玩完扔莉");
		}

		return MSG_IGNORE;
	}

	/**
	 * 群事件-群成员增加 (Type=103)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，1/管理员已同意 2/管理员邀请
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromGroup
	 *            来源群号
	 * @param fromQQ
	 *            操作者QQ(即管理员QQ)
	 * @param beingOperateQQ
	 *            被操作QQ(即加群的QQ)
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int groupMemberIncrease(int subtype, int sendTime, long fromGroup, long fromQQ, long beingOperateQQ) {
		// 这里处理消息
		// if (fromGroup == 101344113L) {
		// sendGroupMessage(fromGroup, CC.at(beingOperateQQ) +
		// "你已经是群萌新了，快亮出你的300亿二觉吧");
		// } else {
		String[] strings = new String[] { "封魔录", "梦时空", "幻想乡", "怪绮谈", "红", "妖", "永", "花", "风", "殿", "船", "庙", "城", "绀",
				"璋", "大战争", };
		sendGroupMessage(fromGroup,
				CC.at(beingOperateQQ) + "你已经是群萌新了，快打个" + strings[random.nextInt(strings.length)] + "LNN给群友们看看吧");
		// }
		return MSG_IGNORE;
	}

	/**
	 * 好友事件-好友已添加 (Type=201)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，目前固定为1
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromQQ
	 *            来源QQ
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int friendAdd(int subtype, int sendTime, long fromQQ) {
		// 这里处理消息

		return MSG_IGNORE;
	}

	/**
	 * 请求-好友添加 (Type=301)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，目前固定为1
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromQQ
	 *            来源QQ
	 * @param msg
	 *            附言
	 * @param responseFlag
	 *            反馈标识(处理请求用)
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int requestAddFriend(int subtype, int sendTime, long fromQQ, String msg, String responseFlag) {
		// 这里处理消息

		/**
		 * REQUEST_ADOPT 通过 REQUEST_REFUSE 拒绝
		 */

		// CQ.setFriendAddRequest(responseFlag, REQUEST_ADOPT, null); //
		// 同意好友添加请求
		return MSG_IGNORE;
	}

	/**
	 * 请求-群添加 (Type=302)<br>
	 * 本方法会在酷Q【线程】中被调用。<br>
	 *
	 * @param subtype
	 *            子类型，1/他人申请入群 2/自己(即登录号)受邀入群
	 * @param sendTime
	 *            发送时间(时间戳)
	 * @param fromGroup
	 *            来源群号
	 * @param fromQQ
	 *            来源QQ
	 * @param msg
	 *            附言
	 * @param responseFlag
	 *            反馈标识(处理请求用)
	 * @return 关于返回值说明, 见 {@link #privateMsg 私聊消息} 的方法
	 */
	public int requestAddGroup(int subtype, int sendTime, long fromGroup, long fromQQ, String msg,
			String responseFlag) {
		// 这里处理消息

		/**
		 * REQUEST_ADOPT 通过 REQUEST_REFUSE 拒绝 REQUEST_GROUP_ADD 群添加
		 * REQUEST_GROUP_INVITE 群邀请
		 */
		/*
		 * if(subtype == 1){ // 本号为群管理，判断是否为他人申请入群
		 * CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT,
		 * null);// 同意入群 }
		 */
		if (subtype == 2) {
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);// 同意进受邀群
		}

		return MSG_IGNORE;
	}

	/**
	 * 本函数会在JCQ【线程】中被调用。
	 *
	 * @return 固定返回0
	 */
	public int menuA() {
		JOptionPane.showMessageDialog(null, "这是测试菜单A，可以在这里加载窗口");
		return 0;
	}

	/**
	 * 本函数会在酷Q【线程】中被调用。
	 *
	 * @return 固定返回0
	 */
	public int menuB() {
		JOptionPane.showMessageDialog(null, "这是测试菜单B，可以在这里加载窗口");
		return 0;
	}

	public static void sendGroupMessage(long fromGroup, long fromQQ, String msg) throws IOException {
		if (enable) {
			String[] stri = msg.split("\\:");
			if (msg.startsWith("image:")) {
				CQ.sendGroupMsg(fromGroup, CC.image(new File(appDirectory + stri[1])));
			} else if (msg.startsWith("atFromQQ:")) {
				CQ.sendGroupMsg(fromGroup, CC.at(fromQQ) + stri[1]);
			} else if (msg.startsWith("atQQ:")) {
				CQ.sendGroupMsg(fromGroup, CC.at(Long.parseLong(stri[1])) + stri[2]);
			} else if (msg.startsWith("imageFolder:")) {
				File fo = new File(appDirectory + stri[1]);
				File[] files = fo.listFiles();
				CQ.sendGroupMsg(fromGroup, CC.image(files[random.nextInt(files.length)]));
			} else {
				CQ.sendGroupMsg(fromGroup, msg);
			}

		}
	}

	public static void sendGroupMessage(long fromGroup, String msg) {
		try {
			sendGroupMessage(fromGroup, 0L, msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsg(LivingPerson p, long group) {
		if (p.isLiving()) {
			String tmp = p.getName() + "直播开始啦大家快去奶" + p.getLiveUrl();
			Autoreply.sendGroupMessage(group, tmp);
		}
	}

	private void addGroupDic(String appDirectory) {
		dicReplyManager.addData(new DicReplyGroup(826536230L, appDirectory + "dic826536230.json"));
		dicReplyManager.addData(new DicReplyGroup(859561731L, appDirectory + "dic859561731.json"));
		dicReplyManager.addData(new DicReplyGroup(210341365L, appDirectory + "dic210341365.json"));
		dicReplyManager.addData(new DicReplyGroup(348595763L, appDirectory + "dic348595763.json"));
		dicReplyManager.addData(new DicReplyGroup(857548607L, appDirectory + "dic857548607.json"));
		// dicReplyManager.addData(new DicReplyGroup(594237002L, appDirectory +
		// "dic594237002.json"));
	}

	private void livingCheck() {
		lCheckV2.addData(new LivingPerson("芳香直播间", "https://live.bilibili.com/2409909"));
		lCheckV2.addData(new LivingPerson("水紫", "https://live.bilibili.com/2803104"));
		lCheckV2.addData(new LivingPerson("记者", "https://live.bilibili.com/523030"));
		lCheckV2.addData(new LivingPerson("古明地决", "https://live.bilibili.com/952890"));
		lCheckV2.addData(new LivingPerson("岁晋芳", "https://live.bilibili.com/4773795"));
		lCheckV2.addData(new LivingPerson("懒瘦椰叶", "https://live.bilibili.com/2128637"));
		lCheckV2.addData(new LivingPerson("天狐Kitsune", "https://live.bilibili.com/936600"));
		lCheckV2.addData(new LivingPerson("砂粑粑", "https://live.bilibili.com/11928"));
		lCheckV2.addData(new LivingPerson("威光椰叶", "https://live.bilibili.com/1318639"));
		lCheckV2.addData(new LivingPerson("空格椰叶", "https://live.bilibili.com/75404"));
		lCheckV2.addData(new LivingPerson("八雲的妖怪闲者", "https://live.bilibili.com/1954885"));
		lCheckV2.addData(new LivingPerson("星海天下", "https://live.bilibili.com/359844"));
		lCheckV2.addData(new LivingPerson("ZRT师傅", "https://live.bilibili.com/8501850"));
		lCheckV2.addData(new LivingPerson("绵羊师傅", "https://live.bilibili.com/6683623"));
		lCheckV2.addData(new LivingPerson("我真的好羡慕你们啊", "https://live.bilibili.com/5404413"));
		lCheckV2.addData(new LivingPerson("Yuriko丶酱", "https://live.bilibili.com/280476"));
		lCheckV2.addData(new LivingPerson("王师傅", "https://live.bilibili.com/8356088"));
		lCheckV2.addData(new LivingPerson("黑白の斑點", "https://live.bilibili.com/1168338"));
		lCheckV2.addData(new LivingPerson("一团毛玉", "https://live.bilibili.com/569678"));
		lCheckV2.addData(new LivingPerson("莉莉厨一号", "https://live.bilibili.com/3749309"));
		lCheckV2.addData(new LivingPerson("幻想星墨", "https://live.bilibili.com/5198157"));
		lCheckV2.addData(new LivingPerson("雾雨沙苗", "https://live.bilibili.com/5136443"));
		lCheckV2.addData(new LivingPerson("假装看风景的露娜厨", "https://live.bilibili.com/1122971"));
		lCheckV2.addData(new LivingPerson("TouhouのDean", "https://live.bilibili.com/1187187"));
		lCheckV2.addData(new LivingPerson("夜桜鎮魂歌", "https://live.bilibili.com/475904"));
		lCheckV2.addData(new LivingPerson("沙苗", "https://live.bilibili.com/14238508"));
		lCheckV2.addData(new LivingPerson("绿绿可柚", "https://live.bilibili.com/10101916"));
		lCheckV2.addData(new LivingPerson("kyoukai_00", "https://live.bilibili.com/8692789"));
		lCheckV2.addData(new LivingPerson("ixix91", "https://live.bilibili.com/12702"));
		lCheckV2.addData(new LivingPerson("stg-industry", "https://live.bilibili.com/3065901"));
		lCheckV2.addData(new LivingPerson("佐猫_", "https://live.bilibili.com/272502"));
		lCheckV2.addData(new LivingPerson("mengo", "https://live.bilibili.com/90128"));
		lCheckV2.start();
	}

	private void addRecorder() {
		recoderManager.addData(new Recoder(210341365L));// 水紫
		recoderManager.addData(new Recoder(312342896L));// 学习
		recoderManager.addData(new Recoder(826536230L));// stg闲聊群
		recoderManager.addData(new Recoder(859561731L));// 东芳直播间
		recoderManager.addData(new Recoder(348595763L));// 沙苗のSTG群
		recoderManager.addData(new Recoder(857548607L));// 恋萌萌粉丝群
		recoderManager.addData(new Recoder(424838564L));// 膜道
		// recoderManager.addData(new Recoder(101344113L));// DNF山东二
	}

}
