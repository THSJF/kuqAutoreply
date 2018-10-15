package com.meng;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import com.sobte.cqp.jcq.entity.Anonymous;
import com.sobte.cqp.jcq.entity.CQDebug;
import com.sobte.cqp.jcq.entity.GroupFile;
import com.sobte.cqp.jcq.entity.ICQVer;
import com.sobte.cqp.jcq.entity.IMsg;
import com.sobte.cqp.jcq.entity.IRequest;
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
/**
 * @author Administrator
 *
 */
public class Autoreply extends JcqAppAbstract implements ICQVer, IMsg, IRequest {

	public static boolean enable = true;
	public static Random random = new Random();
	private Banner banner = new Banner();
	private RecoderManager recoderManager = new RecoderManager();
	private RollPlane rollPlane = new RollPlane();
	// private LivingManager livingCheck = new LivingManager();
	// private LivingManager livingCheck2 = new LivingManager();
	private ZuiSuJinTianGengLeMa zuiSuJinTianGengLeMa = new ZuiSuJinTianGengLeMa();
	private BilibiliVideoInfo bilibiliTest = new BilibiliVideoInfo();
	private FileTipManager fileTipManager = new FileTipManager();
	private fanpohai fph;
	private DicReplyManager dicReplyManager;

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

		/*
		 * 以下是收尾触发函数 // demo.disable();// 实际过程中程序结束不会触发disable，只有用户关闭了此插件才会触发
		 * demo.exit();// 最后程序运行结束，调用exit方法
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
		fph = new fanpohai();
		addGroupDic();
		addFileTip();
		addRecorder();
		fileTipManager.start();
		// livingCheck();
		// 返回如：D:\CoolQ\app\com.sobte.cqp.jcq\app\com.example.demo\
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
		String[] strings = msg.split("\\.");
		// 转发到指定群
		if (fromQQ == 2856986197L) {
			if (strings[0].equalsIgnoreCase("send")) {
				sendGroupMessage(Long.parseLong(strings[1]), strings[2]);
			}
		}
		/*
		 * if (strings[0].equalsIgnoreCase("live")) { boolean b = false; for
		 * (int i = 0; i < livingCheck.getMapFlag(); i++) {
		 * sendMsg(livingCheck.getPerson(i), Long.parseLong(strings[1])); b = b
		 * || livingCheck.getPerson(i).isLiving(); } for (int i = 0; i <
		 * livingCheck2.getMapFlag(); i++) { sendMsg(livingCheck2.getPerson(i),
		 * Long.parseLong(strings[1])); b = b ||
		 * livingCheck2.getPerson(i).isLiving(); } if (!b) {
		 * sendGroupMessage(Long.parseLong(strings[1]), "惊了 居然没有飞机佬直播"); } }
		 */
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
			CQ.setGroupBan(fromGroup, anonymous.getAid(), 60);
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

		System.out.println(msg);

		if (fromQQ == 2856986197L) {
			String[] strings = msg.split("\\.");
			if (strings[0].equalsIgnoreCase("send")) {
				sendGroupMessage(Long.parseLong(strings[1]), strings[2]);
				return MSG_IGNORE;
			}
		}

		try {
			// 控制
			if (Methods.checkSwitch(fromGroup, msg))
				return MSG_IGNORE;
			// 签到消息
			if (msg.startsWith("[CQ:sign")) {
				sendGroupMessage(fromGroup, "image:pic/qiandao.jpg");
				return MSG_IGNORE;
			}
			// 禁言
			if (banner.checkBan(fromQQ, fromGroup, msg))
				return MSG_IGNORE;
			// 苟
			if (Methods.checkGou(fromGroup, msg))
				return MSG_IGNORE;
			// @
			if (Methods.checkAt(fromGroup, fromQQ, msg))
				return MSG_IGNORE;
			// 膜
			if (Methods.checkMo(fromGroup, msg))
				return MSG_IGNORE;
			// 催更
			if (zuiSuJinTianGengLeMa.check(fromGroup, fromQQ))
				return MSG_IGNORE;
			// 比利比利视频详情
			if (bilibiliTest.check(fromGroup, msg))
				return MSG_IGNORE;
			// 收到的消息有链接
			if (Methods.checkLink(fromGroup, msg))
				return MSG_IGNORE;
			// roll
			if (rollPlane.check(fromGroup, msg))
				return MSG_IGNORE;
			// 根据词库触发回答
			if (dicReplyManager.check(fromGroup, fromQQ, msg))
				return MSG_IGNORE;
			// 反迫害
			if (fph.check(fromQQ, fromGroup, msg))
				return MSG_IGNORE;
			// 复读
			if (recoderManager.check(fromGroup, fromQQ, msg))
				return MSG_IGNORE;
		} catch (Exception e) {
		}
		/*
		 * if (msg.equals(".live")) { boolean b = false; for (int i = 0; i <
		 * livingCheck.getMapFlag(); i++) { sendMsg(livingCheck.getPerson(i),
		 * fromGroup); b = b || livingCheck.getPerson(i).isLiving(); } for (int
		 * i = 0; i < livingCheck2.getMapFlag(); i++) {
		 * sendMsg(livingCheck2.getPerson(i), fromGroup); b = b ||
		 * livingCheck2.getPerson(i).isLiving(); } sendGroupMessage(fromGroup, b
		 * ? "消息发送完毕" : "惊了 居然没有飞机佬直播"); return MSG_IGNORE; }
		 */

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
		fileTipManager.onUploadFile(fromGroup, fromQQ);
		if (fromGroup != 807242547L) {// c5
			sendGroupMessage(fromGroup, "发点小电影啊");
		}

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
		String[] strings = new String[] { "封魔录LNN", "梦时空LNN", "幻想乡LNN", "怪绮谈LNN", "红LNN", "妖LNNN", "永0033", "永0037",
				"花LNN", "风LNN", "殿LNN", "船LNNN", "庙LNNN", "城LNN", "绀LNN", "璋LNNN", "大战争LNN" };
		sendGroupMessage(fromGroup, CC.at(beingOperateQQ) + "你已经是群萌新了，快打个" + Methods.rfa(strings) + "给群友们看看吧");
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

