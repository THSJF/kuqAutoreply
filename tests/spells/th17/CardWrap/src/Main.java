import java.util.*;

public class Main {
	public static void main(String[] args) {
		for (String s:spells) {
			System.out.println(String.format("case \"%s\":\nps=\"暂无\";\nbreak;", s));
		}
	}
	public static String[] spells=new String[]{
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
}
