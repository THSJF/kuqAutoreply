import m.*;

public class Main {
	public static void main(String[] args) {
		SpellCardBuilder scb=new SpellCardBuilder()
			.setMaster("琪露诺")
			.addSpell("冰符「Ultimate Blizzard」", 2, 3)

			.setMaster("若鹭姬")
			.addSpell("水符「尾鳍拍击」", 0, 1, 2, 3)
			.addSpell("鳞符「鳞之波」", 0, 1)
			.addSpell("鳞符「逆鳞的惊涛」", 2)
			.addSpell("鳞符「逆鳞的大惊涛」", 3)

			.setMaster("赤蛮奇")
			.addSpell("飞符「飞行之头」", 0, 1, 2, 3)
			.addSpell("首符「闭目射击」", 0, 1)
			.addSpell("首符「辘轳首飞来」", 2, 3)
			.addSpell("飞头「倍增之头」", 0, 1)
			.addSpell("飞头「第七个头」", 2)
			.addSpell("飞头「第九个头」", 3)
			.addSpell("飞头「杜拉罕之夜」", 0, 1, 2, 3)

			.setMaster("今泉影狼")
			.addSpell("牙符「月下的犬齿」", 2, 3)
			.addSpell("变身「三角齿」", 0, 1)
			.addSpell("变身「星形齿」", 2, 3)
			.addSpell("咆哮「陌生的咆哮」", 0, 1)
			.addSpell("咆哮「满月的远吠」", 2, 3)
			.addSpell("狼符「星环猛扑」", 0, 1)
			.addSpell("天狼「高速猛扑」", 2, 3)

			.setMaster("九十九八桥")
			.addSpell("琴符「诸行无常的琴声」", 0, 1, 2, 3)
			.addSpell("响符「平安的残响」", 0, 1)
			.addSpell("响符「回音之庭」", 2, 3)
			.addSpell("筝曲「下克上送筝曲」", 0, 1)
			.addSpell("筝曲「下克上安魂曲」", 2, 3)

			.setMaster("九十九弁弁")
			.addSpell("平曲「祗园精舍的钟声」", 0, 1, 2, 3)
			.addSpell("怨灵「无耳芳一」", 0, 1)
			.addSpell("怨灵「平家的大怨灵」", 2, 3)
			.addSpell("乐符「邪恶的五线谱」", 0, 1)
			.addSpell("乐符「凶恶的五线谱」",  2)
			.addSpell("乐符「Double Score」", 3)

			.setMaster("鬼人正邪")
			.addSpell("欺符「逆针击」", 0, 1, 2, 3)
			.addSpell("逆符「镜之国的弹幕」", 0, 1)
			.addSpell("逆符「镜中的邪恶」", 2, 3)
			.addSpell("逆符「天地有用」", 0, 1)
			.addSpell("逆符「天下翻覆」", 2, 3)
			.addSpell("逆弓「天壤梦弓」", 0, 1)
			.addSpell("逆弓「天壤梦弓的诏敕」", 2, 3)
			.addSpell("逆转「阶级反转」", 0, 1)
			.addSpell("逆转「变革空勇士」", 2, 3)

			.setMaster("少名针妙丸")
			.addSpell("小弹「小人的道路」", 0, 1)
			.addSpell("小弹「小人的荆棘路」", 2, 3)
			.addSpell("小槌「变大吧」", 0, 1)
			.addSpell("小槌「变得更大吧」", 2, 3)
			.addSpell("妖剑「辉针剑」", 0, 1, 2, 3)
			.addSpell("小槌「你给我变大吧」", 0, 1, 2, 3)
			.addSpell("「进击的小人」", 0, 1)
			.addSpell("「一寸之壁」", 2, 3)
			.addSpell("「七个小拇指」", 0, 1)
			.addSpell("「七个一寸法师」", 2, 3)

			.setMaster("九十九姐妹")
			.addSpell("弦乐「风暴的合奏」", 4)
			.addSpell("弦乐「净琉璃世界」", 4)

			.setMaster("堀川雷鼓")
			.addSpell("一鼓「暴乱宫太鼓」", 4)
			.addSpell("二鼓「怨灵绫鼓」", 4)
			.addSpell("三鼓「午夜零时的三振」", 4)
			.addSpell("死鼓「轻敲大地」", 4)
			.addSpell("五鼓「雷电拨浪鼓」", 4)
			.addSpell("六鼓「交替打击法」", 4)
			.addSpell("七鼓「高速和太鼓火箭」", 4)
			.addSpell("八鼓「雷神之怒」", 4)
			.addSpell("「蓝色佳人的演出」", 4)
			.addSpell("「Pristine Beat」", 4);

		System.out.println(scb.getSpell());
	}
}