		CQ.setFriendAddRequest(responseFlag, REQUEST_ADOPT, null); //
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
		if (fromGroup == 859561731L) { // 台长群
			return MSG_IGNORE;
		}

		if (subtype == 1) { // 本号为群管理，判断是否为他人申请入群
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT, null);// 同意入群
			sendGroupMessage(fromGroup, "新人的验证信息------\n" + msg);
		} else if (subtype == 2) {
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
			switch (stri[0]) {
			case "image":
				CQ.sendGroupMsg(fromGroup, CC.image(new File(appDirectory + stri[1])));
				break;
			case "atFromQQ":
				CQ.sendGroupMsg(fromGroup, CC.at(fromQQ) + stri[1]);
				break;
			case "atQQ":
				CQ.sendGroupMsg(fromGroup, CC.at(Long.parseLong(stri[1])) + stri[2]);
				break;
			case "imageFolder":
				File[] files = (new File(appDirectory + stri[1])).listFiles();
				CQ.sendGroupMsg(fromGroup, CC.image(files[random.nextInt(files.length)]));
				break;
			default:
				CQ.sendGroupMsg(fromGroup, msg);
				break;
			}
		}
	}

	public static void sendGroupMessage(long fromGroup, String msg) {
		try {
			sendGroupMessage(fromGroup, 0L, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendPrivateMessage(long fromQQ, String msg) {
		CQ.sendPrivateMsg(fromQQ, msg);
	}

	private void addFileTip() {
		fileTipManager.addData(new FileTipUploader(807242547L, 1592608126L));
	}

	private void addGroupDic() {
		dicReplyManager.addData(new DicReplyGroup(826536230L, appDirectory + "dic826536230.json"));// 闲聊
		dicReplyManager.addData(new DicReplyGroup(859561731L, appDirectory + "dic859561731.json"));// 台长
		dicReplyManager.addData(new DicReplyGroup(210341365L, appDirectory + "dic210341365.json"));// 水紫
		dicReplyManager.addData(new DicReplyGroup(348595763L, appDirectory + "dic348595763.json"));// 沙苗
		dicReplyManager.addData(new DicReplyGroup(857548607L, appDirectory + "dic857548607.json"));// 紫苑
		dicReplyManager.addData(new DicReplyGroup(855927922L, appDirectory + "dic855927922.json"));// 最速
		dicReplyManager.addData(new DicReplyGroup(439664871L, appDirectory + "dic439664871.json"));// 妖妖梦
		dicReplyManager.addData(new DicReplyGroup(424838564L, appDirectory + "dic424838564.json"));// 魔道
	}

	/*
	 * private void sendMsg(LivingPerson p, long group) { if (p.isLiving()) {
	 * Autoreply.sendGroupMessage(group, p.getName() + "直播开始啦大家快去奶" +
	 * p.getLiveUrl()); } } private void livingCheck() { livingCheck.addData(new
	 * LivingPerson("芳香直播间", "https://live.bilibili.com/2409909"));
	 * livingCheck.addData(new LivingPerson("水紫",
	 * "https://live.bilibili.com/2803104")); livingCheck.addData(new
	 * LivingPerson("记者", "https://live.bilibili.com/523030"));
	 * livingCheck.addData(new LivingPerson("T丶Reality",
	 * "https://live.bilibili.com/141896")); livingCheck.addData(new
	 * LivingPerson("古明地决", "https://live.bilibili.com/952890"));
	 * livingCheck.addData(new LivingPerson("岁晋芳",
	 * "https://live.bilibili.com/4773795")); livingCheck.addData(new
	 * LivingPerson("懒瘦椰叶", "https://live.bilibili.com/2128637"));
	 * livingCheck.addData(new LivingPerson("天狐Kitsune",
	 * "https://live.bilibili.com/936600")); livingCheck.addData(new
	 * LivingPerson("砂粑粑", "https://live.bilibili.com/11928"));
	 * livingCheck.addData(new LivingPerson("威光椰叶",
	 * "https://live.bilibili.com/1318639")); livingCheck.addData(new
	 * LivingPerson("空格椰叶", "https://live.bilibili.com/75404"));
	 * livingCheck.addData(new LivingPerson("八雲的妖怪闲者",
	 * "https://live.bilibili.com/1954885")); livingCheck.addData(new
	 * LivingPerson("星海天下", "https://live.bilibili.com/359844"));
	 * livingCheck.addData(new LivingPerson("ZRT师傅",
	 * "https://live.bilibili.com/8501850")); livingCheck.addData(new
	 * LivingPerson("绵羊师傅", "https://live.bilibili.com/6683623"));
	 * livingCheck.addData(new LivingPerson("我真的好羡慕你们啊",
	 * "https://live.bilibili.com/5404413")); livingCheck.addData(new
	 * LivingPerson("我叫猫饭", "https://live.bilibili.com/1601397"));
	 * 
	 * livingCheck2.addData(new LivingPerson("Yuriko丶酱",
	 * "https://live.bilibili.com/280476")); livingCheck2.addData(new
	 * LivingPerson("王师傅", "https://live.bilibili.com/8356088"));
	 * livingCheck2.addData(new LivingPerson("黑白の斑點",
	 * "https://live.bilibili.com/1168338")); livingCheck2.addData(new
	 * LivingPerson("一团毛玉", "https://live.bilibili.com/569678"));
	 * livingCheck2.addData(new LivingPerson("莉莉厨一号",
	 * "https://live.bilibili.com/3749309")); livingCheck2.addData(new
	 * LivingPerson("幻想星墨", "https://live.bilibili.com/5198157"));
	 * livingCheck2.addData(new LivingPerson("雾雨沙苗",
	 * "https://live.bilibili.com/5136443")); livingCheck2.addData(new
	 * LivingPerson("假装看风景的露娜厨", "https://live.bilibili.com/1122971"));
	 * livingCheck2.addData(new LivingPerson("TouhouのDean",
	 * "https://live.bilibili.com/1187187")); livingCheck2.addData(new
	 * LivingPerson("夜桜鎮魂歌", "https://live.bilibili.com/475904"));
	 * livingCheck2.addData(new LivingPerson("沙苗",
	 * "https://live.bilibili.com/14238508")); livingCheck2.addData(new
	 * LivingPerson("绿绿可柚", "https://live.bilibili.com/10101916"));
	 * livingCheck2.addData(new LivingPerson("kyoukai_00",
	 * "https://live.bilibili.com/8692789")); livingCheck2.addData(new
	 * LivingPerson("ixix91", "https://live.bilibili.com/12702"));
	 * livingCheck2.addData(new LivingPerson("stg-industry",
	 * "https://live.bilibili.com/3065901")); livingCheck2.addData(new
	 * LivingPerson("佐猫_", "https://live.bilibili.com/272502"));
	 * livingCheck2.addData(new LivingPerson("mengo",
	 * "https://live.bilibili.com/90128")); livingCheck2.addData(new
	 * LivingPerson("西行妖下两世分", "https://live.bilibili.com/528326"));
	 * livingCheck2.addData(new LivingPerson("c5椰叶",
	 * "https://live.bilibili.com/7834477")); livingCheck2.addData(new
	 * LivingPerson("鱼神", "https://live.bilibili.com/14341"));
	 * livingCheck.start(); livingCheck2.start(); }
	 */
	private void addRecorder() {
		recoderManager.addData(new RecordBanner(312342896L, CQ, CC, 1));// 学习
		recoderManager.addData(new RecordBanner(826536230L, CQ, CC));// stg闲聊群
		recoderManager.addData(new RecordBanner(859561731L, CQ, CC, 1));// 台长
		recoderManager.addData(new RecordBanner(348595763L, CQ, CC));// 沙苗
		recoderManager.addData(new RecordBanner(857548607L, CQ, CC));// 紫苑
		recoderManager.addData(new RecordBanner(424838564L, CQ, CC));// 膜道
		recoderManager.addData(new RecordBanner(439664871L, CQ, CC));// 妖妖梦
		recoderManager.addData(new RecordBanner(855927922L, CQ, CC, 1));// 最速
		recoderManager.addData(new RecordBanner(807242547L, CQ, CC, 1));// c5
		// recoderManager.addData(new Recoder(101344113L));// DNF山东二
	}

}
