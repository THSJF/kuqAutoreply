import m.*;

public class Main {
	public static void main(String[] args) {
		SpellCardBuilder scb=new SpellCardBuilder()
			.setMaster("爱塔妮缇拉尔瓦")
			.addSpell("蝶符「细碎鳞粉」", 0, 1)
			.addSpell("蝶符「凤蝶的鳞粉」", 2, 3)
			.addSpell("蝶符「扑翅之夏」", 0, 1)
			.addSpell("蝶符「盛夏振翅」", 2, 3)

			.setMaster("坂田合欢")
			.addSpell("雨符「被囚禁的秋雨」", 0, 1)
			.addSpell("雨符「被诅咒的柴榑雨」", 2, 3)
			.addSpell("刃符「山姥的菜刀研磨」", 0, 1)
			.addSpell("刃符「山姥的鬼菜刀研磨」", 2, 3)
			.addSpell("尽符「深山谋杀」", 0, 1)
			.addSpell("尽符「血腥的深山谋杀」", 2, 3)

			.setMaster("莉莉白")
			.addSpell("春符「惊喜之春」", 2, 3)

			.setMaster("高丽野阿吽")
			.addSpell("犬符「野犬的散步」", 0, 1)
			.addSpell("狗符「山狗的散步」", 2, 3)
			.addSpell("陀螺「狛犬回旋」", 0, 1, 2)
			.addSpell("陀螺「蜷缩死去」", 3)
			.addSpell("狛符「单人式阿吽的呼吸」", 0, 1, 2, 3)

			.setMaster("矢田寺成美")
			.addSpell("魔符「顷刻菩提」", 0, 1)
			.addSpell("魔符「即席菩提」", 2, 3)
			.addSpell("魔符「弹丸魔像」", 0, 1)
			.addSpell("魔符「作宠物的巨大弹生命体」", 2, 3)
			.addSpell("地藏「罪业救赎」", 0, 1)
			.addSpell("地藏「业火救济」", 2, 3)

			.setMaster("丁礼田舞")
			.addSpell("竹符「竹矛之舞」", 0, 1)
			.addSpell("竹符「竹之狂舞」", 2, 3)
			.addSpell("笹符「七夕星祭」", 0, 1, 2, 3)

			.setMaster("尔子田里乃")
			.addSpell("茗荷「忘却你的名字」", 0, 1, 2, 3)
			.addSpell("冥加「在你背后」", 0, 1, 2, 3)

			.setMaster("丁礼田舞&尔子田里乃")
			.addSpell("舞符「背后之祭」", 0, 1, 2, 3)
			.addSpell("狂舞「天狗怖吓」", 0, 1)
			.addSpell("狂舞「狂乱天狗怖吓」", 2, 3)

			.setMaster("摩多罗隐岐奈")
			.addSpell("后符「秘神的后光」", 0, 1)
			.addSpell("后符「绝对秘神的后光」", 2, 3)
			.addSpell("里夏「暑夏炙烤」", 0, 1)
			.addSpell("里夏「异常酷暑之焦土」", 2, 3)
			.addSpell("里秋「死于饥荒」", 0, 1)
			.addSpell("里秋「异常枯死之饿鬼」", 2, 3)
			.addSpell("里冬「黑色雪人」", 0, 1)
			.addSpell("里冬「异常降雪之雪人」", 2, 3)
			.addSpell("里春「四月巫师」", 0, 1)
			.addSpell("里春「异常落花之魔术使」", 2, 3)
			.addSpell("「里·Breeze Cherry Blossom」", 0, 1, 2, 3)
			.addSpell("「里·Perfect Summer Ice」", 0, 1, 2, 3)
			.addSpell("「里·Crazy Fall Wind」", 0, 1, 2, 3)
			.addSpell("「里·Extreme Winter」", 0, 1, 2, 3)

			.setMaster("丁礼田舞&尔子田里乃")
			.addSpell("鼓舞「强力助威」", 4)
			.addSpell("狂舞「疯狂的背景舞」", 4)
			.addSpell("弹舞「双目台风」", 4)

			.setMaster("摩多罗隐岐奈")
			.addSpell("秘仪「逆向呼神者」", 4)
			.addSpell("秘仪「背叛的后方射击」", 4)
			.addSpell("秘仪「弹幕的玉茧」", 4)
			.addSpell("秘仪「秽那之火」", 4)
			.addSpell("秘仪「后户的狂言」", 4)
			.addSpell("秘仪「摩多罗苦谛」", 4)
			.addSpell("秘仪「七星之剑」", 4)
			.addSpell("秘仪「无纽带的艺人」", 4)
			.addSpell("「背面的暗黑猿乐」", 4)
			.addSpell("「无秩序弹幕地狱」", 4);

		System.out.println(scb.getSpell());
	}
}
