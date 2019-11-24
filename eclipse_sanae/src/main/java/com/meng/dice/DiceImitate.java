package com.meng.dice;
import com.meng.*;
import com.meng.tools.*;
import java.util.*;

public class DiceImitate {
	public static String[] spells;
	public static String[] lastword;
	public static String[] overdrive;
	public static String[] neta;
	public static String[] music;
	public static String[] name;

	private static String[] pl01 = new String[]{"别打砖块了，来飞机"};
	private static String[] pl02 = new String[]{"范围重视型", "高灵击伤害 平衡型", "威力重视型"};
    private static String[] pl03 = new String[]{"博丽灵梦", "魅魔", "雾雨魔理沙", "爱莲", "小兔姬", "卡娜·安娜贝拉尔", "朝仓理香子", "北白河千百合", "冈崎梦美"};
    private static String[] pl04 = new String[]{"博丽灵梦 诱导", "博丽灵梦 大范围", "雾雨魔理沙 激光", "雾雨魔理沙 高速射击"};
    private static String[] pl05 = new String[]{"博丽灵梦", "雾雨魔理沙", "魅魔", "幽香"};
    private static String[] pl06 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符"};
    private static String[] pl07 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符", "十六夜咲夜 幻符", "十六夜咲夜 时符"};
    private static String[] pl08 = new String[]{"幻想的结界组", "咏唱禁咒组", "梦幻的红魔组", "幽冥之住人组", "博丽灵梦", "八云紫", "雾雨魔理沙", "爱丽丝·玛格特罗依德", "蕾米莉亚·斯卡蕾特", "十六夜咲夜", "西行寺幽幽子", "魂魄妖梦"};
    private static String[] pl09 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜", "魂魄妖梦", "铃仙·优昙华院·因幡", "琪露诺", "莉莉卡·普莉兹姆利巴", "梅露兰·普莉兹姆利巴", "露娜萨·普莉兹姆利巴", "米斯蒂娅·萝蕾拉", "因幡帝", "射命丸文", "梅蒂欣·梅兰可莉", "风见幽香", "小野冢小町", "四季映姬·亚玛萨那度"};
    private static String[] pl10 = new String[]{"博丽灵梦 诱导装备", "博丽灵梦 前方集中装备", "博丽灵梦 封印装备", "雾雨魔理沙 高威力装备", "雾雨魔理沙 贯通装备", "雾雨魔理沙 魔法使装备"};
    private static String[] pl11 = new String[]{"博丽灵梦 八云紫", "博丽灵梦 伊吹萃香", "博丽灵梦 射命丸文", "雾雨魔理沙 爱丽丝·玛格特罗依德", "雾雨魔理沙 帕秋莉", "雾雨魔理沙 河城荷取"};
    private static String[] pl12 = new String[]{"博丽灵梦 诱导型", "博丽灵梦 威力重视型", "雾雨魔理沙 贯通型", "雾雨魔理沙 范围重视型", "东风谷早苗 诱导型", "东风谷早苗 广范围炸裂型"};
    private static String[] pl13 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "魂魄妖梦"};
    private static String[] pl14 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜"};
    private static String[] pl14s = new String[]{"使用妖器", "不使用妖器"};
    private static String[] pl15 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "铃仙·优昙华院·因幡"};
    private static String[] pl16 = new String[]{"博丽灵梦", "琪露诺", "射命丸文", "雾雨魔理沙"};
    private static String[] pl16s = new String[]{"春", "夏", "秋", "冬"};
    private static String[] pl17 = new String[]{"博丽灵梦", "雾雨魔理沙", "魂魄妖梦"};
    private static String[] pl17s = new String[]{"[CQ:emoji,id=128059]哥", "[CQ:emoji,id=128037]哥", "[CQ:emoji,id=128054]哥"};
	private static String[] plDiff = new String[]{"easy", "normal", "hard", "lunatic"};

	public static HashSet<String> sp6=new HashSet<>();
	public static HashSet<String> sp7=new HashSet<>();
	public static HashSet<String> sp8=new HashSet<>();
	public static HashSet<String> sp10=new HashSet<>();
	public static HashSet<String> sp11=new HashSet<>();
	public static HashSet<String> sp12=new HashSet<>();
	public static HashSet<String> sp13=new HashSet<>();
	public static HashSet<String> sp14=new HashSet<>();
	public static HashSet<String> sp15=new HashSet<>();
	public static HashSet<String> sp16=new HashSet<>();
	public static HashSet<String> sp17=new HashSet<>();

	public static HashSet<String> cat=new HashSet<>();
	public static HashSet<String> memory=new HashSet<>();
	public static HashSet<String> pachouli=new HashSet<>();


	public DiceImitate() {
		lastword = new String[]{
			"「不合时令的蝶雨」",
			"「失明的夜雀」",
			"「日出之国的天子」",
			"「远古的骗术」",
			"「月的红眼」",
			"「天网蛛网捕蝶之法」",
			"「蓬莱的树海」",
			"「不死鸟重生」",
			"「似有似无的净化」",
			"「梦想天生」",
			"「彗星」",
			"「收缩的世界」",
			"「待宵反射卫星斩」",
			"「猎奇剧团的怪人」",
			"「绯红的宿命」",
			"「西行寺无余涅槃」",
			"「深弹幕结界 梦幻泡影」"
		};
		overdrive = new String[]{
			"樱符「樱吹雪地狱」",
			"山彦「山彦的发挥本领之回音」",
			"毒爪「不死的杀人鬼」",
			"道符「道胎动 ~道~」",
			"怨灵「入鹿之雷」",
			"圣童女「太阳神的贡品」",
			"「神灵大宇宙」",
			"「Wild Carpet」"
		};
		spells = new String[]{
			//th6
			"月符「月光」",
			"夜符「夜雀」",
			"暗符「境界线」",

			"冰符「冰瀑」",
			"雹符「冰雹暴风」",
			"冻符「完美冻结」",
			"雪符「钻石风暴」",

			"华符「芳华绚烂」",
			"华符「Selaginella 9」",
			"虹符「彩虹风铃」",
			"幻符「华想梦葛」",
			"彩符「彩雨」",
			"彩符「彩光乱舞」",
			"彩符「极彩台风」",

			"火符「火神之光」",
			"水符「水精公主」",
			"木符「风灵的角笛」",
			"土符「慵懒三石塔」",
			"金符「金属疲劳」",
			"火符「火神之光 上级」",
			"木符「风灵的角笛 上级」",
			"土符「慵懒三石塔 上级」",
			"火符「火神之光」",
			"水符「湖葬」",
			"木符「翠绿风暴」",
			"土符「三石塔的震动」",
			"金符「银龙」",
			"火&土符「环状熔岩带」",
			"木&火符「森林大火」",
			"水&木符「水精灵」",
			"金&水符「水银之毒」",
			"土&金符「翡翠巨石」",

			"奇术「误导」",
			"奇术「幻惑误导」",
			"幻在「钟表的残骸」",
			"幻幽「迷幻杰克」",
			"幻象「月神之钟」",
			"幻世「世界」",
			"女仆秘技「操弄玩偶」",
			"女仆秘技「杀人玩偶」",

			"奇术「永恒的温柔」",

			"天罚「大卫之星」",
			"神罚「年幼的恶魔之王」",
			"冥符「红色的冥界」",
			"狱符「千根针的针山」",
			"诅咒「弗拉德·特佩斯的诅咒」",
			"神术「吸血鬼幻想」",
			"红符「绯红射击」",
			"红符「绯红之主」",
			"「Red Magic」",
			"「红色的幻想乡」",		

			"月符「静息的月神」",
			"日符「皇家烈焰」",
			"火水木金土符「贤者之石」",

			"禁忌「红莓陷阱」",
			"禁忌「莱瓦汀」",
			"禁忌「四重存在」",
			"禁忌「笼中鸟」",
			"禁忌「恋之迷宫」",
			"禁弹「星弧破碎」",
			"禁弹「折反射」",
			"禁弹「刻着过去的钟表」",
			"秘弹「之后就一个人都没有了吗？」",
			"QED「495年的波纹」",
			//th7
			"冰符「冰袭方阵」",

			"寒符「延长的冬日」",
			"冬符「花之凋零」",
			"白符「波状光」",
			"怪符「桌灵转」",

			"仙符「凤凰卵」",	
			"仙符「凤凰展翅」",	
			"式符「飞翔晴明」",
			"阴阳「道满晴明」",	
			"阴阳「晴明大纹」",
			"天符「天仙鸣动」",
			"翔符「飞翔韦驮天」",
			"童符「护法天童乱舞」",
			"仙符「尸解永远」",
			"鬼符「鬼门金神」",
			"方符「奇门遁甲」",	

			"操符「少女文乐」",
			"苍符「博爱的法兰西人偶」",
			"苍符「博爱的奥尔良人偶」",
			"红符「红发的荷兰人偶」",
			"白符「白垩的俄罗斯人偶」",
			"暗符「雾之伦敦人偶」",
			"回符「轮回的西藏人偶」",
			"雅符「春之京人偶」",
			"诅咒「魔彩光的上海人偶」",
			"诅咒「上吊的蓬莱人偶」",

			"弦奏「Guarneri del Gesù」",
			"神弦「Stradivarius」",
			"伪弦「Pseudo Stradivarius」",

			"管灵「日野幻想」",
			"冥管「灵之克里福德」",
			"管灵「灵之克里福德」",

			"冥键「法吉奥里冥奏」",
			"键灵「蓓森朵芙神奏」",

			"骚符「幽灵絮语」",
			"骚符「活着的骚灵」",
			"合葬「棱镜协奏曲」",
			"骚葬「冥河边缘」",
			"大合葬「灵车大协奏曲」",
			"大合葬「灵车大协奏曲改」",
			"大合葬「灵车大协奏曲怪」",

			"幽鬼剑「妖童饿鬼之断食」",
			"饿鬼剑「饿鬼道草纸」",
			"饿王剑「饿鬼十王的报应」",
			"狱界剑「二百由旬之一闪」",
			"狱炎剑「业风闪影阵」",
			"狱神剑「业风神闪斩」",
			"畜趣剑「无为无策之冥罚」",
			"修罗剑「现世妄执」",
			"人界剑「悟入幻想」",
			"人世剑「大悟显晦」",
			"人神剑「俗谛常住」",
			"天上剑「天人之五衰」",
			"天界剑「七魄忌讳」",
			"天神剑「三魂七魄」",

			"六道剑「一念无量劫」",
			"亡乡「亡我乡 -彷徨的灵魂-」",
			"亡乡「亡我乡 -宿罪-」",
			"亡乡「亡我乡 -无道之路-」",
			"亡乡「亡我乡 -自尽-」",
			"亡舞「生者必灭之理 -眩惑-」",
			"亡舞「生者必灭之理 -死蝶-」",
			"亡舞「生者必灭之理 -毒蛾-」",
			"亡舞「生者必灭之理 -魔境-」",
			"华灵「死蝶」",
			"华灵「燕尾蝶」",
			"华灵「深固难徙之蝶」",
			"华灵「蝶幻」",
			"幽曲「埋骨于弘川 -伪灵-」",
			"幽曲「埋骨于弘川 -亡灵-」",
			"幽曲「埋骨于弘川 -幻灵-」",
			"幽曲「埋骨于弘川 -神灵-」",
			"樱符「完全墨染的樱花 -封印-」",
			"樱符「完全墨染的樱花 -亡我-」",
			"樱符「完全墨染的樱花 -春眠-」",
			"樱符「完全墨染的樱花 -开花-」",
			"「反魂蝶 -一分咲-」",
			"「反魂蝶 -三分咲-」",
			"「反魂蝶 -五分咲-」",
			"「反魂蝶 -八分咲-」",

			"鬼符「青鬼赤鬼」",
			"鬼神「飞翔毘沙门天」",		
			"式神「仙狐思念」",
			"式神「十二神将之宴」",
			"式辉「狐狸妖怪激光」",
			"式辉「迷人的四面楚歌」",
			"式辉「天狐公主 -Illusion-」",
			"式弹「往生极乐的佛教徒」",
			"式弹「片面义务契约」",
			"式神「橙」",
			"「狐狗狸的契约」",
			"幻神「饭纲权现降临」",

			"式神「前鬼后鬼的守护」",	
			"式神「凭依荼吉尼天」",
			"结界「梦境与现实的诅咒」",
			"结界「动与静的均衡」",
			"结界「光与暗的网孔」",
			"罔两「直与曲的梦乡」",
			"罔两「八云紫的神隐」",
			"罔两「栖息于禅寺的妖蝶」",
			"魍魉「二重黑死蝶」",
			"式神「八云蓝」",
			"「人与妖的境界」",
			"结界「生与死的境界」",
			"紫奥义「弹幕结界」",
			//th8
			"萤符「地上的流星」",
			"萤符「地上的彗星」",
			"灯符「萤光现象」",
			"蠢符「小虫」",
			"蠢符「小虫风暴」",
			"蠢符「夜虫风暴」",
			"蠢符「夜虫龙卷」",

			"声符「枭的夜鸣声」",
			"声符「木菟的咆哮」",
			"蛾符「天蛾的蛊道」",
			"毒符「毒蛾的鳞粉」",
			"猛毒「毒蛾的黑暗演舞」",
			"鹰符「祸延疾冲」",
			"夜盲「夜雀之歌」",

			"产灵「最初的金字塔」",
			"始符「短暂的137」",
			"野符「武烈的危机」",
			"野符「将门的危机」",
			"野符「义满的危机」",
			"野符「GHQ的危机」",
			"国符「三种神器 剑」",
			"国符「三种神器 玉」",
			"国符「三种神器 镜」",
			"国体「三种神器 乡」",
			"终符「幻想天皇」",
			"虚史「幻想乡传说」",

			"梦符「二重结界」",
			"梦境「二重大结界」",
			"灵符「梦想封印 散」",
			"散灵「梦想封印 寂」",
			"梦符「封魔阵」",
			"神技「八方鬼缚阵」",
			"神技「八方龙杀阵」",
			"灵符「梦想封印 集」",
			"回灵「梦想封印 侘」",
			"境界「二重弹幕结界」",
			"大结界「博丽弹幕结界」",

			"魔符「银河」",
			"魔空「小行星带」",
			"魔符「星尘幻想」",
			"黑魔「黑洞边缘」",
			"恋符「非定向光线」",
			"恋风「星光台风」",
			"恋符「极限火花」",
			"恋心「二重火花」",
			"光符「地球光」",
			"光击「击月」",

			"波符「赤眼催眠(Mind Shaker)」",
			"幻波「赤眼催眠(Mind Blowing)」",
			"狂符「幻视调律(Visionary Tuning)」",
			"狂视「狂视调律(Illusion Seeker)」",
			"懒符「生神停止(Idling Wave)」",
			"懒惰「生神停止(Mind Stopper)」",
			"散符「真实之月(Invisible Full Moon)」",

			"天丸「壶中的天地」",
			"觉神「神代的记忆」",
			"神符「天人的族谱」",
			"苏活「生命游戏 -Life Game-」",
			"苏生「Rising Game」",
			"操神「思兼装置」",
			"神脑「思兼的头脑」",
			"天咒「阿波罗13」",
			"秘术「天文密葬法」",

			"药符「壶中的大银河」",

			"难题「龙颈之玉 -五色的弹丸-」",
			"神宝「耀眼的龙玉」",
			"难题「佛御石之钵 -不碎的意志-」",
			"神宝「佛体的金刚石」",
			"难题「火鼠的皮衣 -不焦躁的内心-」",
			"神宝「火蜥蜴之盾」",
			"难题「燕的子安贝 -永命线-」",
			"神宝「无限的生命之泉」",
			"难题「蓬莱的弹枝 -七色的弹幕-」",
			"神宝「蓬莱的玉枝 -梦色之乡-」",

			"旧史「旧秘境史 -古代史-」",
			"转世「一条归桥」",
			"新史「新幻想史 -现代史-」",

			"时效「月岩笠的诅咒」",
			"不死「火鸟 -凤翼天翔-」",
			"藤原「灭罪寺院伤」",
			"不死「徐福时空」",
			"灭罪「正直者之死」",
			"虚人「无一」",
			"不灭「不死鸟之尾」",
			"蓬莱「凯风快晴 -Fujiyama Volcano-」",
			"「不死鸟附体」",
			"「蓬莱人形」",
			//th10
			"叶符「狂舞的落叶」",

			"秋符「秋季的天空」",
			"秋符「无常秋日与少女的心」",
			"丰符「大年收获者」",
			"丰收「谷物神的允诺」",

			"厄符「厄运」",
			"厄符「厄神大人的生理节律」",
			"疵符「破裂的护符」",
			"疵痕「损坏的护身符」",
			"恶灵「厄运之轮」",
			"悲运「大钟婆之火」",
			"创符「痛苦之流」",
			"创符「流刑人偶」",

			"光学「光学迷彩」",
			"光学「水迷彩」",
			"洪水「泥浆泛滥」",
			"洪水「冲积梦魇」",
			"漂溺「粼粼水底之心伤」",
			"水符「河童之河口浪潮」",
			"水符「河童之山洪暴发」",
			"水符「河童之幻想大瀑布」",
			"河童「妖怪黄瓜」",
			"河童「延展手臂」",
			"河童「回转顶板」",

			"岐符「天之八衢」",
			"岐符「猿田彦神之岔路」",
			"风神「风神木叶隐身术」",
			"风神「天狗颪」",
			"风神「二百十日」",
			"「幻想风靡」",
			"「无双风神」",
			"塞符「山神渡御」",
			"塞符「天孙降临」",
			"塞符「天上天下的照国」",

			"秘术「灰色奇术」",
			"秘术「遗忘之祭仪」",
			"秘术「一脉相传的弹幕」",
			"奇迹「白昼的客星」",
			"奇迹「客星璀璨之夜」",
			"奇迹「客星辉煌之夜」",
			"开海「割海成路之日」",
			"开海「摩西之奇迹」",
			"准备「呼唤神风的星之仪式」",
			"准备「召请建御名方神」",
			"奇迹「神之风」",
			"大奇迹「八坂之神风」",

			"神祭「扩展御柱」",
			"奇祭「目处梃子乱舞」",	
			"筒粥「神の粥」",
			"忘谷「遗忘之谷」",
			"神谷「神谕之谷」",
			"贽符「御射山御狩神事」",
			"神秘「葛泉清水」",
			"神秘「大和茅环」",	
			"天流「天水奇迹」",
			"天龙「雨之源泉」",
			"「信仰之山」",
			"「风神之神德」",

			"神符「如水眼之美丽源泉」",
			"神符「结于杉木之古缘」",
			"神符「神所踏足之御神渡」",

			"开宴「二拜二拍一拜」",
			"土著神「手长足长大人」",
			"神具「洩矢的铁轮」",
			"源符「厌川的翡翠」",
			"蛙狩「蛙以口鸣，方致蛇祸」",
			"土著神「七石七木」",
			"土著神「小小青蛙不输风雨」",
			"土著神「宝永四年的赤蛙」",
			"「诹访大战 ~ 土著神话 vs 中央神话」",
			"祟符「洩矢大人」",
			//th11
			"怪奇「钓瓶落之怪」",

			"罠符「捕捉之网」",
			"蜘蛛「石窟的蜘蛛巢」",
			"瘴符「瘴气场」",
			"瘴气「原因不明的热病」",

			"妒符「绿眼怪兽」",
			"嫉妒「看不见的绿眼怪兽」",
			"开花爷爷「对华丽的仁者之嫉妒」",
			"开花爷爷「小白的灰烬」",
			"剪舌麻雀「对谦虚的富者之记恨」",
			"剪舌麻雀「大葛笼与小葛笼」",
			"恨符「丑时参拜」",
			"恨符「丑时参拜第七日」",

			"鬼符「怪力乱神」",
			"怪轮「地狱之苦轮」",
			"枷符「罪人不释之枷」",
			"力业「大江山岚」",
			"力业「大江山颪」",
			"四天王奥义「三步必杀」",

			"想起「恐怖的回忆」",
			"想起「恐怖催眠术」",
			"想起「二重黑死蝶」",
			"想起「飞行虫之巢」",
			"想起「波与粒的境界」",
			"想起「户隐山之投」",
			"想起「百万鬼夜行」",
			"想起「蒙蒙迷雾」",
			"想起「风神木叶隐身术」",
			"想起「天狗巨暴流」",
			"想起「鸟居旋风」",
			"想起「春之京人偶」",
			"想起「稻草人敢死队」",
			"想起「回归虚无」",
			"想起「水银之毒」",
			"想起「水精公主」",
			"想起「贤者之石」",
			"想起「延展手臂」",
			"想起「河童之河口浪潮」",
			"想起「粼粼水底之心伤」",

			"猫符「猫的步伐」",
			"猫符「怨灵猫乱步」",
			"咒精「僵尸妖精」",
			"咒精「怨灵凭依妖精」",
			"恨灵「脾脏蛀食者」",
			"尸灵「食人怨灵」",
			"赎罪「旧地狱的针山」",
			"赎罪「古时之针与痛楚的怨灵」",
			"「死灰复燃」",
			"「小恶灵复活」",

			"妖怪「火焰的车轮」",

			"核热「核聚变」",
			"核热「核功率骤增」",
			"核热「核反应失控」",
			"爆符「小型耀斑」",
			"爆符「百万耀斑」",
			"爆符「十亿耀斑」",
			"爆符「千兆耀斑」",
			"焰星「恒星」",
			"焰星「行星公转」",
			"焰星「十凶星」",
			"「地狱极乐熔毁」",
			"「地狱的托卡马克」",
			"「地狱的人工太阳」",
			"「地底太阳」",

			"秘法「九字切」",
			"奇迹「神秘果」",
			"神德「五谷丰穰米之浴」",

			"表象「先祖托梦」",
			"表象「弹幕偏执症」",
			"本能「本我的解放」",
			"抑制「超我」",
			"反应「妖怪测谎机」",
			"潜意识「弹幕的墨迹测验」",
			"复燃「恋爱的埋火」",
			"深层「潜意识的基因」",
			"「被厌恶者的哲学」",
			"「Subterranean Rose」",
			//th12
			"棒符「忙碌探知棒」",
			"搜符「稀有金属探测器」",
			"搜符「G黄金探测器」",
			"视符「娜兹玲灵摆」",
			"视符「高感度娜兹玲灵摆」",
			"守符「灵摆防御」",

			"大轮「唐伞光晕」",
			"大轮「Hello,Forgotten World」",
			"伞符「雨伞的星之交响」",
			"伞符「雨伞的星之追忆」",
			"雨符「雨夜怪谈」",
			"雨伞「超防水干爽伞妖」",
			"化符「遗忘之伞的夜行列车」",
			"化铁「备用伞特急夜晚狂欢号」",

			"铁拳「问答无用妖怪拳」",
			"神拳「凌云地狱冲」",
			"神拳「天海地狱冲」",
			"拳符「天网沙袋」",
			"连打「云界海妖来袭」",
			"连打「帝王海妖来袭」",
			"拳打「重拳碎击」",	
			"溃灭「天上天下连续勾拳」",
			"大喝「守旧尊老之怒眼」",
			"忿怒「天变巨眼焚身」",
			"忿怒「空前绝后巨眼焚身」",

			"倾覆「同路人之锚」",
			"倾覆「沉没之锚」",
			"倾覆「击沉之锚」",
			"溺符「深海漩涡」",
			"溺符「沉底漩涡」",
			"港符「幽灵船之泊」",
			"港符「幽灵船之港」",
			"港符「幽灵船永久停泊」",
			"幽灵「Sinker Ghost」",
			"幽灵「悄然袭来的长勺」",

			"宝塔「最优良的宝物」",

			"宝塔「光辉之宝」",
			"宝塔「光辉宝枪」",
			"光符「绝对正义」",
			"光符「正义之威光」",
			"法力「至宝之独钴杵」",
			"法灯「无瑕佛法之独钴杵」",
			"光符「净化之魔」",
			"「完全净化」",

			"魔法「紫云之兆」",
			"吉兆「紫色云路」",
			"吉兆「极乐的紫色云路」",
			"魔法「魔界蝶之妖香」",
			"魔法「魔法之蝶」",
			"光魔「星辰漩涡」",
			"光魔「魔法银河」",
			"大魔法「魔神复诵」",
			"「圣尼公的气之魔法卷轴」",
			"超人「圣白莲」",
			"飞钵「飞行幻想」",
			"飞钵「传说的飞空圆盘」",	

			"伞符「大颗的泪雨」",
			"惊雨「台风骤雨」",
			"光晕「唐伞惊吓闪光」",

			"妖云「平安时代的黑云」",
			"真相不明「愤怒的红色UFO袭来」",
			"鵺符「鵺的蛇行表演」",
			"真相不明「哀愁的蓝色UFO袭来」",
			"鵺符「弹幕奇美拉」",
			"真相不明「忠义的绿色UFO袭来」",
			"鵺符「真相不明的黑暗」",
			"真相不明「恐怖的虹色UFO袭来」",
			"「平安京的恶梦」",
			"恨弓「源三位赖政之弓」",
			//th13
			"符牒「死蝶之舞」",
			"符牒「死蝶之舞 -樱花-」",
			"幽蝶「幽魂聚地」",
			"幽蝶「幽魂聚地 -樱花-」",
			"冥符「常夜樱」",
			"樱符「西行樱吹雪」",

			"响符「山谷回声」",
			"响符「混乱的山谷回声」",
			"响符「强力共振」",
			"山彦「远距离回声」",
			"山彦「扩大回声」",
			"大声「激动的呼喊」",
			"大声「激动Yahoo」",

			"虹符「雨伞风暴」",

			"回复「借由欲望的恢复」",
			"毒爪「剧毒抹消」",
			"毒爪「剧毒杀害」",
			"欲符「赚钱欲灵招来」",
			"欲灵「贪分欲吞噬者」",

			"邪符「养小鬼」",
			"邪符「孤魂野鬼」",
			"入魔「走火入魔」",
			"降灵「死人童乩」",
			"通灵「通灵芳香」",
			"道符「道胎动」",

			"雷矢「元兴寺的旋风」",
			"雷矢「元兴寺的龙卷」",
			"怨灵「入鹿之雷」",

			"天符「雨之磐舟」",
			"天符「天之磐舟哟，向天飞升吧」",
			"投皿「物部氏的八十平瓮」",
			"炎符「废佛之炎风」",
			"炎符「火烧樱井寺」",
			"圣童女「大物忌正餐」",

			"名誉「十二阶之色彩」",
			"名誉「十二阶之冠位」",
			"仙符「日出之处的道士」",
			"仙符「日出之处的天子」",
			"召唤「豪族乱舞」",
			"秘宝「斑鸠寺的天球仪」",
			"秘宝「圣德太子的欧帕兹」",
			"光符「救世观音的佛光」",
			"光符「救世之光」",
			"眼光「十七条的光芒」",
			"神光「无忤为宗」",
			"「星辰降落的神灵庙」",
			"「新生的神灵」",

			"未知「轨道不明的鬼火」",
			"未知「姿态不明的空鱼」",
			"未知「原理不明的妖怪玉」",

			"一回胜负「灵长类弹幕变化」",
			"二回胜负「肉食类弹幕变化」",
			"三回胜负「羽鸟类弹幕变化」",
			"四回胜负「两栖类弹幕变化」",
			"五回胜负「鸟兽戏画」",
			"六回胜负「狸猫的变身学校」",
			"七回胜负「野生的离岛」",
			"变化「魔奴化巫女的伪退治」",
			"「猯藏化弹幕十变化」",
			"貉符「满月下的腹鼓舞」",
			//th14
			"冰符「Ultimate Blizzard」",

			"水符「尾鳍拍击」",
			"鳞符「鳞之波」",
			"鳞符「逆鳞的惊涛」",
			"鳞符「逆鳞的大惊涛」",

			"飞符「飞行之头」",
			"首符「闭目射击」",
			"首符「辘轳首飞来」",
			"飞头「倍增之头」",
			"飞头「第七个头」",
			"飞头「第九个头」",
			"飞头「杜拉罕之夜」",

			"牙符「月下的犬齿」",
			"变身「三角齿」",
			"变身「星形齿」",
			"咆哮「陌生的咆哮」",
			"咆哮「满月的远吠」",
			"狼符「星环猛扑」",
			"天狼「高速猛扑」",

			"琴符「诸行无常的琴声」",
			"响符「平安的残响」",
			"响符「回音之庭」",
			"筝曲「下克上送筝曲」",
			"筝曲「下克上安魂曲」",

			"平曲「祗园精舍的钟声」",
			"怨灵「无耳芳一」",
			"怨灵「平家的大怨灵」",
			"乐符「邪恶的五线谱」",
			"乐符「凶恶的五线谱」",
			"乐符「Double Score」",

			"欺符「逆针击」",
			"逆符「镜之国的弹幕」",
			"逆符「镜中的邪恶」",
			"逆符「天地有用」",
			"逆符「天下翻覆」",
			"逆弓「天壤梦弓」",
			"逆弓「天壤梦弓的诏敕」",
			"逆转「阶级反转」",
			"逆转「变革空勇士」",

			"小弹「小人的道路」",
			"小弹「小人的荆棘路」",
			"小槌「变大吧」",
			"小槌「变得更大吧」",
			"妖剑「辉针剑」",
			"小槌「你给我变大吧」",
			"「进击的小人」",
			"「一寸之壁」",
			"「七个小拇指」",
			"「七个一寸法师」",	

			"弦乐「风暴的合奏」",
			"弦乐「净琉璃世界」",

			"一鼓「暴乱宫太鼓」",
			"二鼓「怨灵绫鼓」",
			"三鼓「午夜零时的三振」",
			"死鼓「轻敲大地」",
			"五鼓「雷电拨浪鼓」",
			"六鼓「交替打击法」",
			"七鼓「高速和太鼓火箭」",
			"八鼓「雷神之怒」",
			"「蓝色佳人的演出」",
			"「Pristine beat」",
			//th15
			"凶弹「高速撞击」",
			"弹符「射鹰」",
			"弹符「鹰已击中」",
			"铳符「月狂之枪」",

			"兔符「草莓团子」",
			"兔符「浆果浆果团子」",
			"兔符「团子影响力」",
			"月见「九月的满月」",
			"月见酒「月狂的九月」",

			"梦符「绯红色的噩梦」",
			"梦符「绯红色的压迫噩梦」",
			"梦符「蔚蓝色的愁梦」",
			"梦符「蔚蓝色的愁三重梦」",
			"梦符「愁永远之梦」",
			"梦符「刈安色的迷梦」",
			"梦符「刈安色的错综迷梦」",
			"梦符「捕梦网」",
			"梦符「苍蓝色的捕梦网」",
			"梦符「梦我梦中」",
			"月符「绀色的狂梦」",

			"玉符「乌合之咒」",
			"玉符「乌合的逆咒」",
			"玉符「乌合的二重咒」",
			"玉符「秽身探知型水雷」",
			"玉符「秽身探知型水雷 改」",
			"玉符「众神的弹冠」",
			"玉符「众神的光辉弹冠」",
			"「孤翼的白鹭」",

			"狱符「地狱日食」",
			"狱符「地狱之蚀」",
			"狱符「闪光与条纹」",
			"狱符「星与条纹」",
			"狱炎「擦弹地狱火」",
			"狱炎「擦弹的狱意」",
			"地狱「条纹状的深渊」",
			"「伪阿波罗」",
			"「阿波罗捏造说」",

			"「掌上的纯光」",
			"「杀意的百合」",
			"「现代的神灵界」",
			"「原始的神灵界」",
			"「战栗的寒冷之星」",
			"「纯粹的疯狂」",
			"「溢出的暇秽」",
			"「地上秽的纯化」",
			"纯符「单纯的子弹地狱」",
			"纯符「纯粹的弹幕地狱」",

			"蝴蝶「取而代之的蝴蝶」",
			"超特急「梦幻快车」",
			"爬梦「爬行的子弹」",

			"异界「逢魔之刻」",
			"地球「邪秽在身」",
			"月「阿波罗反射镜」",
			"「用于杀人的纯粹弹幕」",
			"异界「地狱的非理想弹幕」",
			"地球「落向地狱的雨」",
			"「用于逼死瓮中鼠的单纯弹幕」",
			"月「月狂冲击」",
			"「三位一体论狂想曲」",	
			"「最初与最后的无名弹幕」",		
			//th16
			"蝶符「细碎鳞粉」",
			"蝶符「凤蝶的鳞粉」",
			"蝶符「扑翅之夏」",
			"蝶符「盛夏振翅」",

			"雨符「被囚禁的秋雨」",
			"雨符「被诅咒的柴榑雨」",
			"刃符「山姥的菜刀研磨」",
			"刃符「山姥的鬼菜刀研磨」",
			"尽符「深山谋杀」",
			"尽符「血腥的深山谋杀」",

			"春符「惊喜之春」",

			"犬符「野犬的散步」",
			"狗符「山狗的散步」",
			"陀螺「狛犬回旋」",
			"陀螺「蜷缩死去」",
			"狛符「单人式阿吽的呼吸」",

			"魔符「顷刻菩提」",
			"魔符「即席菩提」",
			"魔符「弹丸魔像」",
			"魔符「作宠物的巨大弹生命体」",
			"地藏「罪业救赎」",
			"地藏「业火救济」",

			"竹符「竹矛之舞」",
			"竹符「竹之狂舞」",
			"笹符「七夕星祭」",

			"茗荷「忘却你的名字」",
			"冥加「在你背后」",

			"舞符「背后之祭」",
			"狂舞「天狗怖吓」",
			"狂舞「狂乱天狗怖吓」",

			"后符「秘神的后光」",
			"后符「绝对秘神的后光」",
			"里夏「暑夏炙烤」",
			"里夏「异常酷暑之焦土」",
			"里秋「死于饥荒」",
			"里秋「异常枯死之饿鬼」",
			"里冬「黑色雪人」",
			"里冬「异常降雪之雪人」",
			"里春「四月巫师」",
			"里春「异常落花之魔术使」",	
			"「里·Breeze Cherry Blossom」",
			"「里·Perfect Summer Ice」",
			"「里·Crazy Fall Wind」",
			"「里·Extreme Winter」",

			"鼓舞「强力助威」",
			"狂舞「疯狂的背景舞」",
			"弹舞「双目台风」",

			"秘仪「逆向呼神者」",
			"秘仪「背叛的后方射击」",
			"秘仪「弹幕的玉茧」",
			"秘仪「秽那之火」",
			"秘仪「后户的狂言」",
			"秘仪「摩多罗苦谛」",
			"秘仪「七星之剑」",
			"秘仪「无纽带的艺人」",
			"「背面的暗黑猿乐」",
			"「无秩序弹幕地狱」",
			//th17	
			"石符「石林」",
			"石符「石头针叶林」",
			"石符「儿童们的灵薄狱」",
			"石符「成年儿童们的灵薄狱」",

			"石符「石头婴儿」",
			"石符「沉重的石之头婴儿」",
			"溺符「三途的沦溺」",
			"鬼符「魔鬼围城」",
			"鬼符「饿鬼围城」",

			"水符「分水的试练」",
			"水符「分水的上级试炼」",
			"水符「分水的顶级试炼」",
			"光符「瞭望的试练」",
			"光符「瞭望的上级试炼」",
			"光符「瞭望的顶级试炼」",
			"鬼符「鬼渡的试练」",
			"鬼符「鬼渡的上级试炼」",
			"鬼符「鬼渡的狱级试炼」",

			"龟符「龟甲地狱」",
			"鬼符「搦手之畜生」",
			"鬼符「搦手之犬畜生」",
			"鬼符「搦手之鬼畜生」",
			"龙符「龙纹弹」",

			"埴轮「弓兵埴轮」",
			"埴轮「熟练弓兵埴轮」",
			"埴轮「剑士埴轮」",
			"埴轮「熟练剑士埴轮」",
			"埴轮「骑马兵埴轮」",
			"埴轮「熟练骑马兵埴轮」",
			"埴轮「空洞的无尽兵团」",
			"埴轮「不败的无尽兵团」",

			"方形「方形造形术」",
			"方形「方形造物」",
			"圆形「正圆造形术」",
			"圆形「圆形造物」",
			"线形「线形造形术」",
			"线形「线形造物」",
			"埴轮「偶像人马造形术」",
			"埴轮「偶像造物」",
			"「鬼形造形术」",
			"「几何造物」",
			"「Idola Diabolus」",

			"血战「血之分水岭」",
			"血战「狱界视线」",
			"血战「全灵鬼渡」",

			"劲疾技「惊险射击」",
			"劲疾技「闪电嘶鸣」",
			"劲疾技「浓云」",
			"劲疾技「兽性感染」",
			"劲疾技「三角追击」",
			"劲疾技「黑色天马流星弹」",
			"劲疾技「肌肉爆破」",
			"「跟我来，不要怕」",
			"「鬼形的乌合之众」",
			"「鬼畜生的所业」"
		};
		neta = new String[]{
			"红lnb",
			"红lnm",
			"妖lnm",
			"妖lnn",
			"永lnm",
			"风lnm",
			"风lnn",
			"殿lnm",
			"船lnm",
			"船lnn",
			"庙lnm",
			"城lnm",
			"绀lnm",
			"璋lnn"};
		music = new String[]{
			//th4
			"bad apple",
			//th6
			"月时计",
			"献给已逝公主的七重奏",
			//th7
			"天空的花之都",
			"幽灵乐团",
			"幽雅的绽放 墨染的樱花",
			"少女幻葬",
			"死亡幻想",
			//th8
			"少女绮想曲",
			"恋色 master spark",
			"竹取飞翔",
			//th10
			"眷爱众生之神",
			"众神眷恋的幻想乡",
			"少女曾见的日本原风景",
			"信仰是为了虚幻之人",
			"神圣庄严的古战场",
			//th11
			"漫步在地狱的街道",
			"尸体旅行",
			//th12
			"春之岸边",
			"感情的摩天轮",
			//th13
			"古老的元神",
			"圣德传说 ~ True Administrator",
			//th14
			"幻想净琉璃",
			"辉光之针的小人族",
			//th15
			"忘不了，那曾依籍的绿意",
			"九月的南瓜",
			"遥遥38万公里的航程",
			"星条旗的小丑",
			"Pure Furies ~ 心之所在",
			//th16
			"被隐匿的四个季节",
			"门再也进不去了",
			"秘神摩多罗 ~ Hidden Star in All Seasons.",
			//th17
			"寄世界于偶像 ~ Idoratrize World",
		};

		name = new String[]{
			//th2
			"里香",
			"明罗",
			"魅魔",
			//th3
			"爱莲", 
			"小兔姬", 
			"卡娜·安娜贝拉尔",
			"朝仓理香子", 
			"北白河千百合", 
			"冈崎梦美",
			//th4
			"奥莲姬",
			"胡桃",
			"艾丽",
			"梦月",
			"幻月",
			//th5
			"萨拉",
			"露易兹",
			"雪",
			"舞",
			"梦子",
			"神绮",
			//th6
			"露米娅",
			"大妖精",
			"琪露诺",
			"红美铃",
			"帕秋莉·诺蕾姬",
			"十六夜咲夜",
			"蕾米莉亚·斯卡雷特",
			"芙兰朵露·斯卡雷特",
			//th7
			"蕾蒂",
			"橙",
			"爱丽丝·玛格特罗依德",
			"莉莉白",
			"莉莉卡·普莉兹姆利巴", 
			"梅露兰·普莉兹姆利巴",
			"露娜萨·普莉兹姆利巴",
			"魂魄妖梦",
			"西行寺幽幽子",
			"八云蓝",
			"八云紫",
			//th7.5
			"伊吹萃香",
			//th8
			"莉格露",
			"米斯蒂娅·萝蕾拉",
			"上白泽慧音",
			"博丽灵梦",
			"雾雨魔理沙",
			"因幡帝",
			"铃仙·优昙华院·因幡",
			"八意永琳",
			"蓬莱山辉夜",
			"藤原妹红",
			//th9
			"梅蒂欣·梅兰可莉",
			"风见幽香",
			"小野冢小町",
			"四季映姬",
			//th10
			"秋静叶",
			"秋穰子",
			"键山雏",
			"河城荷取",
			"犬走椛",
			"射命丸文",
			"东风谷早苗",
			"八坂神奈子",
			"洩矢诹访子",
			//th11
			"琪斯美",
			"黑谷山女",
			"水桥帕露西",
			"星熊勇仪",
			"古明地觉",
			"火焰猫燐",
			"灵乌路空",
			"古明地恋",
			//th12
			"纳兹琳",
			"多多良小伞",
			"云居一轮",
			"村纱水蜜",
			"寅丸星",
			"圣白莲",
			"封兽鵺",
			//th12.8
			"桑尼·米尔克",
			"露娜·切露德",
			"斯塔·萨菲雅",
			//th13
			"鬼火",
			"幽谷响子",
			"宫古芳香",
			"霍青娥",
			"苏我屠自古",
			"物部布都",
			"丰聪耳神子",
			"二岩瑞藏",
			//th13.5
			"秦心",
			//th14
			"若鹭姬",
			"赤蛮奇",
			"今泉影狼",
			"九十九八桥",
			"九十九弁弁",
			"鬼人正邪",
			"少名针妙丸",
			"堀川雷鼓",
			//th14.5
			"宇佐见堇子",
			//th15
			"清兰",
			"铃瑚",
			"哆来咪·苏伊特",
			"稀神探女",
			"克劳恩皮丝",
			"纯狐",
			"赫卡提亚·拉碧斯拉祖利",
			//th15.5
			"依神紫苑",
			"依神女苑",
			//th16
			"爱塔妮缇·拉尔瓦",
			"坂田合欢",
			"高丽野阿吽",
			"矢田寺成美",
			"尔子田里乃",
			"丁礼田舞",
			"摩多罗隐岐奈",
			//th17
			"戎璎花",
			"牛崎润美",
			"庭渡久侘歌",
			"吉吊八千慧",
			"杖刀偶磨弓",
			"埴安神袿姬",
			"骊驹早鬼"};
		addArrayToSet(sp6, "月符「月光」", "QED「495年的波纹」");
		addArrayToSet(sp7, "冰符「冰袭方阵」", "紫奥义「弹幕结界」");
		addArrayToSet(sp8, "萤符「地上的流星」", "「蓬莱人形」");
		addArrayToSet(sp10, "叶符「狂舞的落叶」", "祟符「洩矢大人」");
		addArrayToSet(sp11, "怪奇「钓瓶落之怪」", "「Subterranean Rose」");
		addArrayToSet(sp12, "棒符「忙碌探知棒」", "恨弓「源三位赖政之弓」");
		addArrayToSet(sp13, "符牒「死蝶之舞」", "貉符「满月下的腹鼓舞」");
		addArrayToSet(sp14, "冰符「Ultimate Blizzard」", "「Pristine beat」");
		addArrayToSet(sp15, "凶弹「高速撞击」", "「最初与最后的无名弹幕」");
		addArrayToSet(sp16, "蝶符「细碎鳞粉」", "「无秩序弹幕地狱」");
		addArrayToSet(sp17, "石符「石林」", "「鬼畜生的所业」");
		addArrayToSet(memory, "想起「二重黑死蝶」", "想起「粼粼水底之心伤」");
		addArrayToSet(pachouli, "火符「火神之光」", "土&金符「翡翠巨石」");
		addArrayToSet(pachouli, "月符「静息的月神」", "火水木金土符「贤者之石」");

		Collections.addAll(cat, new String[]{
							   "仙符「凤凰卵」",
							   "仙符「凤凰展翅」",
							   "式符「飞翔晴明」",
							   "阴阳「道满晴明」",	
							   "阴阳「晴明大纹」",
							   "天符「天仙鸣动」",
							   "翔符「飞翔韦驮天」",
							   "童符「护法天童乱舞」",
							   "仙符「尸解永远」",
							   "鬼符「鬼门金神」",
							   "方符「奇门遁甲」",
							   "鬼符「青鬼赤鬼」",
							   "鬼神「飞翔毘沙门天」",
							   "猫符「猫的步伐」",
							   "猫符「怨灵猫乱步」",
							   "咒精「僵尸妖精」",
							   "咒精「怨灵凭依妖精」",
							   "恨灵「脾脏蛀食者」",
							   "尸灵「食人怨灵」",
							   "赎罪「旧地狱的针山」",
							   "赎罪「古时之针与痛楚的怨灵」",
							   "「死灰复燃」",
							   "「小恶灵复活」",
							   "妖怪「火焰的车轮」"
						   });

	}
	public boolean check(long fromGroup, long fromQQ, String msg) {
		String[] ss = msg.split("\\.");
        if (ss.length > 1) {
            if (ss[0].equals("roll")) {
                switch (ss[1]) {
                    case "pl":
                    case "plane":
                    case "player":
                        if (ss.length == 3) {
                            rollPlane(ss[2], fromGroup);
                        } else if (ss.length == 4) {
                            rollPlane(ss[2] + "." + ss[3], fromGroup);
                        }
                        break;
                    case "游戏":
                    case "game":
                        Autoreply.sendMessage(fromGroup, 0, "th" + (Autoreply.instence.random.nextInt(16) + 1));
                        break;
                    case "diff":
                    case "difficult":
                    case "难度":
                        Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(plDiff));
                        break;
                    case "stage":
                    case "关卡":
                    case "面数":
                        rollStage(ss, fromGroup);
                        break;
                    case "help":
                    case "帮助":
                        String str = "\nroll.game roll.游戏 可以随机选择游戏\nroll.difficult roll.diff roll.难度 可以随机选择难度\nroll.player roll.pl roll.plane接作品名或编号可随机选择机体（仅官方整数作）\nroll.stage roll.关卡 roll.面数 加玩家名可用来接力时随机选择面数，多个玩家名之间用.隔开\n";
                        Autoreply.sendMessage(fromGroup, 0, str);
                        break;
                }
                return true;
            }
        }
		String pname=Autoreply.instence.configManager.getNickName(fromGroup, fromQQ);
		String md5=Methods.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		char c=md5.charAt(0);
		switch (msg) {
			case ".jrrp":
				float fpro=0f;
				if (c == '0') {
					fpro = 99.61f;
				} else if (c == '1') {
					fpro = 97.60f;
				} else {
					fpro = ((float)(md5Random(fromQQ) % 10001)) / 100;
				}
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%.2f%%处疮痍", pname, fpro));
				return true;	
			case "。jrrp":
				Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, md5RanStr(fromQQ, spells)));
		   		return true;
		}

		if (msg.startsWith(".draw")) {
			String drawcmd=msg.substring(6);
			switch (drawcmd) {
				case "help":
					Autoreply.sendMessage(fromGroup, 0, "当前有:spell neta music grandma game all");
					return true;
				case "spell":
					Autoreply.sendMessage(fromGroup, 0, spells[new Random().nextInt(spells.length)]);
					return true;
				case "neta":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, neta)));
					return true;
				case "music":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜听%s", pname, md5RanStr(fromQQ, music)));
					return true;
				case "grandma":
					if (Methods.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
						Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认八云紫当奶奶", pname));
						return true;
					}
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name)));
					return true;
				case "game":
					String s=randomGame(pname, fromQQ, true);
					s += ",";
					s += randomGame(pname, fromQQ + 1, false);
					Autoreply.sendMessage(fromGroup, 0, s);
					return true;
				case "jrrp":
					Autoreply.sendMessage(fromGroup, 0, String.format("%s今天会在%s疮痍", pname, md5RanStr(fromQQ, spells)));
					return true;
				case "all":
					String sss=String.format("%s今天宜打%s", pname, md5RanStr(fromQQ, neta));
					sss += "\n";
					sss += String.format("%s今天宜听%s", pname, md5RanStr(fromQQ, music));
					sss += "\n";
					if (Methods.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000))).charAt(0) == '0') {
						sss += String.format("%s今天宜认八云紫当奶奶", pname);
					} else {
						sss += String.format("%s今天宜认%s当奶奶", pname, md5RanStr(fromQQ, name));
					}
					sss += "\n";
					sss += randomGame(pname, fromQQ, true);
					sss += ",";
					sss += randomGame(pname, fromQQ + 1, false);
					sss += "\n";
					float fpro=0f;
					if (c == '0') {
						fpro = 99.61f;
					} else if (c == '1') {
						fpro = 97.60f;
					} else {
						fpro = ((float)(md5Random(fromQQ) % 10001)) / 100;
					}
					sss += String.format("%s今天会在%.2f%%处疮痍", pname, fpro);
					Autoreply.sendMessage(fromGroup, 0, sss);
					return true;			
				default:
					if (drawcmd.startsWith("spell ")) {	
						int i=-1;
						try {
							i = Integer.parseInt(drawcmd.substring(6));
							String spellResult=pname + "抽到了:";
							Random r=new Random();
							for (int ii=0;ii < i;ii++) {
								spellResult += "\n";
								spellResult += spells[r.nextInt(spells.length)];			
							}	
							Autoreply.sendMessage(fromGroup, 0, spellResult);
						} catch (NumberFormatException e) {
							Autoreply.sendMessage(fromGroup, 0, e.toString());
						}
					} else {
						Autoreply.sendMessage(fromGroup, 0, "可用.draw help查看帮助");						
					}
					break;
			}	
			return true;
		}

		return false;
	}

	private String randomGame(String pname, long fromQQ, boolean goodAt) {
		int gameNo=md5Random(fromQQ) % 16 + 2;
		String gameName = null;
		String charaName = null;
		switch (gameNo) {
			case 2:
				gameName = "封魔录";
				charaName = md5RanStr(fromQQ + 2, pl02);
				break;
			case 3:
				gameName = "梦时空";
				charaName = md5RanStr(fromQQ + 2, pl03);
				break;
			case 4:
				gameName = "幻想乡";
				charaName = md5RanStr(fromQQ + 2, pl04);
				break;
			case 5:
				gameName = "怪绮谈";
				charaName = md5RanStr(fromQQ + 2, pl05);
				break;
			case 6:
				gameName = "红魔乡";
				charaName = md5RanStr(fromQQ + 2, pl06);
				break;
			case 7:
				gameName = "妖妖梦";
				charaName = md5RanStr(fromQQ + 2, pl07);
				break;
			case 8:
				gameName = "永夜抄";
				charaName = md5RanStr(fromQQ + 2, pl08);
				break;
			case 9:
				gameName = "花映冢";
				charaName = md5RanStr(fromQQ + 2, pl09);
				break;
			case 10:
				gameName = "风神录";
				charaName = md5RanStr(fromQQ + 2, pl10);
				break;
			case 11:
				gameName = "地灵殿";
				charaName = md5RanStr(fromQQ + 2, pl11);
				break;
			case 12:
				gameName = "星莲船";
				charaName = md5RanStr(fromQQ + 2, pl12);
				break;
			case 13:
				gameName = "神灵庙";
				charaName = md5RanStr(fromQQ + 2, pl13);
				break;
			case 14:
				gameName = "辉针城";
				charaName = md5RanStr(fromQQ + 2, pl14);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl14s), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, pl14s), gameName);
				}
			case 15:
				gameName = "绀珠传";
				charaName = md5RanStr(fromQQ + 2, pl15);
				break;
			case 16:
				gameName = "天空璋";
				charaName = md5RanStr(fromQQ + 2, pl16);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl16s), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, pl16s), gameName);
				}
			case 17:
				gameName = "鬼形兽";
				charaName = md5RanStr(fromQQ + 2, pl17);
				if (goodAt) {
					return String.format("%s今天宜用%s-%s打%s", pname, charaName, md5RanStr(fromQQ + 1, pl17s), gameName);
				} else {
					return String.format("忌用%s-%s打%s", charaName, md5RanStr(fromQQ + 1, pl17s), gameName);
				}
			default:
				return "";
		}
		if (goodAt) {
			return String.format("%s今天宜用%s打%s", pname, charaName, gameName);
		} else {
			return String.format("忌用%s打%s", charaName, gameName);
		}
	}

	private void rollPlane(String ss, long fromGroup) {
        switch (ss) {
            case "东方灵异传":
            case "th1":
            case "th01":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl01));
                break;
            case "东方封魔录":
            case "th2":
            case "th02":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl02));
                break;
            case "东方梦时空":
            case "th3":
            case "th03":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl03));
                break;
            case "东方幻想乡":
            case "th4":
            case "th04":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl04));
                break;
            case "东方怪绮谈":
            case "th5":
            case "th05":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl05));
                break;
            case "东方红魔乡":
            case "th6":
            case "th06":
            case "tEoSD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl06));
                break;
            case "东方妖妖梦":
            case "th7":
            case "th07":
            case "PCB":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl07));
                break;
            case "东方永夜抄":
            case "th8":
            case "th08":
            case "IN":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl08));
                break;
            case "东方花映冢":
            case "th9":
            case "th09":
            case "PoFV":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl09));
                break;
            case "东方风神录":
            case "th10":
            case "MoF":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl10));
                break;
            case "东方地灵殿":
            case "th11":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl11));
                break;
            case "东方星莲船":
            case "th12":
            case "UFO":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl12));
                break;
            case "东方神灵庙":
            case "th13":
            case "TD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl13));
                break;
            case "东方辉针城":
            case "th14":
            case "DDC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl14) + " " + Methods.rfa(pl14s));
                break;
            case "东方绀珠传":
            case "th15":
            case "LoLK":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl15));
                break;
            case "东方天空璋":
            case "th16":
            case "HSiFS":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl16) + " " + Methods.rfa(pl16s));
                break;
            case "东方鬼形兽":
            case "th17":
            case "WBaWC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl17) + "+" + Methods.rfa(pl17s));
                break;

            case "东方文花帖":
            case "th9.5":
            case "StB":
                //       case "东方文花帖DS":
                //       case "th12.5":
                //       case "DS":
            case "妖精大战争":
            case "th12.8":
            case "弹幕天邪鬼":
            case "th14.3":
            case "ISC":
            case "秘封噩梦日记":
            case "th16.5":
            case "VD":
                Autoreply.sendMessage(fromGroup, 0, "就一个飞机你roll你[CQ:emoji,id=128052]呢");
                break;
            default:
                Autoreply.sendMessage(fromGroup, 0, "只有2un飞机游戏");
                break;
        }
    }

    private void rollStage(String[] ss, long fromGroup) {
        HashMap<Integer, String> hMap = new HashMap<>();
        for (int i = 2; i < ss.length; i++) {
            hMap.put(Autoreply.instence.random.nextInt(), ss[i]);
        }
        int flag = 1;
        StringBuilder sBuilder = new StringBuilder();
        for (Integer key : hMap.keySet()) {
            sBuilder.append("stage").append(flag).append(":").append(hMap.get(key)).append("\n");
            flag++;
        }
        Autoreply.sendMessage(fromGroup, 0, sBuilder.append("完成").toString());
    }

	private int md5Random(long fromQQ) {
		String md5=Methods.stringToMD5(String.valueOf(fromQQ + System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		return Integer.parseInt(md5.substring(26), 16);
	}

	public String md5RanStr(long fromQQ, String[] arr) {
		return arr[md5Random(fromQQ) % arr.length];
	}

	private void addArrayToSet(Set<String> set, String start, String stop) {
		int istart=0;
		int istop=0;
		for (int i=0;i < spells.length;++i) {
			if (spells[i].equals(start)) {
				istart = i;
			} else if (spells[i].equals(stop)) {
				istop = i;
			}	
		}
		for (int i=istart;i <= istop;++i) {
			set.add(spells[i]);
		}
	}
}
