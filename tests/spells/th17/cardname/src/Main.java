import m.*;

public class Main {
	public static void main(String[] args) {
		SpellCardBuilder scb=new SpellCardBuilder()
			.setMaster("戎璎花")
			.addSpell("石符「石林」", 0, 1)
			.addSpell("石符「石头针叶林」", 2, 3)
			.addSpell("石符「儿童们的灵薄狱」", 0, 1)
			.addSpell("石符「成年儿童们的灵薄狱」", 2, 3)

			.setMaster("牛崎润美")
			.addSpell("石符「石头婴儿」", 0, 1)
			.addSpell("石符「沉重的石之头婴儿」", 2, 3)
			.addSpell("溺符「三途的沦溺」", 0, 1, 2, 3)
			.addSpell("鬼符「魔鬼围城」", 0, 1)
			.addSpell("鬼符「饿鬼围城」", 2, 3)

			.setMaster("庭渡久侘歌")
			.addSpell("水符「分水的试练」", 0, 1)
			.addSpell("水符「分水的上级试炼」", 2)
			.addSpell("水符「分水的顶级试炼」", 3)
			.addSpell("光符「瞭望的试练」", 0, 1)
			.addSpell("光符「瞭望的上级试炼」", 2)
			.addSpell("光符「瞭望的顶级试炼」", 3)
			.addSpell("鬼符「鬼渡的试练」", 0, 1)
			.addSpell("鬼符「鬼渡的上级试炼」", 2)
			.addSpell("鬼符「鬼渡的狱级试炼」", 3)

			.setMaster("吉吊八千慧")
			.addSpell("龟符「龟甲地狱」", 0, 1, 2, 3)
			.addSpell("鬼符「搦手之畜生」", 0, 1)
			.addSpell("鬼符「搦手之犬畜生」", 2)
			.addSpell("鬼符「搦手之鬼畜生」", 3)
			.addSpell("龙符「龙纹弹」", 0, 1, 2, 3)

			.setMaster("杖刀偶磨弓")
			.addSpell("埴轮「弓兵埴轮」", 0, 1)
			.addSpell("埴轮「熟练弓兵埴轮」", 2, 3)
			.addSpell("埴轮「剑士埴轮」", 0, 1)
			.addSpell("埴轮「熟练剑士埴轮」", 2, 3)
			.addSpell("埴轮「骑马兵埴轮」", 0, 1)
			.addSpell("埴轮「熟练骑马兵埴轮」", 2, 3)
			.addSpell("埴轮「空洞的无尽兵团」", 0, 1)
			.addSpell("埴轮「不败的无尽兵团」", 2, 3)

			.setMaster("埴安神袿姬")
			.addSpell("方形「方形造形术」", 0, 1)
			.addSpell("方形「方形造物」", 2, 3)
			.addSpell("圆形「正圆造形术」", 0, 1)
			.addSpell("圆形「圆形造物」", 2, 3)
			.addSpell("线形「线形造形术」", 0, 1)
			.addSpell("线形「线形造物」", 2, 3)
			.addSpell("埴轮「偶像人马造形术」", 0, 1)
			.addSpell("埴轮「偶像造物」", 2, 3)
			.addSpell("「鬼形造形术」", 0, 1, 2, 3)
			.addSpell("「几何造物」", 0, 1, 2, 3)
			.addSpell("「造型恶魔」", 0, 1, 2, 3)

			.setMaster("庭渡久侘歌")
			.addSpell("血战「血之分水岭」", 4)
			.addSpell("血战「狱界视线」", 4)
			.addSpell("血战「全灵鬼渡」", 4)

			.setMaster("骊驹早鬼")
			.addSpell("劲疾技「惊险射击」", 4)
			.addSpell("劲疾技「闪电嘶鸣」", 4)
			.addSpell("劲疾技「浓云」", 4)
			.addSpell("劲疾技「兽性感染」", 4)
			.addSpell("劲疾技「三角追击」", 4)
			.addSpell("劲疾技「黑色天马流星弹」", 4)
			.addSpell("劲疾技「肌肉爆破」", 4)
			.addSpell("「跟我来，不要怕」", 4)
			.addSpell("「鬼形的乌合之众」", 4)
			.addSpell("「鬼畜生的所业」", 4);


		System.out.println(scb.getSpell());
	}
}
