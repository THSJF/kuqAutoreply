package com.meng.gameData.TouHou;

public class SpellCardInfo {
	public SpellCard sc =null;
	public String ps=null;
	public SpellCardInfo(SpellCard spc) {
		sc = spc;
		switch (sc.n) {
			case "凶弹「高速撞击」":
				ps = "暂无";
				break;
			case "弹符「射鹰」":
				ps = "暂无";
				break;
			case "弹符「鹰已击中」":
				ps = "暂无";
				break;
			case "铳符「月狂之枪」":
				ps = "暂无";
				break;
			case "兔符「草莓团子」":
				ps = "暂无";
				break;
			case "兔符「浆果浆果团子」":
				ps = "暂无";
				break;
			case "兔符「团子影响力」":
				ps = "暂无";
				break;
			case "月见「九月的满月」":
				ps = "暂无";
				break;
			case "月见酒「月狂的九月」":
				ps = "暂无";
				break;
			case "梦符「绯红色的噩梦」":
				ps = "暂无";
				break;
			case "梦符「绯红色的压迫噩梦」":
				ps = "暂无";
				break;
			case "梦符「蔚蓝色的愁梦」":
				ps = "暂无";
				break;
			case "梦符「蔚蓝色的愁三重梦」":
				ps = "暂无";
				break;
			case "梦符「愁永远之梦」":
				ps = "暂无";
				break;
			case "梦符「刈安色的迷梦」":
				ps = "暂无";
				break;
			case "梦符「刈安色的错综迷梦」":
				ps = "暂无";
				break;
			case "梦符「捕梦网」":
				ps = "暂无";
				break;
			case "梦符「苍蓝色的捕梦网」":
				ps = "暂无";
				break;
			case "梦符「梦我梦中」":
				ps = "暂无";
				break;
			case "月符「绀色的狂梦」":
				ps = "暂无";
				break;
			case "玉符「乌合之咒」":
				ps = "暂无";
				break;
			case "玉符「乌合的逆咒」":
				ps = "暂无";
				break;
			case "玉符「乌合的二重咒」":
				ps = "暂无";
				break;
			case "玉符「秽身探知型水雷」":
				ps = "暂无";
				break;
			case "玉符「秽身探知型水雷 改」":
				ps = "暂无";
				break;
			case "玉符「众神的弹冠」":
				ps = "暂无";
				break;
			case "玉符「众神的光辉弹冠」":
				ps = "暂无";
				break;
			case "「孤翼的白鹭」":
				ps = "暂无";
				break;
			case "狱符「地狱日食」":
				ps = "暂无";
				break;
			case "狱符「地狱之蚀」":
				ps = "暂无";
				break;
			case "狱符「闪光与条纹」":
				ps = "暂无";
				break;
			case "狱符「星与条纹」":
				ps = "暂无";
				break;
			case "狱炎「擦弹地狱火」":
				ps = "暂无";
				break;
			case "狱炎「擦弹的狱意」":
				ps = "暂无";
				break;
			case "地狱「条纹状的深渊」":
				ps = "暂无";
				break;
			case "「伪阿波罗」":
				ps = "暂无";
				break;
			case "「阿波罗捏造说」":
				ps = "暂无";
				break;
			case "「掌上的纯光」":
				ps = "暂无";
				break;
			case "「杀意的百合」":
				ps = "暂无";
				break;
			case "「现代的神灵界」":
				ps = "暂无";
				break;
			case "「原始的神灵界」":
				ps = "暂无";
				break;
			case "「战栗的寒冷之星」":
				ps = "暂无";
				break;
			case "「纯粹的疯狂」":
				ps = "暂无";
				break;
			case "「溢出的暇秽」":
				ps = "暂无";
				break;
			case "「地上秽的纯化」":
				ps = "暂无";
				break;
			case "纯符「单纯的子弹地狱」":
				ps = "暂无";
				break;
			case "纯符「纯粹的弹幕地狱」":
				ps = "p1:这tm是终符?你在逗我 p2:这tm是终符? p3:这tm... p4:这...";
				break;
			case "蝴蝶「取而代之的蝴蝶」":
				ps = "暂无";
				break;
			case "超特急「梦幻快车」":
				ps = "暂无";
				break;
			case "爬梦「爬行的子弹」":
				ps = "暂无";
				break;
			case "异界「逢魔之刻」":
				ps = "暂无";
				break;
			case "地球「邪秽在身」":
				ps = "暂无";
				break;
			case "月「阿波罗反射镜」":
				ps = "暂无";
				break;
			case "「用于杀人的纯粹弹幕」":
				ps = "暂无";
				break;
			case "异界「地狱的非理想弹幕」":
				ps = "暂无";
				break;
			case "地球「落向地狱的雨」":
				ps = "暂无";
				break;
			case "「用于逼死瓮中鼠的单纯弹幕」":
				ps = "暂无";
				break;
			case "月「月狂冲击」":
				ps = "暂无";
				break;
			case "「三位一体论狂想曲」":
				ps = "暂无";
				break;
			case "「最初与最后的无名弹幕」":
				ps = "暂无";
				break;		
		}
	}

	@Override
	public String toString() {
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
		sb.append(ps);
		return sb.toString();
	}

}
