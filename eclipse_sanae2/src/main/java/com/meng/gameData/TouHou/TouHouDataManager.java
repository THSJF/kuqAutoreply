package com.meng.gameData.TouHou;

import com.meng.*;
import com.meng.dice.*;
import java.util.*;

public class TouHouDataManager {
	public static TouHouDataManager ins;
	private HashMap<String,String> spellCardInfoMap=new HashMap<>();
	
	public TouHouDataManager() {
		initSpellPs();
	}
	public boolean check(long fromGroup, long fromQQ, String msg){
		if (msg.startsWith("-符卡查询 ")) {
			SpellCard sc = TouHouDataManager.ins.getSpellCard(msg.substring(6));
			if (sc == null) {
				Autoreply.sendMessage(fromGroup, 0, "没有找到这张符卡");
				return true;
			}
			Autoreply.sendMessage(fromGroup, 0, TouHouDataManager.ins.getSpellCardPs(sc));
			return true;
		}
		if (msg.startsWith("-角色查询 ")) {
			Autoreply.sendMessage(fromGroup, 0, TouHouDataManager.ins.getCharaNick(msg.substring(6)));
			return true;
		}
		return false;
	}
	public String getSpellCardPs(SpellCard sc) {
		StringBuilder sb=new StringBuilder();
		sb.append(sc.n).append("是").append(sc.m);
		if (sc.d != SpellCard.Ls && sc.d != SpellCard.Lw) {
			sb.append("在");
			if ((sc.d & SpellCard.E) == SpellCard.E) {
				sb.append(" easy");
			}
			if ((sc.d & SpellCard.N) == SpellCard.N) {
				sb.append(" normal");
			}
			if ((sc.d & SpellCard.H) == SpellCard.H) {
				sb.append(" hard");
			}
			if ((sc.d & SpellCard.L) == SpellCard.L) {
				sb.append(" lunatic");
			}
			if (sc.d == SpellCard.Ex) {
				sb.append(" extra");
			}
			if (sc.d == SpellCard.Ph) {
				sb.append(" phantasm");
			}
			if (sc.d == SpellCard.O) {
				sb.append(" overdrive");
			}
			sb.append("难度下的符卡");
		} else {
			if (sc.d == SpellCard.Ls) {
				sb.append("的lastspell");
			} else if (sc.d == SpellCard.Lw) {
				sb.append("的lastword");
			}
		}
		sb.append("\n附加:\n");
		sb.append(spellCardInfoMap.get(sc.n));
		return sb.toString();
	}

