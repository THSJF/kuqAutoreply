package com.meng;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.meng.barcode.BarcodeManager;
import com.meng.bilibili.BiliLinkInfo;
import com.meng.bilibili.LiveManager;
import com.meng.bilibili.LivePerson;
import com.meng.bilibili.NewUpdateManager;
import com.meng.bilibili.UpperBean;
import com.meng.groupChat.DicReplyGroup;
import com.meng.groupChat.DicReplyManager;
import com.meng.groupChat.RepeaterBanner;
import com.meng.groupChat.RepeaterManager;
import com.meng.groupChat.fanpohai;
import com.meng.searchPicture.PicSearchManager;
import com.meng.tip.FileTipManager;
import com.meng.tip.FileTipUploader;
import com.meng.tip.TimeTip;
import com.meng.tools.Random;
import com.sobte.cqp.jcq.entity.CQImage;
import com.sobte.cqp.jcq.entity.CoolQ;
import com.sobte.cqp.jcq.entity.GroupFile;
import com.sobte.cqp.jcq.entity.ICQVer;
import com.sobte.cqp.jcq.entity.IMsg;
import com.sobte.cqp.jcq.entity.IRequest;
import com.sobte.cqp.jcq.entity.QQInfo;
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
	public static CQCodeCC CC = new CQCodeCC();
	public static UseCount useCount;
	private Banner banner = new Banner();
	private RepeaterManager recoderManager;
	private RollPlane rollPlane = new RollPlane();
	private TimeTip timeTip = new TimeTip();
	private BiliLinkInfo biliLinkInfo = new BiliLinkInfo();
	private FileTipManager fileTipManager;
	private fanpohai fph;
	private static DicReplyManager dicReplyManager;
	private MengAutoReplyConfig mengAutoReplyConfig;
	private CQcodeManager cQcodeManager = new CQcodeManager();
	private NewUpdateManager newUpdate;
	private LiveManager livingManager = new LiveManager();
	private PicSearchManager picSearchManager = new PicSearchManager();
	private BarcodeManager barcodeManager = new BarcodeManager();

	private HashMap<Integer, Long> grg;
	private HashMap<Integer, Long> nrq;
	private HashMap<Integer, String> nrw;

	private HashMap<Long, MessageSender> messageMap = new HashMap<>();

	/**
	 * @param args
	 *            系统参数
	 */
	public static void main(String[] args) {
		// CQ此变量为特殊变量，在JCQ启动时实例化赋值给每个插件，而在测试中可以用CQDebug类来代替他
		CQ = new CoolQ(1000);// new CQDebug("应用目录","应用名称") 可以用此构造器初始化应用的目录
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
		useCount = new UseCount();
		fph = new fanpohai();
		loadConfig();
		addFileTip();
		timeTip.start();
		messageMap.clear();
		Thread messageWatcher = new checkMessageThread();
		messageWatcher.start();
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
		if (fromQQ == 2856986197L) {
			String[] strings = msg.split("\\.");
			if (strings[0].equalsIgnoreCase("send")) {
				sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
			}
		}
		messageMap.put(fromQQ, new MessageSender(0, fromQQ, msg, System.currentTimeMillis()));
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
			// Anonymous anonymous = CQ.getAnonymous(fromAnonymous);
			// CQ.setGroupBan(fromGroup, anonymous.getAid(), 60);
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

		if (Methods.checkSwitch(fromGroup, msg))// 控制
			return MSG_IGNORE;
		if (fromQQ == 2856986197L || fromQQ == 1592608126L) {
			// 手动更新设置，不再需要重启
			if (msg.equalsIgnoreCase("loadConfig")) {
				loadConfig();
				sendMessage(fromGroup, 0, "reload");
				return MSG_IGNORE;
			}
			if (msg.contains("迫害图[CQ:image")) {
				CQImage cqi = CC.getCQImage(msg);
				String pohaituName = msg.substring(0, msg.indexOf("[CQ:image") - 3);
				switch (pohaituName) {
				case "零食":
					msg = "鸽鸽";
					break;
				case "旭东":
					msg = "天星厨";
					break;
				case "杏子":
					msg = "星小渚";
					break;
				}
				try {
					cqi.download(appDirectory + File.separator + "pohai/" + pohaituName,
							"pohaitu" + Autoreply.random.nextInt() + ".png");
					try {
						sendGroupMessage(fromGroup, fromQQ, "新图添加成功");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					try {
						sendGroupMessage(fromGroup, fromQQ, e.toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			if (msg.contains("色图[CQ:image")) {
				CQImage cqi = CC.getCQImage(msg);
				String pohaituName = msg.substring(0, msg.indexOf("[CQ:image") - 2);
				try {
					cqi.download(appDirectory + File.separator + "setu/" + pohaituName,
							"setu" + Autoreply.random.nextInt() + ".png");
					try {
						sendGroupMessage(fromGroup, fromQQ, "新图添加成功");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					try {
						sendGroupMessage(fromGroup, fromQQ, e.toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			if (msg.equals("大膜法 膜神复诵")) {
				new MoShenFuSong(fromGroup).start();
			}
			String[] strings = msg.split("\\.");
			if (strings[0].equalsIgnoreCase("send")) {
				sendMessage(Long.parseLong(strings[1]), 0, strings[2]);
				return MSG_IGNORE;
			}
		}
		if (checkReplyTheGroup(fromGroup)) {
			if (messageMap.get(fromQQ) == null) {
				messageMap.put(fromQQ, new MessageSender(fromGroup, fromQQ, msg, System.currentTimeMillis()));
			} // else if (System.currentTimeMillis() -
				// messageMap.get(fromQQ).getTimeStamp() > 1000) {
				// messageMap.put(fromQQ, new MessageSender(fromGroup, fromQQ,
				// msg, System.currentTimeMillis()));
				// }

		}
		/*
		 * if (msg.equalsIgnoreCase(".live")) { boolean b = false; for (int i =
		 * 0; i < livingManager.getMapFlag(); i++) { LivePerson lp =
		 * livingManager.getPerson(i); if (lp.isLiving()) {
		 * sendGroupMessage(fromGroup, lp.getName() + "直播开始啦大家快去奶" +
		 * lp.getLiveUrl()); } b = b || livingManager.getPerson(i).isLiving(); }
		 * if (!b) { sendGroupMessage(fromGroup, "惊了 居然没有飞机佬直播"); } return
		 * MSG_IGNORE; }
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
		System.out.println(file);
		System.out.println(groupFile.toString());
		if (checkReplyTheGroup(fromGroup)) {
			fileTipManager.onUploadFile(fromGroup, fromQQ);
			if (fromGroup != 807242547L) {// c5
				sendMessage(fromGroup, 0, "发点小电影啊");
			}
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
		if (checkReplyTheGroup(fromGroup)) {
			if (subtype == 1) {
				sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "你绿帽子没莉");
			} else if (subtype == 2) {
				sendMessage(fromGroup, 0, CC.at(beingOperateQQ) + "群主给了你个绿帽子");
			}
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
		if (checkReplyTheGroup(fromGroup)) {
			try {
				QQInfo qInfo = CQ.getStrangerInfo(beingOperateQQ);
				if (subtype == 1) {
					sendMessage(fromGroup, 0, qInfo.getNick() + "(" + qInfo.getQqId() + ")" + "跑莉");
				} else if (subtype == 2) {
					QQInfo qInfo2 = CQ.getStrangerInfo(fromQQ);
					sendMessage(fromGroup, 0, qInfo.getNick() + "(" + qInfo.getQqId() + ")" + "被绿帽" + qInfo2.getNick()
							+ "(" + qInfo2.getQqId() + ")" + "玩完扔莉");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		if (fromQQ == CQ.getLoginQQ()) {
			return MSG_IGNORE;
		}
		try {
			Thread.sleep(random.nextInt(5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendMessage(fromGroup, 0, "欢迎新大佬");

		if (fromGroup == 859561731L) { // 台长群
			sendMessage(859561731L, 0, "芳赛服务器炸了");
			/*
			 * try { sendMessage(859561731L, 0, CC.image(new File(appDirectory +
			 * "pic/sjf9961.jpg"))); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */
		}
		/*
		 * String[] strings = new String[] { "封魔录LNN", "梦时空LNN", "幻想乡LNN",
		 * "怪绮谈LNN", "红LNN", "妖LNNN", "永0033", "永0037", "花LNN", "风LNN", "殿LNN",
		 * "船LNNN", "庙LNNN", "城LNN", "绀LNN", "璋LNNN", "大战争LNN" }; try { if
		 * (beingOperateQQ == CQ.getLoginQQ()) { return MSG_IGNORE; } QQInfo
		 * qInfo = CQ.getStrangerInfo(beingOperateQQ); if (qInfo.getGender() ==
		 * 1) { sendGroupMessage(fromGroup, "新来的小姐姐打个" + Methods.rfa(strings) +
		 * "给这群飞机佬看看吧"); } else { sendGroupMessage(fromGroup,
		 * CC.at(beingOperateQQ) + "你已经是群萌新了，快打个" + Methods.rfa(strings) +
		 * "给群友们看看吧"); } } catch (Exception e) { e.printStackTrace(); }
		 */
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
		QQInfo qInfo = CQ.getStrangerInfo(fromQQ);
		CQ.setFriendAddRequest(responseFlag, REQUEST_ADOPT, qInfo.getNick()); //
		sendMessage(0, fromQQ, "本体2856986197");
		sendMessage(0, 2856986197L, fromQQ + "把我加为好友");
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
			if (fromQQ == 3035936740L | fromQQ == 169901502L | fromQQ == 2963261413L | fromQQ == 946433685L) {
				CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_REFUSE, "烧饼禁入");
			} else {
				CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_ADD, REQUEST_ADOPT, null);// 同意入群
				sendMessage(fromGroup, 0, "新人的验证信息------\n" + msg);
			}
		} else if (subtype == 2) {
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null);// 同意进受邀群
			sendMessage(0, 2856986197L, fromQQ + "邀请我加入群" + fromGroup);
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
			// 处理词库中为特殊消息做的标记
			Methods.setRandomPop();
			if (msg.startsWith("red:")) {
				if (dicReplyManager.check(fromGroup, fromQQ, msg.replace("red:", "")))// 根据词库触发回答
					return;
			}
			String[] stri = msg.split("\\:");
			switch (stri[0]) {
			case "image":
				CQ.sendGroupMsg(fromGroup, stri[2].replace("--image--", CC.image(new File(appDirectory + stri[1]))));
				break;
			case "atFromQQ":
				CQ.sendGroupMsg(fromGroup, CC.at(fromQQ) + stri[1]);
				break;
			case "atQQ":
				CQ.sendGroupMsg(fromGroup, CC.at(Long.parseLong(stri[1])) + stri[2]);
				break;
			case "imageFolder":
				File[] files = (new File(appDirectory + stri[1])).listFiles();
				CQ.sendGroupMsg(fromGroup, stri[2].replace("--image--", CC.image((File) Methods.rfa(files))));
				break;
			default:
				CQ.sendGroupMsg(fromGroup, msg);
				break;
			}
		}
	}

	public static void sendPrivateMessage(long fromQQ, String msg) throws IOException {
		Methods.setRandomPop();
		if (enable) {
			// 处理词库中为特殊消息做的标记
			String[] stri = msg.split("\\:");
			switch (stri[0]) {
			case "image":
				CQ.sendPrivateMsg(fromQQ, stri[2].replace("--image--", CC.image(new File(appDirectory + stri[1]))));
				break;
			case "imageFolder":
				File[] files = (new File(appDirectory + stri[1])).listFiles();
				CQ.sendPrivateMsg(fromQQ, stri[2].replace("--image--", CC.image((File) Methods.rfa(files))));
				break;
			default:
				CQ.sendPrivateMsg(fromQQ, msg);
				break;
			}
		}
	}

	private void addFileTip() {
		fileTipManager = new FileTipManager();
		fileTipManager.addData(new FileTipUploader(807242547L, 1592608126L));
		fileTipManager.start();
	}

	private void loadConfig() {
		mengAutoReplyConfig = new MengAutoReplyConfig();
		grg = mengAutoReplyConfig.getMapGroupReply();
		nrq = mengAutoReplyConfig.getMapQQNotReply();
		nrw = mengAutoReplyConfig.getMapWordNotReply();
		addGroupDic();
		System.out.println("词库回复添加完成");
		addUp();
		System.out.println("催更添加完成");
		addRecorder();
		System.out.println("复读机添加完成");
		addLive();
		System.out.println("直播提醒添加完成");
	}

	private void addGroupDic() {
		HashMap<Integer, Long> gdr = mengAutoReplyConfig.getMapGroupDicReply();
		dicReplyManager = new DicReplyManager();
		for (int key : gdr.keySet()) {// 遍历
			long groupNum = gdr.get(key);
			dicReplyManager.addData(new DicReplyGroup(groupNum, appDirectory + "dic" + groupNum + ".json"));
		}
	}

	private void addRecorder() {
		HashMap<Integer, String> gr = mengAutoReplyConfig.getMapGroupRecorder();
		recoderManager = new RepeaterManager();
		for (int key : gr.keySet()) {// 遍历
			String[] groupCfg = gr.get(key).split("\\.");
			recoderManager.addData(new RepeaterBanner(Long.parseLong(groupCfg[0]), Integer.parseInt(groupCfg[1])));
		}
	}

	private void addUp() {
		HashMap<Integer, UpperBean> bu = mengAutoReplyConfig.getMapBiliUp();
		newUpdate = new NewUpdateManager();
		for (int key : bu.keySet()) {// 遍历
			newUpdate.addData(bu.get(key));
		}
	}

	private void addLive() {
		HashMap<String, String> mlt = mengAutoReplyConfig.getMapLiveTip();
		livingManager = new LiveManager();
		for (String key : mlt.keySet()) {// 遍历
			livingManager.addData(new LivePerson(key, "https://live.bilibili.com/" + mlt.get(key)));
		}
		livingManager.start();
	}

	private boolean checkReplyTheGroup(long fromGroup) {
		for (int key : grg.keySet()) {// 遍历
			if (fromGroup == grg.get(key)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkNotReply(long fromQQ, String msg) {
		for (int key : nrq.keySet()) {
			if (fromQQ == nrq.get(key)) {
				return true;
			}
		}
		for (int key : nrw.keySet()) {
			if (msg.contains(nrw.get(key))) {
				return true;
			}
		}
		return false;
	}

	public class GroupMsgThread extends Thread {
		int subType = 0;
		int msgId = 0;
		long fromGroup = 0;
		long fromQQ = 0;
		String fromAnonymous = "";
		String msg = "";
		int font = 0;
		long timeStamp = 0;

		public GroupMsgThread(int subType, int msgId, long fromGroup, long fromQQ, String fromAnonymous, String msg,
				int font, long timeStamp) {
			this.font = font;
			this.fromAnonymous = fromAnonymous;
			this.fromGroup = fromGroup;
			this.fromQQ = fromQQ;
			this.msg = msg;
			this.msgId = msgId;
			this.subType = subType;
			this.timeStamp = timeStamp;
		}

		public GroupMsgThread(MessageSender ms) {
			font = ms.getFont();
			fromGroup = ms.getFromGroup();
			fromQQ = ms.getFromQQ();
			msg = ms.getMsg();
			msgId = ms.getMsgId();
			subType = ms.getSubType();
			timeStamp = ms.getTimeStamp();
		}

		@Override
		public synchronized void run() {
			check();
		}

		private boolean check() {
			if (msg.equals("-help")) {
				sendMessage(fromGroup, 0, Methods.helpMenu);
				return true;
			}
			if (checkNotReply(fromQQ, msg))// 指定不回复的项目
				return true;
			if (msg.equals("赞我")) {
				for (int i = 0; i < 10; i++) {
					CQ.sendLike(fromQQ);
				}
				sendMessage(fromGroup, 0, "完成");
				return true;
			}
			if (Methods.isPohaitu(fromGroup, fromQQ, msg))
				return true;
			if (Methods.isSetu(fromGroup, fromQQ, msg))
				return true;
			if (barcodeManager.check(fromGroup, fromQQ, msg)) // 二维码
				return true;
			if (picSearchManager.check(fromGroup, fromQQ, msg))// 搜索图片
				return true;
			if (Methods.checkLook(fromGroup, msg))// 窥屏检测
				return true;
			if (biliLinkInfo.check(fromGroup, fromQQ, msg))// 比利比利链接详情
				return true;
			if (cQcodeManager.check(fromGroup, msg))// 特殊信息(签到 分享等)
				return true;
			if (banner.checkBan(fromQQ, fromGroup, msg))// 禁言
				return true;
			if (Methods.checkGou(fromGroup, msg))// 苟
				return true;
			if (Methods.checkMeng2(fromGroup, msg))// 萌2
				return true;
			if (Methods.checkAt(fromGroup, fromQQ, msg))// @
				return true;
			if (timeTip.check(fromGroup, fromQQ))// 根据时间提醒
				return true;
			if (rollPlane.check(fromGroup, msg))// roll
				return true;
			if (fph.check(fromQQ, fromGroup, msg))// 反迫害
				return true;
			if (newUpdate.check(fromGroup, msg))// 催更
				return true;
			if (recoderManager.check(fromGroup, fromQQ, msg))// 复读
				return true;
			if (dicReplyManager.check(fromGroup, fromQQ, msg))// 根据词库触发回答
				return true;
			if (msg.equals("查看统计")) {
				sendMessage(fromGroup, fromQQ, useCount.getMyCount(fromQQ));
				return true;
			}
			if (msg.equals("查看排行")) {
				sendMessage(fromGroup, fromQQ, useCount.getTheFirst(fromQQ));
				return true;
			}
			return false;
		}
	}

	public class PrivateMsgThread extends Thread {
		int subType = 0;
		int msgId = 0;
		long fromQQ = 0;
		String msg = "";
		int font = 0;
		long timeStamp = 0;

		public PrivateMsgThread(int subType, int msgId, long fromQQ, String msg, int font, long timeStamp) {
			this.font = font;
			this.fromQQ = fromQQ;
			this.msg = msg;
			this.msgId = msgId;
			this.subType = subType;
			this.timeStamp = timeStamp;
		}

		public PrivateMsgThread(MessageSender ms) {
			font = ms.getFont();
			fromQQ = ms.getFromQQ();
			msg = ms.getMsg();
			msgId = ms.getMsgId();
			subType = ms.getSubType();
			timeStamp = ms.getTimeStamp();
		}

		@Override
		public synchronized void run() {
			check();
		}

		private boolean check() {
			if (msg.equals("-help")) {
				sendMessage(0, fromQQ, Methods.helpMenu);
				return true;
			}
			if (msg.equals("赞我")) {
				for (int i = 0; i < 10; i++) {
					CQ.sendLike(fromQQ);
				}
				sendMessage(0, fromQQ, "完成");
				return true;
			}
			if (barcodeManager.check(0, fromQQ, msg)) // 二维码解码
				return true;
			if (msg.equals("查看统计")) {
				sendMessage(0, fromQQ, useCount.getMyCount(fromQQ));
				return true;
			}
			if (msg.equals("查看排行")) {
				sendMessage(0, fromQQ, useCount.getTheFirst(fromQQ));
				return true;
			}
			if (msg.equals("色图")) {
				sendMessage(0, fromQQ, "imageFolder:r15/:--image--");
				useCount.incSetu(fromQQ);
				return true;
			}
			if (picSearchManager.check(0, fromQQ, msg))// 搜索图片
				return true;
			return false;
		}
	}

	public static void sendMessage(long fromGroup, long fromQQ, String msg) {
		if (fromGroup == 0) {
			try {
				sendPrivateMessage(fromQQ, msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				sendGroupMessage(fromGroup, fromQQ, msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public class checkMessageThread extends Thread {
		@Override
		public void run() {
			HashMap<Long, MessageSender> delMap = new HashMap<>();
			while (true) {
				for (MessageSender value : messageMap.values()) {
					if (System.currentTimeMillis() - value.getTimeStamp() > 1000) {
						if (value.getFromGroup() == 0) {
							new PrivateMsgThread(value).start();
						} else {
							new GroupMsgThread(value).start();
						}
						delMap.put(value.getFromQQ(), value);
					}
				}
				if (delMap.size() > 0) {
					for (MessageSender value : delMap.values()) {
						messageMap.remove(value.getFromQQ());
					}
				}
				delMap.clear();
				try {
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