	public String getCharaNick(String charaName) {
		String fullName=null;
		for (TouhouCharacter s:DiceImitate.name) {
			if (s.charaName.contains(charaName)) {
				fullName = s.charaName;
				break;
			}
		}
		if (fullName == null) {
			return "该角色信息未填坑";
		}
		StringBuilder sb=new StringBuilder();
		sb.append(fullName).append("有以下称号:\n");
		for (TouhouCharacter thc:DiceImitate.name) {
			if (thc.charaName.equals(fullName)) {
				if (thc.nick.equals("该角色信息未填坑")) {
					continue;
				}
				sb.append(thc.nick).append("(").append(thc.game).append(")\n");
			}
		}
		if (sb.toString().equals(fullName + "有以下称号:\n")) {
			return "该角色信息未填坑";
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public SpellCard getSpellCard(String spellName) {
		for (SpellCard sc:DiceImitate.spells) {
			if (sc.n.contains(spellName)) {
				return sc;
			}
		}
		return null;
	}

	public SpellCard getSpellCard(String spellName, int diff) {
		for (SpellCard sc:DiceImitate.spells) {
			if (sc.n.contains(spellName) && sc.d == diff) {
				return sc;
			}
		}
		return null;
	}

	public HashSet<SpellCard> getCharaSpellCard(String name) {
		HashSet<SpellCard> scs=new HashSet<>();
		for (SpellCard sc:DiceImitate.spells) {
			if (sc.m.equals(name)) {
				scs.add(sc);
			}
		}
		return scs;
	}

	public HashSet<SpellCard> getCharaSpellCard(String name, int diff) {
		HashSet<SpellCard> scs=new HashSet<>();
		for (SpellCard sc:DiceImitate.spells) {
			if (sc.m.equals(name) && sc.d == diff) {
				scs.add(sc);
			}
		}
		return scs;
	}

	public HashSet<SpellCard> getCharaSpellCard(String name, String... spellExcept) {
		HashSet<SpellCard> scs=new HashSet<>();
		for (SpellCard sc:DiceImitate.spells) {
			if (sc.m.equals(name)) {
				for (String necx:spellExcept) {
					if (!sc.n.equals(necx)) {
						scs.add(sc);
					}
				}
			}
		}
		return scs;
	}

	private void initSpellPs(){
		spellCardInfoMap.put("月符「月光」","未填坑");
		spellCardInfoMap.put("夜符「夜雀」","未填坑");
		spellCardInfoMap.put("暗符「境界线」","未填坑");
		spellCardInfoMap.put("冰符「冰瀑」","未填坑");
		spellCardInfoMap.put("雹符「冰雹暴风」","未填坑");
		spellCardInfoMap.put("冻符「完美冻结」","未填坑");
		spellCardInfoMap.put("雪符「钻石风暴」","未填坑");
		spellCardInfoMap.put("华符「芳华绚烂」","未填坑");
		spellCardInfoMap.put("华符「Selaginella 9」","未填坑");
		spellCardInfoMap.put("虹符「彩虹风铃」","未填坑");
		spellCardInfoMap.put("幻符「华想梦葛」","未填坑");
		spellCardInfoMap.put("彩符「彩雨」","未填坑");
		spellCardInfoMap.put("彩符「彩光乱舞」","未填坑");
		spellCardInfoMap.put("彩符「极彩台风」","未填坑");
		spellCardInfoMap.put("火符「火神之光」","未填坑");
		spellCardInfoMap.put("水符「水精公主」","未填坑");
		spellCardInfoMap.put("木符「风灵的角笛」","未填坑");
		spellCardInfoMap.put("土符「慵懒三石塔」","未填坑");
		spellCardInfoMap.put("金符「金属疲劳」","未填坑");
		spellCardInfoMap.put("火符「火神之光 上级」","未填坑");
		spellCardInfoMap.put("木符「风灵的角笛 上级」","未填坑");
		spellCardInfoMap.put("土符「慵懒三石塔 上级」","未填坑");
		spellCardInfoMap.put("水符「湖葬」","未填坑");
		spellCardInfoMap.put("木符「翠绿风暴」","未填坑");
		spellCardInfoMap.put("土符「三石塔的震动」","未填坑");
		spellCardInfoMap.put("金符「银龙」","未填坑");
		spellCardInfoMap.put("火&土符「环状熔岩带」","未填坑");
		spellCardInfoMap.put("木&火符「森林大火」","未填坑");
		spellCardInfoMap.put("水&木符「水精灵」","未填坑");
		spellCardInfoMap.put("金&水符「水银之毒」","未填坑");
		spellCardInfoMap.put("土&金符「翡翠巨石」","未填坑");
		spellCardInfoMap.put("奇术「误导」","未填坑");
		spellCardInfoMap.put("奇术「幻惑误导」","未填坑");
		spellCardInfoMap.put("幻在「钟表的残骸」","未填坑");
		spellCardInfoMap.put("幻幽「迷幻杰克」","未填坑");
		spellCardInfoMap.put("幻象「月神之钟」","未填坑");
		spellCardInfoMap.put("幻世「世界」","未填坑");
		spellCardInfoMap.put("女仆秘技「操弄玩偶」","未填坑");
		spellCardInfoMap.put("女仆秘技「杀人玩偶」","未填坑");
		spellCardInfoMap.put("奇术「永恒的温柔」","未填坑");
		spellCardInfoMap.put("天罚「大卫之星」","未填坑");
		spellCardInfoMap.put("神罚「年幼的恶魔之王」","未填坑");
		spellCardInfoMap.put("冥符「红色的冥界」","未填坑");
		spellCardInfoMap.put("狱符「千根针的针山」","未填坑");
		spellCardInfoMap.put("诅咒「弗拉德·特佩斯的诅咒」","未填坑");
		spellCardInfoMap.put("神术「吸血鬼幻想」","未填坑");
		spellCardInfoMap.put("红符「绯红射击」","未填坑");
		spellCardInfoMap.put("红符「绯红之主」","未填坑");
		spellCardInfoMap.put("「Red Magic」","未填坑");
		spellCardInfoMap.put("「红色的幻想乡」","未填坑");
		spellCardInfoMap.put("月符「静息的月神」","未填坑");
		spellCardInfoMap.put("日符「皇家烈焰」","未填坑");
		spellCardInfoMap.put("火水木金土符「贤者之石」","未填坑");
		spellCardInfoMap.put("禁忌「红莓陷阱」","未填坑");
		spellCardInfoMap.put("禁忌「莱瓦汀」","未填坑");
		spellCardInfoMap.put("禁忌「四重存在」","未填坑");
		spellCardInfoMap.put("禁忌「笼中鸟」","未填坑");
		spellCardInfoMap.put("禁忌「恋之迷宫」","未填坑");
		spellCardInfoMap.put("禁弹「星弧破碎」","未填坑");
		spellCardInfoMap.put("禁弹「折反射」","未填坑");
		spellCardInfoMap.put("禁弹「刻着过去的钟表」","未填坑");
		spellCardInfoMap.put("秘弹「之后就一个人都没有了吗？」","未填坑");
		spellCardInfoMap.put("QED「495年的波纹」","未填坑");
		spellCardInfoMap.put("冰符「冰袭方阵」","未填坑");
		spellCardInfoMap.put("寒符「延长的冬日」","未填坑");
		spellCardInfoMap.put("冬符「花之凋零」","未填坑");
		spellCardInfoMap.put("白符「波状光」","未填坑");
		spellCardInfoMap.put("怪符「桌灵转」","未填坑");
		spellCardInfoMap.put("仙符「凤凰卵」","未填坑");
		spellCardInfoMap.put("仙符「凤凰展翅」","未填坑");
		spellCardInfoMap.put("式符「飞翔晴明」","未填坑");
		spellCardInfoMap.put("阴阳「道满晴明」","未填坑");
		spellCardInfoMap.put("阴阳「晴明大纹」","未填坑");
		spellCardInfoMap.put("天符「天仙鸣动」","未填坑");
		spellCardInfoMap.put("翔符「飞翔韦驮天」","未填坑");
		spellCardInfoMap.put("童符「护法天童乱舞」","未填坑");
		spellCardInfoMap.put("仙符「尸解永远」","未填坑");
		spellCardInfoMap.put("鬼符「鬼门金神」","未填坑");
		spellCardInfoMap.put("方符「奇门遁甲」","未填坑");
		spellCardInfoMap.put("操符「少女文乐」","未填坑");
		spellCardInfoMap.put("苍符「博爱的法兰西人偶」","未填坑");
		spellCardInfoMap.put("苍符「博爱的奥尔良人偶」","未填坑");
		spellCardInfoMap.put("红符「红发的荷兰人偶」","未填坑");
		spellCardInfoMap.put("白符「白垩的俄罗斯人偶」","未填坑");
		spellCardInfoMap.put("暗符「雾之伦敦人偶」","未填坑");
		spellCardInfoMap.put("回符「轮回的西藏人偶」","未填坑");
		spellCardInfoMap.put("雅符「春之京人偶」","未填坑");
		spellCardInfoMap.put("诅咒「魔彩光的上海人偶」","未填坑");
		spellCardInfoMap.put("诅咒「上吊的蓬莱人偶」","未填坑");
		spellCardInfoMap.put("弦奏「Guarneri del Gesù」","未填坑");
		spellCardInfoMap.put("神弦「Stradivarius」","未填坑");
		spellCardInfoMap.put("伪弦「Pseudo Stradivarius」","未填坑");
		spellCardInfoMap.put("管灵「日野幻想」","未填坑");
		spellCardInfoMap.put("冥管「灵之克里福德」","未填坑");
		spellCardInfoMap.put("管灵「灵之克里福德」","未填坑");
		spellCardInfoMap.put("冥键「法吉奥里冥奏」","未填坑");
		spellCardInfoMap.put("键灵「蓓森朵芙神奏」","未填坑");
		spellCardInfoMap.put("骚符「幽灵絮语」","未填坑");
		spellCardInfoMap.put("骚符「活着的骚灵」","未填坑");
		spellCardInfoMap.put("合葬「棱镜协奏曲」","未填坑");
		spellCardInfoMap.put("骚葬「冥河边缘」","未填坑");
		spellCardInfoMap.put("大合葬「灵车大协奏曲」","未填坑");
		spellCardInfoMap.put("大合葬「灵车大协奏曲改」","未填坑");
		spellCardInfoMap.put("大合葬「灵车大协奏曲怪」","未填坑");
		spellCardInfoMap.put("幽鬼剑「妖童饿鬼之断食」","未填坑");
		spellCardInfoMap.put("饿鬼剑「饿鬼道草纸」","未填坑");
		spellCardInfoMap.put("饿王剑「饿鬼十王的报应」","未填坑");
		spellCardInfoMap.put("狱界剑「二百由旬之一闪」","未填坑");
		spellCardInfoMap.put("狱炎剑「业风闪影阵」","未填坑");
		spellCardInfoMap.put("狱神剑「业风神闪斩」","未填坑");
		spellCardInfoMap.put("畜趣剑「无为无策之冥罚」","未填坑");
		spellCardInfoMap.put("修罗剑「现世妄执」","未填坑");
		spellCardInfoMap.put("人界剑「悟入幻想」","未填坑");
		spellCardInfoMap.put("人世剑「大悟显晦」","未填坑");
		spellCardInfoMap.put("人神剑「俗谛常住」","未填坑");
		spellCardInfoMap.put("天上剑「天人之五衰」","未填坑");
		spellCardInfoMap.put("天界剑「七魄忌讳」","未填坑");
		spellCardInfoMap.put("天神剑「三魂七魄」","未填坑");
		spellCardInfoMap.put("六道剑「一念无量劫」","未填坑");
		spellCardInfoMap.put("亡乡「亡我乡 -彷徨的灵魂-」","未填坑");
		spellCardInfoMap.put("亡乡「亡我乡 -宿罪-」","未填坑");
		spellCardInfoMap.put("亡乡「亡我乡 -无道之路-」","未填坑");
		spellCardInfoMap.put("亡乡「亡我乡 -自尽-」","未填坑");
		spellCardInfoMap.put("亡舞「生者必灭之理 -眩惑-」","未填坑");
		spellCardInfoMap.put("亡舞「生者必灭之理 -死蝶-」","未填坑");
		spellCardInfoMap.put("亡舞「生者必灭之理 -毒蛾-」","未填坑");
		spellCardInfoMap.put("亡舞「生者必灭之理 -魔境-」","未填坑");
		spellCardInfoMap.put("华灵「死蝶」","未填坑");
		spellCardInfoMap.put("华灵「燕尾蝶」","未填坑");
		spellCardInfoMap.put("华灵「深固难徙之蝶」","未填坑");
		spellCardInfoMap.put("华灵「蝶幻」","未填坑");
		spellCardInfoMap.put("幽曲「埋骨于弘川 -伪灵-」","未填坑");
		spellCardInfoMap.put("幽曲「埋骨于弘川 -亡灵-」","未填坑");
		spellCardInfoMap.put("幽曲「埋骨于弘川 -幻灵-」","未填坑");
		spellCardInfoMap.put("幽曲「埋骨于弘川 -神灵-」","未填坑");
		spellCardInfoMap.put("樱符「完全墨染的樱花 -封印-」","未填坑");
		spellCardInfoMap.put("樱符「完全墨染的樱花 -亡我-」","未填坑");
		spellCardInfoMap.put("樱符「完全墨染的樱花 -春眠-」","未填坑");
		spellCardInfoMap.put("樱符「完全墨染的樱花 -开花-」","未填坑");
		spellCardInfoMap.put("「反魂蝶 -一分咲-」","未填坑");
		spellCardInfoMap.put("「反魂蝶 -三分咲-」","未填坑");
		spellCardInfoMap.put("「反魂蝶 -五分咲-」","未填坑");
		spellCardInfoMap.put("「反魂蝶 -八分咲-」","未填坑");
		spellCardInfoMap.put("鬼符「青鬼赤鬼」","未填坑");
		spellCardInfoMap.put("鬼神「飞翔毘沙门天」","未填坑");
		spellCardInfoMap.put("式神「仙狐思念」","未填坑");
		spellCardInfoMap.put("式神「十二神将之宴」","未填坑");
		spellCardInfoMap.put("式辉「狐狸妖怪激光」","未填坑");
		spellCardInfoMap.put("式辉「迷人的四面楚歌」","未填坑");
		spellCardInfoMap.put("式辉「天狐公主 -Illusion-」","未填坑");
		spellCardInfoMap.put("式弹「往生极乐的佛教徒」","未填坑");
		spellCardInfoMap.put("式弹「片面义务契约」","未填坑");
		spellCardInfoMap.put("式神「橙」","未填坑");
		spellCardInfoMap.put("「狐狗狸的契约」","未填坑");
		spellCardInfoMap.put("幻神「饭纲权现降临」","未填坑");
		spellCardInfoMap.put("式神「前鬼后鬼的守护」","未填坑");
		spellCardInfoMap.put("式神「凭依荼吉尼天」","未填坑");
		spellCardInfoMap.put("结界「梦境与现实的诅咒」","未填坑");
		spellCardInfoMap.put("结界「动与静的均衡」","未填坑");
		spellCardInfoMap.put("结界「光与暗的网孔」","未填坑");
		spellCardInfoMap.put("罔两「直与曲的梦乡」","未填坑");
		spellCardInfoMap.put("罔两「八云紫的神隐」","未填坑");
		spellCardInfoMap.put("罔两「栖息于禅寺的妖蝶」","未填坑");
		spellCardInfoMap.put("魍魉「二重黑死蝶」","未填坑");
		spellCardInfoMap.put("式神「八云蓝」","未填坑");
		spellCardInfoMap.put("「人与妖的境界」","未填坑");
		spellCardInfoMap.put("结界「生与死的境界」","未填坑");
		spellCardInfoMap.put("紫奥义「弹幕结界」","未填坑");
		spellCardInfoMap.put("萤符「地上的流星」","未填坑");
		spellCardInfoMap.put("萤符「地上的彗星」","未填坑");
		spellCardInfoMap.put("灯符「萤光现象」","未填坑");
		spellCardInfoMap.put("蠢符「小虫」","未填坑");
		spellCardInfoMap.put("蠢符「小虫风暴」","未填坑");
		spellCardInfoMap.put("蠢符「夜虫风暴」","未填坑");
		spellCardInfoMap.put("蠢符「夜虫龙卷」","未填坑");
		spellCardInfoMap.put("声符「枭的夜鸣声」","未填坑");
		spellCardInfoMap.put("声符「木菟的咆哮」","未填坑");
		spellCardInfoMap.put("蛾符「天蛾的蛊道」","未填坑");
		spellCardInfoMap.put("毒符「毒蛾的鳞粉」","未填坑");
		spellCardInfoMap.put("猛毒「毒蛾的黑暗演舞」","未填坑");
		spellCardInfoMap.put("鹰符「祸延疾冲」","未填坑");
		spellCardInfoMap.put("夜盲「夜雀之歌」","未填坑");
		spellCardInfoMap.put("产灵「最初的金字塔」","未填坑");
		spellCardInfoMap.put("始符「短暂的137」","未填坑");
		spellCardInfoMap.put("野符「武烈的危机」","未填坑");
		spellCardInfoMap.put("野符「将门的危机」","未填坑");
		spellCardInfoMap.put("野符「义满的危机」","未填坑");
		spellCardInfoMap.put("野符「GHQ的危机」","未填坑");
		spellCardInfoMap.put("国符「三种神器 剑」","未填坑");
		spellCardInfoMap.put("国符「三种神器 玉」","未填坑");
		spellCardInfoMap.put("国符「三种神器 镜」","未填坑");
		spellCardInfoMap.put("国体「三种神器 乡」","未填坑");
		spellCardInfoMap.put("终符「幻想天皇」","未填坑");
		spellCardInfoMap.put("虚史「幻想乡传说」","未填坑");
		spellCardInfoMap.put("梦符「二重结界」","未填坑");
		spellCardInfoMap.put("梦境「二重大结界」","未填坑");
		spellCardInfoMap.put("灵符「梦想封印 散」","未填坑");
		spellCardInfoMap.put("散灵「梦想封印 寂」","未填坑");
		spellCardInfoMap.put("梦符「封魔阵」","未填坑");
		spellCardInfoMap.put("神技「八方鬼缚阵」","未填坑");
		spellCardInfoMap.put("神技「八方龙杀阵」","未填坑");
		spellCardInfoMap.put("灵符「梦想封印 集」","未填坑");
		spellCardInfoMap.put("回灵「梦想封印 侘」","未填坑");
		spellCardInfoMap.put("境界「二重弹幕结界」","未填坑");
		spellCardInfoMap.put("大结界「博丽弹幕结界」","未填坑");
		spellCardInfoMap.put("魔符「银河」","未填坑");
		spellCardInfoMap.put("魔空「小行星带」","未填坑");
		spellCardInfoMap.put("魔符「星尘幻想」","未填坑");
		spellCardInfoMap.put("黑魔「黑洞边缘」","未填坑");
		spellCardInfoMap.put("恋符「非定向光线」","未填坑");
		spellCardInfoMap.put("恋风「星光台风」","未填坑");
		spellCardInfoMap.put("恋符「极限火花」","未填坑");
		spellCardInfoMap.put("恋心「二重火花」","未填坑");
		spellCardInfoMap.put("光符「地球光」","未填坑");
		spellCardInfoMap.put("光击「击月」","未填坑");
		spellCardInfoMap.put("波符「赤眼催眠(Mind Shaker)」","未填坑");
		spellCardInfoMap.put("幻波「赤眼催眠(Mind Blowing)」","未填坑");
		spellCardInfoMap.put("狂符「幻视调律(Visionary Tuning)」","未填坑");
		spellCardInfoMap.put("狂视「狂视调律(Illusion Seeker)」","未填坑");
		spellCardInfoMap.put("懒符「生神停止(Idling Wave)」","未填坑");
		spellCardInfoMap.put("懒惰「生神停止(Mind Stopper)」","未填坑");
		spellCardInfoMap.put("散符「真实之月(Invisible Full Moon)」","未填坑");
		spellCardInfoMap.put("天丸「壶中的天地」","未填坑");
		spellCardInfoMap.put("觉神「神代的记忆」","未填坑");
		spellCardInfoMap.put("神符「天人的族谱」","未填坑");
		spellCardInfoMap.put("苏活「生命游戏 -Life Game-」","未填坑");
		spellCardInfoMap.put("苏生「Rising Game」","未填坑");
		spellCardInfoMap.put("操神「思兼装置」","未填坑");
		spellCardInfoMap.put("神脑「思兼的头脑」","未填坑");
		spellCardInfoMap.put("天咒「阿波罗13」","未填坑");
		spellCardInfoMap.put("秘术「天文密葬法」","未填坑");
		spellCardInfoMap.put("药符「壶中的大银河」","未填坑");
		spellCardInfoMap.put("难题「龙颈之玉 -五色的弹丸-」","未填坑");
		spellCardInfoMap.put("神宝「耀眼的龙玉」","未填坑");
		spellCardInfoMap.put("难题「佛御石之钵 -不碎的意志-」","未填坑");
		spellCardInfoMap.put("神宝「佛体的金刚石」","未填坑");
		spellCardInfoMap.put("难题「火鼠的皮衣 -不焦躁的内心-」","未填坑");
		spellCardInfoMap.put("神宝「火蜥蜴之盾」","未填坑");
		spellCardInfoMap.put("难题「燕的子安贝 -永命线-」","未填坑");
		spellCardInfoMap.put("神宝「无限的生命之泉」","未填坑");
		spellCardInfoMap.put("难题「蓬莱的弹枝 -七色的弹幕-」","未填坑");
		spellCardInfoMap.put("神宝「蓬莱的玉枝 -梦色之乡-」","未填坑");
		spellCardInfoMap.put("旧史「旧秘境史 -古代史-」","未填坑");
		spellCardInfoMap.put("转世「一条归桥」","未填坑");
		spellCardInfoMap.put("新史「新幻想史 -现代史-」","未填坑");
		spellCardInfoMap.put("时效「月岩笠的诅咒」","未填坑");
		spellCardInfoMap.put("不死「火鸟 -凤翼天翔-」","未填坑");
		spellCardInfoMap.put("藤原「灭罪寺院伤」","未填坑");
		spellCardInfoMap.put("不死「徐福时空」","未填坑");
		spellCardInfoMap.put("灭罪「正直者之死」","未填坑");
		spellCardInfoMap.put("虚人「无一」","未填坑");
		spellCardInfoMap.put("不灭「不死鸟之尾」","未填坑");
		spellCardInfoMap.put("蓬莱「凯风快晴 -Fujiyama Volcano-」","未填坑");
		spellCardInfoMap.put("「不死鸟附体」","未填坑");
		spellCardInfoMap.put("「蓬莱人形」","未填坑");
		spellCardInfoMap.put("「不合时令的蝶雨」","未填坑");
		spellCardInfoMap.put("「失明的夜雀」","未填坑");
		spellCardInfoMap.put("「日出之国的天子」","未填坑");
		spellCardInfoMap.put("「远古的骗术」","未填坑");
		spellCardInfoMap.put("「月的红眼」","未填坑");
		spellCardInfoMap.put("「天网蛛网捕蝶之法」","未填坑");
		spellCardInfoMap.put("「蓬莱的树海」","未填坑");
		spellCardInfoMap.put("「不死鸟重生」","未填坑");
		spellCardInfoMap.put("「似有似无的净化」","未填坑");
		spellCardInfoMap.put("「梦想天生」","未填坑");
		spellCardInfoMap.put("「彗星」","未填坑");
		spellCardInfoMap.put("「收缩的世界」","未填坑");
		spellCardInfoMap.put("「待宵反射卫星斩」","未填坑");
		spellCardInfoMap.put("「猎奇剧团的怪人」","未填坑");
		spellCardInfoMap.put("「绯红的宿命」","未填坑");
		spellCardInfoMap.put("「西行寺无余涅槃」","未填坑");
		spellCardInfoMap.put("「深弹幕结界 梦幻泡影」","未填坑");
		spellCardInfoMap.put("叶符「狂舞的落叶」","未填坑");
		spellCardInfoMap.put("秋符「秋季的天空」","未填坑");
		spellCardInfoMap.put("秋符「无常秋日与少女的心」","未填坑");
		spellCardInfoMap.put("丰符「大年收获者」","未填坑");
		spellCardInfoMap.put("丰收「谷物神的允诺」","未填坑");
		spellCardInfoMap.put("厄符「厄运」","未填坑");
		spellCardInfoMap.put("厄符「厄神大人的生理节律」","未填坑");
		spellCardInfoMap.put("疵符「破裂的护符」","未填坑");
		spellCardInfoMap.put("疵痕「损坏的护身符」","未填坑");
		spellCardInfoMap.put("恶灵「厄运之轮」","未填坑");
		spellCardInfoMap.put("悲运「大钟婆之火」","未填坑");
		spellCardInfoMap.put("创符「痛苦之流」","未填坑");
		spellCardInfoMap.put("创符「流刑人偶」","未填坑");
		spellCardInfoMap.put("光学「光学迷彩」","未填坑");
		spellCardInfoMap.put("光学「水迷彩」","未填坑");
		spellCardInfoMap.put("洪水「泥浆泛滥」","未填坑");
		spellCardInfoMap.put("洪水「冲积梦魇」","未填坑");
		spellCardInfoMap.put("漂溺「粼粼水底之心伤」","未填坑");
		spellCardInfoMap.put("水符「河童之河口浪潮」","未填坑");
		spellCardInfoMap.put("水符「河童之山洪暴发」","未填坑");
		spellCardInfoMap.put("水符「河童之幻想大瀑布」","未填坑");
		spellCardInfoMap.put("河童「妖怪黄瓜」","未填坑");
		spellCardInfoMap.put("河童「延展手臂」","未填坑");
		spellCardInfoMap.put("河童「回转顶板」","未填坑");
		spellCardInfoMap.put("岐符「天之八衢」","未填坑");
		spellCardInfoMap.put("岐符「猿田彦神之岔路」","未填坑");
		spellCardInfoMap.put("风神「风神木叶隐身术」","未填坑");
		spellCardInfoMap.put("风神「天狗颪」","未填坑");
		spellCardInfoMap.put("风神「二百十日」","未填坑");
		spellCardInfoMap.put("「幻想风靡」","未填坑");
		spellCardInfoMap.put("「无双风神」","未填坑");
		spellCardInfoMap.put("塞符「山神渡御」","未填坑");
		spellCardInfoMap.put("塞符「天孙降临」","未填坑");
		spellCardInfoMap.put("塞符「天上天下的照国」","未填坑");
		spellCardInfoMap.put("秘术「灰色奇术」","未填坑");
		spellCardInfoMap.put("秘术「遗忘之祭仪」","未填坑");
		spellCardInfoMap.put("秘术「一脉相传的弹幕」","未填坑");
		spellCardInfoMap.put("奇迹「白昼的客星」","未填坑");
		spellCardInfoMap.put("奇迹「客星璀璨之夜」","未填坑");
		spellCardInfoMap.put("奇迹「客星辉煌之夜」","未填坑");
		spellCardInfoMap.put("开海「割海成路之日」","未填坑");
		spellCardInfoMap.put("开海「摩西之奇迹」","未填坑");
		spellCardInfoMap.put("准备「呼唤神风的星之仪式」","未填坑");
		spellCardInfoMap.put("准备「召请建御名方神」","未填坑");
		spellCardInfoMap.put("奇迹「神之风」","未填坑");
		spellCardInfoMap.put("大奇迹「八坂之神风」","未填坑");
		spellCardInfoMap.put("神祭「扩展御柱」","未填坑");
		spellCardInfoMap.put("奇祭「目处梃子乱舞」","未填坑");
		spellCardInfoMap.put("筒粥「神の粥」","未填坑");
		spellCardInfoMap.put("忘谷「遗忘之谷」","未填坑");
		spellCardInfoMap.put("神谷「神谕之谷」","未填坑");
		spellCardInfoMap.put("贽符「御射山御狩神事」","未填坑");
		spellCardInfoMap.put("神秘「葛泉清水」","未填坑");
		spellCardInfoMap.put("神秘「大和茅环」","未填坑");
		spellCardInfoMap.put("天流「天水奇迹」","未填坑");
		spellCardInfoMap.put("天龙「雨之源泉」","未填坑");
		spellCardInfoMap.put("「信仰之山」","麻将山上麻将飞,麻将山下残机堆");
		spellCardInfoMap.put("「风神之神德」","麻将山上麻将飞,麻将山下残机堆");
		spellCardInfoMap.put("神符「如水眼之美丽源泉」","未填坑");
		spellCardInfoMap.put("神符「结于杉木之古缘」","未填坑");
		spellCardInfoMap.put("神符「神所踏足之御神渡」","未填坑");
		spellCardInfoMap.put("开宴「二拜二拍一拜」","未填坑");
		spellCardInfoMap.put("土著神「手长足长大人」","未填坑");
		spellCardInfoMap.put("神具「洩矢的铁轮」","未填坑");
		spellCardInfoMap.put("源符「厌川的翡翠」","未填坑");
		spellCardInfoMap.put("蛙狩「蛙以口鸣，方致蛇祸」","未填坑");
		spellCardInfoMap.put("土著神「七石七木」","未填坑");
		spellCardInfoMap.put("土著神「小小青蛙不输风雨」","未填坑");
		spellCardInfoMap.put("土著神「宝永四年的赤蛙」","未填坑");
		spellCardInfoMap.put("「诹访大战 ~ 土著神话 vs 中央神话」","未填坑");
		spellCardInfoMap.put("祟符「洩矢大人」","未填坑");
		spellCardInfoMap.put("怪奇「钓瓶落之怪」","未填坑");
		spellCardInfoMap.put("罠符「捕捉之网」","未填坑");
		spellCardInfoMap.put("蜘蛛「石窟的蜘蛛巢」","未填坑");
		spellCardInfoMap.put("瘴符「瘴气场」","未填坑");
		spellCardInfoMap.put("瘴气「原因不明的热病」","未填坑");
		spellCardInfoMap.put("妒符「绿眼怪兽」","未填坑");
		spellCardInfoMap.put("嫉妒「看不见的绿眼怪兽」","未填坑");
		spellCardInfoMap.put("开花爷爷「对华丽的仁者之嫉妒」","未填坑");
		spellCardInfoMap.put("开花爷爷「小白的灰烬」","未填坑");
		spellCardInfoMap.put("剪舌麻雀「对谦虚的富者之记恨」","未填坑");
		spellCardInfoMap.put("剪舌麻雀「大葛笼与小葛笼」","未填坑");
		spellCardInfoMap.put("恨符「丑时参拜」","未填坑");
		spellCardInfoMap.put("恨符「丑时参拜第七日」","未填坑");
		spellCardInfoMap.put("鬼符「怪力乱神」","未填坑");
		spellCardInfoMap.put("怪轮「地狱之苦轮」","未填坑");
		spellCardInfoMap.put("枷符「罪人不释之枷」","未填坑");
		spellCardInfoMap.put("力业「大江山岚」","未填坑");
		spellCardInfoMap.put("力业「大江山颪」","未填坑");
		spellCardInfoMap.put("四天王奥义「三步必杀」","未填坑");
		spellCardInfoMap.put("想起「恐怖的回忆」","未填坑");
		spellCardInfoMap.put("想起「恐怖催眠术」","未填坑");
		spellCardInfoMap.put("想起「二重黑死蝶」","未填坑");
		spellCardInfoMap.put("想起「飞行虫之巢」","未填坑");
		spellCardInfoMap.put("想起「波与粒的境界」","未填坑");
		spellCardInfoMap.put("想起「户隐山之投」","未填坑");
		spellCardInfoMap.put("想起「百万鬼夜行」","未填坑");
		spellCardInfoMap.put("想起「蒙蒙迷雾」","未填坑");
		spellCardInfoMap.put("想起「风神木叶隐身术」","未填坑");
		spellCardInfoMap.put("想起「天狗巨暴流」","未填坑");
		spellCardInfoMap.put("想起「鸟居旋风」","未填坑");
		spellCardInfoMap.put("想起「春之京人偶」","未填坑");
		spellCardInfoMap.put("想起「稻草人敢死队」","未填坑");
		spellCardInfoMap.put("想起「回归虚无」","未填坑");
		spellCardInfoMap.put("想起「水银之毒」","未填坑");
		spellCardInfoMap.put("想起「水精公主」","未填坑");
		spellCardInfoMap.put("想起「贤者之石」","未填坑");
		spellCardInfoMap.put("想起「延展手臂」","未填坑");
		spellCardInfoMap.put("想起「河童之河口浪潮」","未填坑");
		spellCardInfoMap.put("想起「粼粼水底之心伤」","未填坑");
		spellCardInfoMap.put("猫符「猫的步伐」","未填坑");
		spellCardInfoMap.put("猫符「怨灵猫乱步」","未填坑");
		spellCardInfoMap.put("咒精「僵尸妖精」","未填坑");
		spellCardInfoMap.put("咒精「怨灵凭依妖精」","未填坑");
		spellCardInfoMap.put("恨灵「脾脏蛀食者」","未填坑");
		spellCardInfoMap.put("尸灵「食人怨灵」","未填坑");
		spellCardInfoMap.put("赎罪「旧地狱的针山」","未填坑");
		spellCardInfoMap.put("赎罪「古时之针与痛楚的怨灵」","未填坑");
		spellCardInfoMap.put("「死灰复燃」","未填坑");
		spellCardInfoMap.put("「小恶灵复活」","未填坑");
		spellCardInfoMap.put("妖怪「火焰的车轮」","未填坑");
		spellCardInfoMap.put("核热「核聚变」","未填坑");
		spellCardInfoMap.put("核热「核功率骤增」","未填坑");
		spellCardInfoMap.put("核热「核反应失控」","未填坑");
		spellCardInfoMap.put("爆符「小型耀斑」","未填坑");
		spellCardInfoMap.put("爆符「百万耀斑」","未填坑");
		spellCardInfoMap.put("爆符「十亿耀斑」","未填坑");
		spellCardInfoMap.put("爆符「千兆耀斑」","未填坑");
		spellCardInfoMap.put("焰星「恒星」","未填坑");
		spellCardInfoMap.put("焰星「行星公转」","未填坑");
		spellCardInfoMap.put("焰星「十凶星」","未填坑");
		spellCardInfoMap.put("「地狱极乐熔毁」","未填坑");
		spellCardInfoMap.put("「地狱的托卡马克」","未填坑");
		spellCardInfoMap.put("「地狱的人工太阳」","未填坑");
		spellCardInfoMap.put("「地底太阳」","未填坑");
		spellCardInfoMap.put("秘法「九字切」","未填坑");
		spellCardInfoMap.put("奇迹「神秘果」","未填坑");
		spellCardInfoMap.put("神德「五谷丰穰米之浴」","未填坑");
		spellCardInfoMap.put("表象「先祖托梦」","未填坑");
		spellCardInfoMap.put("表象「弹幕偏执症」","未填坑");
		spellCardInfoMap.put("本能「本我的解放」","未填坑");
		spellCardInfoMap.put("抑制「超我」","未填坑");
		spellCardInfoMap.put("反应「妖怪测谎机」","未填坑");
		spellCardInfoMap.put("潜意识「弹幕的墨迹测验」","未填坑");
		spellCardInfoMap.put("复燃「恋爱的埋火」","未填坑");
		spellCardInfoMap.put("深层「潜意识的基因」","未填坑");
		spellCardInfoMap.put("「被厌恶者的哲学」","未填坑");
		spellCardInfoMap.put("「地底蔷薇」","未填坑");
		spellCardInfoMap.put("棒符「忙碌探知棒」","未填坑");
		spellCardInfoMap.put("搜符「稀有金属探测器」","未填坑");
		spellCardInfoMap.put("搜符「G黄金探测器」","未填坑");
		spellCardInfoMap.put("视符「娜兹玲灵摆」","未填坑");
		spellCardInfoMap.put("视符「高感度娜兹玲灵摆」","未填坑");
		spellCardInfoMap.put("守符「灵摆防御」","未填坑");
		spellCardInfoMap.put("大轮「唐伞光晕」","未填坑");
		spellCardInfoMap.put("大轮「Hello,Forgotten World」","未填坑");
		spellCardInfoMap.put("伞符「雨伞的星之交响」","未填坑");
		spellCardInfoMap.put("伞符「雨伞的星之追忆」","未填坑");
		spellCardInfoMap.put("雨符「雨夜怪谈」","未填坑");
		spellCardInfoMap.put("雨伞「超防水干爽伞妖」","未填坑");
		spellCardInfoMap.put("化符「遗忘之伞的夜行列车」","未填坑");
		spellCardInfoMap.put("化铁「备用伞特急夜晚狂欢号」","未填坑");
		spellCardInfoMap.put("铁拳「问答无用妖怪拳」","未填坑");
		spellCardInfoMap.put("神拳「凌云地狱冲」","未填坑");
		spellCardInfoMap.put("神拳「天海地狱冲」","未填坑");
		spellCardInfoMap.put("拳符「天网沙袋」","未填坑");
		spellCardInfoMap.put("连打「云界海妖来袭」","未填坑");
		spellCardInfoMap.put("连打「帝王海妖来袭」","未填坑");
		spellCardInfoMap.put("拳打「重拳碎击」","未填坑");
		spellCardInfoMap.put("溃灭「天上天下连续勾拳」","未填坑");
		spellCardInfoMap.put("大喝「守旧尊老之怒眼」","未填坑");
		spellCardInfoMap.put("忿怒「天变巨眼焚身」","未填坑");
		spellCardInfoMap.put("忿怒「空前绝后巨眼焚身」","未填坑");
		spellCardInfoMap.put("倾覆「同路人之锚」","未填坑");
		spellCardInfoMap.put("倾覆「沉没之锚」","未填坑");
		spellCardInfoMap.put("倾覆「击沉之锚」","未填坑");
		spellCardInfoMap.put("溺符「深海漩涡」","未填坑");
		spellCardInfoMap.put("溺符「沉底漩涡」","未填坑");
		spellCardInfoMap.put("港符「幽灵船之泊」","未填坑");
		spellCardInfoMap.put("港符「幽灵船之港」","未填坑");
		spellCardInfoMap.put("港符「幽灵船永久停泊」","未填坑");
		spellCardInfoMap.put("幽灵「Sinker Ghost」","未填坑");
		spellCardInfoMap.put("幽灵「悄然袭来的长勺」","未填坑");
		spellCardInfoMap.put("宝塔「最优良的宝物」","未填坑");
		spellCardInfoMap.put("宝塔「光辉之宝」","未填坑");
		spellCardInfoMap.put("宝塔「光辉宝枪」","未填坑");
		spellCardInfoMap.put("光符「绝对正义」","未填坑");
		spellCardInfoMap.put("光符「正义之威光」","未填坑");
		spellCardInfoMap.put("法力「至宝之独钴杵」","未填坑");
		spellCardInfoMap.put("法灯「无瑕佛法之独钴杵」","未填坑");
		spellCardInfoMap.put("光符「净化之魔」","未填坑");
		spellCardInfoMap.put("「完全净化」","未填坑");
		spellCardInfoMap.put("魔法「紫云之兆」","未填坑");
		spellCardInfoMap.put("吉兆「紫色云路」","未填坑");
		spellCardInfoMap.put("吉兆「极乐的紫色云路」","未填坑");
		spellCardInfoMap.put("魔法「魔界蝶之妖香」","未填坑");
		spellCardInfoMap.put("魔法「魔法之蝶」","未填坑");
		spellCardInfoMap.put("光魔「星辰漩涡」","未填坑");
		spellCardInfoMap.put("光魔「魔法银河」","未填坑");
		spellCardInfoMap.put("大魔法「魔神复诵」","未填坑");
		spellCardInfoMap.put("「圣尼公的气之魔法卷轴」","未填坑");
		spellCardInfoMap.put("超人「圣白莲」","未填坑");
		spellCardInfoMap.put("飞钵「飞行幻想」","未填坑");
		spellCardInfoMap.put("飞钵「传说的飞空圆盘」","未填坑");
		spellCardInfoMap.put("伞符「大颗的泪雨」","未填坑");
		spellCardInfoMap.put("惊雨「台风骤雨」","未填坑");
		spellCardInfoMap.put("光晕「唐伞惊吓闪光」","未填坑");
		spellCardInfoMap.put("妖云「平安时代的黑云」","未填坑");
		spellCardInfoMap.put("真相不明「愤怒的红色UFO袭来」","未填坑");
		spellCardInfoMap.put("鵺符「鵺的蛇行表演」","未填坑");
		spellCardInfoMap.put("真相不明「哀愁的蓝色UFO袭来」","未填坑");
		spellCardInfoMap.put("鵺符「弹幕奇美拉」","未填坑");
		spellCardInfoMap.put("真相不明「忠义的绿色UFO袭来」","未填坑");
		spellCardInfoMap.put("鵺符「真相不明的黑暗」","未填坑");
		spellCardInfoMap.put("真相不明「恐怖的虹色UFO袭来」","未填坑");
		spellCardInfoMap.put("「平安京的恶梦」","未填坑");
		spellCardInfoMap.put("恨弓「源三位赖政之弓」","未填坑");
		spellCardInfoMap.put("符牒「死蝶之舞」","未填坑");
		spellCardInfoMap.put("符牒「死蝶之舞 -樱花-」","未填坑");
		spellCardInfoMap.put("幽蝶「幽魂聚地」","未填坑");
		spellCardInfoMap.put("幽蝶「幽魂聚地 -樱花-」","未填坑");
		spellCardInfoMap.put("冥符「常夜樱」","未填坑");
		spellCardInfoMap.put("樱符「西行樱吹雪」","未填坑");
		spellCardInfoMap.put("响符「山谷回声」","未填坑");
		spellCardInfoMap.put("响符「混乱的山谷回声」","未填坑");
		spellCardInfoMap.put("响符「强力共振」","未填坑");
		spellCardInfoMap.put("山彦「远距离回声」","未填坑");
		spellCardInfoMap.put("山彦「扩大回声」","未填坑");
		spellCardInfoMap.put("大声「激动的呼喊」","未填坑");
		spellCardInfoMap.put("大声「激动Yahoo」","未填坑");
		spellCardInfoMap.put("虹符「雨伞风暴」","未填坑");
		spellCardInfoMap.put("回复「借由欲望的恢复」","未填坑");
		spellCardInfoMap.put("毒爪「剧毒抹消」","未填坑");
		spellCardInfoMap.put("毒爪「剧毒杀害」","未填坑");
		spellCardInfoMap.put("欲符「赚钱欲灵招来」","未填坑");
		spellCardInfoMap.put("欲灵「贪分欲吞噬者」","未填坑");
		spellCardInfoMap.put("邪符「养小鬼」","未填坑");
		spellCardInfoMap.put("邪符「孤魂野鬼」","未填坑");
		spellCardInfoMap.put("入魔「走火入魔」","未填坑");
		spellCardInfoMap.put("降灵「死人童乩」","未填坑");
		spellCardInfoMap.put("通灵「通灵芳香」","未填坑");
		spellCardInfoMap.put("道符「道胎动」","未填坑");
		spellCardInfoMap.put("雷矢「元兴寺的旋风」","未填坑");
		spellCardInfoMap.put("雷矢「元兴寺的龙卷」","未填坑");
		spellCardInfoMap.put("天符「雨之磐舟」","未填坑");
		spellCardInfoMap.put("天符「天之磐舟哟，向天飞升吧」","未填坑");
		spellCardInfoMap.put("投皿「物部氏的八十平瓮」","未填坑");
		spellCardInfoMap.put("炎符「废佛之炎风」","未填坑");
		spellCardInfoMap.put("炎符「火烧樱井寺」","未填坑");
		spellCardInfoMap.put("圣童女「大物忌正餐」","未填坑");
		spellCardInfoMap.put("名誉「十二阶之色彩」","未填坑");
		spellCardInfoMap.put("名誉「十二阶之冠位」","未填坑");
		spellCardInfoMap.put("仙符「日出之处的道士」","未填坑");
		spellCardInfoMap.put("仙符「日出之处的天子」","未填坑");
		spellCardInfoMap.put("召唤「豪族乱舞」","未填坑");
		spellCardInfoMap.put("秘宝「斑鸠寺的天球仪」","未填坑");
		spellCardInfoMap.put("秘宝「圣德太子的欧帕兹」","未填坑");
		spellCardInfoMap.put("光符「救世观音的佛光」","未填坑");
		spellCardInfoMap.put("光符「救世之光」","未填坑");
		spellCardInfoMap.put("眼光「十七条的光芒」","未填坑");
		spellCardInfoMap.put("神光「无忤为宗」","未填坑");
		spellCardInfoMap.put("「星辰降落的神灵庙」","未填坑");
		spellCardInfoMap.put("「新生的神灵」","未填坑");
		spellCardInfoMap.put("未知「轨道不明的鬼火」","未填坑");
		spellCardInfoMap.put("未知「姿态不明的空鱼」","未填坑");
		spellCardInfoMap.put("未知「原理不明的妖怪玉」","未填坑");
		spellCardInfoMap.put("一回胜负「灵长类弹幕变化」","未填坑");
		spellCardInfoMap.put("二回胜负「肉食类弹幕变化」","未填坑");
		spellCardInfoMap.put("三回胜负「羽鸟类弹幕变化」","未填坑");
		spellCardInfoMap.put("四回胜负「两栖类弹幕变化」","未填坑");
		spellCardInfoMap.put("五回胜负「鸟兽戏画」","未填坑");
		spellCardInfoMap.put("六回胜负「狸猫的变身学校」","未填坑");
		spellCardInfoMap.put("七回胜负「野生的离岛」","未填坑");
		spellCardInfoMap.put("变化「魔奴化巫女的伪退治」","未填坑");
		spellCardInfoMap.put("「猯藏化弹幕十变化」","未填坑");
		spellCardInfoMap.put("貉符「满月下的腹鼓舞」","未填坑");
		spellCardInfoMap.put("樱符「樱吹雪地狱」","未填坑");
		spellCardInfoMap.put("山彦「山彦的发挥本领之回音」","未填坑");
		spellCardInfoMap.put("毒爪「不死的杀人鬼」","未填坑");
		spellCardInfoMap.put("道符「TAO胎动 ~道~」","未填坑");
		spellCardInfoMap.put("怨灵「入鹿之雷」","未填坑");
		spellCardInfoMap.put("圣童女「太阳神的贡品」","未填坑");
		spellCardInfoMap.put("「神灵大宇宙」","未填坑");
		spellCardInfoMap.put("「Wild Carpet」","未填坑");
		spellCardInfoMap.put("冰符「Ultimate Blizzard」","未填坑");
		spellCardInfoMap.put("水符「尾鳍拍击」","未填坑");
		spellCardInfoMap.put("鳞符「鳞之波」","未填坑");
		spellCardInfoMap.put("鳞符「逆鳞的惊涛」","未填坑");
		spellCardInfoMap.put("鳞符「逆鳞的大惊涛」","未填坑");
		spellCardInfoMap.put("飞符「飞行之头」","未填坑");
		spellCardInfoMap.put("首符「闭目射击」","未填坑");
		spellCardInfoMap.put("首符「辘轳首飞来」","未填坑");
		spellCardInfoMap.put("飞头「倍增之头」","未填坑");
		spellCardInfoMap.put("飞头「第七个头」","未填坑");
		spellCardInfoMap.put("飞头「第九个头」","未填坑");
		spellCardInfoMap.put("飞头「杜拉罕之夜」","未填坑");
		spellCardInfoMap.put("牙符「月下的犬齿」","未填坑");
		spellCardInfoMap.put("变身「三角齿」","未填坑");
		spellCardInfoMap.put("变身「星形齿」","未填坑");
		spellCardInfoMap.put("咆哮「陌生的咆哮」","未填坑");
		spellCardInfoMap.put("咆哮「满月的远吠」","未填坑");
		spellCardInfoMap.put("狼符「星环猛扑」","未填坑");
		spellCardInfoMap.put("天狼「高速猛扑」","未填坑");
		spellCardInfoMap.put("琴符「诸行无常的琴声」","未填坑");
		spellCardInfoMap.put("响符「平安的残响」","未填坑");
		spellCardInfoMap.put("响符「回音之庭」","未填坑");
		spellCardInfoMap.put("筝曲「下克上送筝曲」","未填坑");
		spellCardInfoMap.put("筝曲「下克上安魂曲」","未填坑");
		spellCardInfoMap.put("平曲「祗园精舍的钟声」","未填坑");
		spellCardInfoMap.put("怨灵「无耳芳一」","未填坑");
		spellCardInfoMap.put("怨灵「平家的大怨灵」","未填坑");
		spellCardInfoMap.put("乐符「邪恶的五线谱」","未填坑");
		spellCardInfoMap.put("乐符「凶恶的五线谱」","未填坑");
		spellCardInfoMap.put("乐符「Double Score」","未填坑");
		spellCardInfoMap.put("欺符「逆针击」","未填坑");
		spellCardInfoMap.put("逆符「镜之国的弹幕」","未填坑");
		spellCardInfoMap.put("逆符「镜中的邪恶」","未填坑");
		spellCardInfoMap.put("逆符「天地有用」","未填坑");
		spellCardInfoMap.put("逆符「天下翻覆」","未填坑");
		spellCardInfoMap.put("逆弓「天壤梦弓」","未填坑");
		spellCardInfoMap.put("逆弓「天壤梦弓的诏敕」","未填坑");
		spellCardInfoMap.put("逆转「阶级反转」","未填坑");
		spellCardInfoMap.put("逆转「变革空勇士」","未填坑");
		spellCardInfoMap.put("小弹「小人的道路」","未填坑");
		spellCardInfoMap.put("小弹「小人的荆棘路」","未填坑");
		spellCardInfoMap.put("小槌「变大吧」","未填坑");
		spellCardInfoMap.put("小槌「变得更大吧」","未填坑");
		spellCardInfoMap.put("妖剑「辉针剑」","未填坑");
		spellCardInfoMap.put("小槌「你给我变大吧」","未填坑");
		spellCardInfoMap.put("「进击的小人」","未填坑");
		spellCardInfoMap.put("「一寸之壁」","未填坑");
		spellCardInfoMap.put("「七个小拇指」","未填坑");
		spellCardInfoMap.put("「七个一寸法师」","未填坑");
		spellCardInfoMap.put("弦乐「风暴的合奏」","未填坑");
		spellCardInfoMap.put("弦乐「净琉璃世界」","未填坑");
		spellCardInfoMap.put("一鼓「暴乱宫太鼓」","未填坑");
		spellCardInfoMap.put("二鼓「怨灵绫鼓」","未填坑");
		spellCardInfoMap.put("三鼓「午夜零时的三振」","未填坑");
		spellCardInfoMap.put("死鼓「轻敲大地」","未填坑");
		spellCardInfoMap.put("五鼓「雷电拨浪鼓」","未填坑");
		spellCardInfoMap.put("六鼓「交替打击法」","未填坑");
		spellCardInfoMap.put("七鼓「高速和太鼓火箭」","未填坑");
		spellCardInfoMap.put("八鼓「雷神之怒」","未填坑");
		spellCardInfoMap.put("「蓝色佳人的演出」","未填坑");
		spellCardInfoMap.put("「Pristine beat」","未填坑");
		spellCardInfoMap.put("凶弹「高速撞击」","未填坑");
		spellCardInfoMap.put("弹符「射鹰」","未填坑");
		spellCardInfoMap.put("弹符「鹰已击中」","未填坑");
		spellCardInfoMap.put("铳符「月狂之枪」","东 方 跟 着 转");
		spellCardInfoMap.put("兔符「草莓团子」","未填坑");
		spellCardInfoMap.put("兔符「浆果浆果团子」","未填坑");
		spellCardInfoMap.put("兔符「团子影响力」","未填坑");
		spellCardInfoMap.put("月见「九月的满月」","未填坑");
		spellCardInfoMap.put("月见酒「月狂的九月」","未填坑");
		spellCardInfoMap.put("梦符「绯红色的噩梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「绯红色的压迫噩梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「蔚蓝色的愁梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「蔚蓝色的愁三重梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「愁永远之梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「刈安色的迷梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「刈安色的错综迷梦」","东 方 跟 着 转");
		spellCardInfoMap.put("梦符「捕梦网」","未填坑");
		spellCardInfoMap.put("梦符「苍蓝色的捕梦网」","未填坑");
		spellCardInfoMap.put("梦符「梦我梦中」","未填坑");
		spellCardInfoMap.put("月符「绀色的狂梦」","简单(?)的自机相关");
		spellCardInfoMap.put("玉符「乌合之咒」","简单(?)的狙");
		spellCardInfoMap.put("玉符「乌合的逆咒」","简单(?)的狙");
		spellCardInfoMap.put("玉符「乌合的二重咒」","简单(?)的狙");
		spellCardInfoMap.put("玉符「秽身探知型水雷」","未填坑");
		spellCardInfoMap.put("玉符「秽身探知型水雷 改」","未填坑");
		spellCardInfoMap.put("玉符「众神的弹冠」","绀珠传最简单符卡");
		spellCardInfoMap.put("玉符「众神的光辉弹冠」","绀珠传最简单符卡（真）");
		spellCardInfoMap.put("「孤翼的白鹭」","多动症shadiao");
		spellCardInfoMap.put("狱符「地狱日食」","未填坑");
		spellCardInfoMap.put("狱符「地狱之蚀」","未填坑");
		spellCardInfoMap.put("狱符「闪光与条纹」","太野蛮了");
		spellCardInfoMap.put("狱符「星与条纹」","未填坑");
		spellCardInfoMap.put("狱炎「擦弹地狱火」","未填坑");
		spellCardInfoMap.put("狱炎「擦弹的狱意」","未填坑");
		spellCardInfoMap.put("地狱「条纹状的深渊」","未填坑");
		spellCardInfoMap.put("「伪阿波罗」","简单(?)的固定弹");
		spellCardInfoMap.put("「阿波罗捏造说」","简单(?)的固定弹");
		spellCardInfoMap.put("「掌上的纯光」","简单(真)的角随固");
		spellCardInfoMap.put("「杀意的百合」","东 方 初 见 杀");
		spellCardInfoMap.put("「原始的神灵界」","简单(?)的随机");
		spellCardInfoMap.put("「现代的神灵界」","简单(?)的随机");
		spellCardInfoMap.put("「战栗的寒冷之星」","简单(?)的左右左右");
		spellCardInfoMap.put("「纯粹的疯狂」","简单(?)的角随固");
		spellCardInfoMap.put("「溢出的暇秽」","简单(?)的狙");
		spellCardInfoMap.put("「地上秽的纯化」","简单(?)的狙");
		spellCardInfoMap.put("纯符「单纯的子弹地狱」","简单(bu)的随机加固定\np1:这tm是终符?你在逗我\np2:这tm是终符?\np3:这tm...\np4:这...");
		spellCardInfoMap.put("纯符「纯粹的弹幕地狱」","简单(bu)的随机加固定\np1:这tm是终符?你在逗我\np2:这tm是终符?\np3:这tm...\np4:这...");
		spellCardInfoMap.put("蝴蝶「取而代之的蝴蝶」","未填坑");
		spellCardInfoMap.put("超特急「梦幻快车」","未填坑");
		spellCardInfoMap.put("爬梦「爬行的子弹」","未填坑");
		spellCardInfoMap.put("异界「逢魔之刻」","瞎了");
		spellCardInfoMap.put("地球「邪秽在身」","未填坑");
		spellCardInfoMap.put("月「阿波罗反射镜」","未填坑");
		spellCardInfoMap.put("「用于杀人的纯粹弹幕」","未填坑");
		spellCardInfoMap.put("异界「地狱的非理想弹幕」","未填坑");
		spellCardInfoMap.put("地球「落向地狱的雨」","未填坑");
		spellCardInfoMap.put("「用于逼死瓮中鼠的单纯弹幕」","未填坑");
		spellCardInfoMap.put("月「月狂冲击」","未填坑");
		spellCardInfoMap.put("「三位一体论狂想曲」","未填坑");
		spellCardInfoMap.put("「最初与最后的无名弹幕」","野蛮的二打一");
		spellCardInfoMap.put("蝶符「细碎鳞粉」","未填坑");
		spellCardInfoMap.put("蝶符「凤蝶的鳞粉」","未填坑");
		spellCardInfoMap.put("蝶符「扑翅之夏」","未填坑");
		spellCardInfoMap.put("蝶符「盛夏振翅」","未填坑");
		spellCardInfoMap.put("雨符「被囚禁的秋雨」","未填坑");
		spellCardInfoMap.put("雨符「被诅咒的柴榑雨」","未填坑");
		spellCardInfoMap.put("刃符「山姥的菜刀研磨」","未填坑");
		spellCardInfoMap.put("刃符「山姥的鬼菜刀研磨」","未填坑");
		spellCardInfoMap.put("尽符「深山谋杀」","未填坑");
		spellCardInfoMap.put("尽符「血腥的深山谋杀」","未填坑");
		spellCardInfoMap.put("春符「惊喜之春」","未填坑");
		spellCardInfoMap.put("犬符「野犬的散步」","未填坑");
		spellCardInfoMap.put("狗符「山狗的散步」","未填坑");
		spellCardInfoMap.put("陀螺「狛犬回旋」","未填坑");
		spellCardInfoMap.put("陀螺「蜷缩死去」","未填坑");
		spellCardInfoMap.put("狛符「单人式阿吽的呼吸」","未填坑");
		spellCardInfoMap.put("魔符「顷刻菩提」","未填坑");
		spellCardInfoMap.put("魔符「即席菩提」","未填坑");
		spellCardInfoMap.put("魔符「弹丸魔像」","未填坑");
		spellCardInfoMap.put("魔符「作宠物的巨大弹生命体」","未填坑");
		spellCardInfoMap.put("地藏「罪业救赎」","未填坑");
		spellCardInfoMap.put("地藏「业火救济」","未填坑");
		spellCardInfoMap.put("竹符「竹矛之舞」","未填坑");
		spellCardInfoMap.put("竹符「竹之狂舞」","未填坑");
		spellCardInfoMap.put("笹符「七夕星祭」","未填坑");
		spellCardInfoMap.put("茗荷「忘却你的名字」","未填坑");
		spellCardInfoMap.put("冥加「在你背后」","未填坑");
		spellCardInfoMap.put("舞符「背后之祭」","未填坑");
		spellCardInfoMap.put("狂舞「天狗怖吓」","未填坑");
		spellCardInfoMap.put("狂舞「狂乱天狗怖吓」","未填坑");
		spellCardInfoMap.put("后符「秘神的后光」","未填坑");
		spellCardInfoMap.put("后符「绝对秘神的后光」","未填坑");
		spellCardInfoMap.put("里夏「暑夏炙烤」","未填坑");
		spellCardInfoMap.put("里夏「异常酷暑之焦土」","未填坑");
		spellCardInfoMap.put("里秋「死于饥荒」","未填坑");
		spellCardInfoMap.put("里秋「异常枯死之饿鬼」","未填坑");
		spellCardInfoMap.put("里冬「黑色雪人」","未填坑");
		spellCardInfoMap.put("里冬「异常降雪之雪人」","未填坑");
		spellCardInfoMap.put("里春「四月巫师」","未填坑");
		spellCardInfoMap.put("里春「异常落花之魔术使」","未填坑");
		spellCardInfoMap.put("「里·Breeze Cherry Blossom」","未填坑");
		spellCardInfoMap.put("「里·Perfect Summer Ice」","未填坑");
		spellCardInfoMap.put("「里·Crazy Fall Wind」","未填坑");
		spellCardInfoMap.put("「里·Extreme Winter」","未填坑");
		spellCardInfoMap.put("鼓舞「强力助威」","未填坑");
		spellCardInfoMap.put("狂舞「疯狂的背景舞」","未填坑");
		spellCardInfoMap.put("弹舞「双目台风」","未填坑");
		spellCardInfoMap.put("秘仪「逆向呼神者」","未填坑");
		spellCardInfoMap.put("秘仪「背叛的后方射击」","未填坑");
		spellCardInfoMap.put("秘仪「弹幕的玉茧」","未填坑");
		spellCardInfoMap.put("秘仪「秽那之火」","未填坑");
		spellCardInfoMap.put("秘仪「后户的狂言」","未填坑");
		spellCardInfoMap.put("秘仪「摩多罗苦谛」","未填坑");
		spellCardInfoMap.put("秘仪「七星之剑」","未填坑");
		spellCardInfoMap.put("秘仪「无纽带的艺人」","未填坑");
		spellCardInfoMap.put("「背面的暗黑猿乐」","未填坑");
		spellCardInfoMap.put("「无秩序弹幕地狱」","未填坑");
		spellCardInfoMap.put("石符「石林」","未填坑");
		spellCardInfoMap.put("石符「石头针叶林」","未填坑");
		spellCardInfoMap.put("石符「儿童们的灵薄狱」","未填坑");
		spellCardInfoMap.put("石符「成年儿童们的灵薄狱」","未填坑");
		spellCardInfoMap.put("石符「石头婴儿」","未填坑");
		spellCardInfoMap.put("石符「沉重的石之头婴儿」","未填坑");
		spellCardInfoMap.put("溺符「三途的沦溺」","未填坑");
		spellCardInfoMap.put("鬼符「魔鬼围城」","未填坑");
		spellCardInfoMap.put("鬼符「饿鬼围城」","未填坑");
		spellCardInfoMap.put("水符「分水的试练」","鸡你太美");
		spellCardInfoMap.put("水符「分水的上级试炼」","鸡你太美");
		spellCardInfoMap.put("水符「分水的顶级试炼」","鸡你太美");
		spellCardInfoMap.put("光符「瞭望的试练」","鸡你太美");
		spellCardInfoMap.put("光符「瞭望的上级试炼」","鸡你太美");
		spellCardInfoMap.put("光符「瞭望的顶级试炼」","鸡你太美");
		spellCardInfoMap.put("鬼符「鬼渡的试练」","鸡你太美");
		spellCardInfoMap.put("鬼符「鬼渡的上级试炼」","鸡你太美");
		spellCardInfoMap.put("鬼符「鬼渡的狱级试炼」","鸡你太美");
		spellCardInfoMap.put("龟符「龟甲地狱」","未填坑");
		spellCardInfoMap.put("鬼符「搦手之畜生」","未填坑");
		spellCardInfoMap.put("鬼符「搦手之犬畜生」","未填坑");
		spellCardInfoMap.put("鬼符「搦手之鬼畜生」","未填坑");
		spellCardInfoMap.put("龙符「龙纹弹」","未填坑");
		spellCardInfoMap.put("埴轮「弓兵埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「熟练弓兵埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「剑士埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「熟练剑士埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「骑马兵埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「熟练骑马兵埴轮」","未填坑");
		spellCardInfoMap.put("埴轮「空洞的无尽兵团」","未填坑");
		spellCardInfoMap.put("埴轮「不败的无尽兵团」","未填坑");
		spellCardInfoMap.put("方形「方形造形术」","未填坑");
		spellCardInfoMap.put("方形「方形造物」","未填坑");
		spellCardInfoMap.put("圆形「正圆造形术」","未填坑");
		spellCardInfoMap.put("圆形「圆形造物」","未填坑");
		spellCardInfoMap.put("线形「线形造形术」","未填坑");
		spellCardInfoMap.put("线形「线形造物」","未填坑");
		spellCardInfoMap.put("埴轮「偶像人马造形术」","未填坑");
		spellCardInfoMap.put("埴轮「偶像造物」","未填坑");
		spellCardInfoMap.put("「鬼形造形术」","未填坑");
		spellCardInfoMap.put("「几何造物」","未填坑");
		spellCardInfoMap.put("「Idola Diabolus」","未填坑");
		spellCardInfoMap.put("血战「血之分水岭」","鸡你太美");
		spellCardInfoMap.put("血战「狱界视线」","鸡你太美");
		spellCardInfoMap.put("血战「全灵鬼渡」","鸡你太美");
		spellCardInfoMap.put("劲疾技「惊险射击」","未填坑");
		spellCardInfoMap.put("劲疾技「闪电嘶鸣」","未填坑");
		spellCardInfoMap.put("劲疾技「浓云」","未填坑");
		spellCardInfoMap.put("劲疾技「兽性感染」","未填坑");
		spellCardInfoMap.put("劲疾技「三角追击」","未填坑");
		spellCardInfoMap.put("劲疾技「黑色天马流星弹」","未填坑");
		spellCardInfoMap.put("劲疾技「肌肉爆破」","未填坑");
		spellCardInfoMap.put("「跟我来，不要怕」","未填坑");
		spellCardInfoMap.put("「鬼形的乌合之众」","未填坑");
		spellCardInfoMap.put("「鬼畜生的所业」","未填坑");
	}
}
