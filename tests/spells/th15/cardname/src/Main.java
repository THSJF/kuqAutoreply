import m.*;

public class Main {
	public static void main(String[] args) {
		SpellCardBuilder scb=new SpellCardBuilder()

			.setMaster("清兰")
			.addSpell("凶弹「高速撞击」", 2, 3)
			.addSpell("弹符「射鹰」", 0, 1)
			.addSpell("弹符「鹰已击中」", 2, 3)
			.addSpell("铳符「月狂之枪」", 0, 1, 2, 3)

			.setMaster("铃瑚")
			.addSpell("兔符「草莓团子」", 0, 1)
			.addSpell("兔符「浆果浆果团子」", 2)
			.addSpell("兔符「团子影响力」", 3)
			.addSpell("月见「九月的满月」", 0, 1)
			.addSpell("月见酒「月狂的九月」", 2, 3)

			.setMaster("哆来咪·苏伊特")
			.addSpell("梦符「绯红色的噩梦」", 0, 1)
			.addSpell("梦符「绯红色的压迫噩梦」", 2, 3)
			.addSpell("梦符「蔚蓝色的愁梦」", 0, 1)
			.addSpell("梦符「蔚蓝色的愁三重梦」", 2)
			.addSpell("梦符「愁永远之梦」", 3)
			.addSpell("梦符「刈安色的迷梦」", 0, 1)
			.addSpell("梦符「刈安色的错综迷梦」", 2, 3)
			.addSpell("梦符「捕梦网」", 0, 1)
			.addSpell("梦符「苍蓝色的捕梦网」", 2)
			.addSpell("梦符「梦我梦中」", 3)
			.addSpell("月符「绀色的狂梦」", 0, 1, 2, 3)

			.setMaster("稀神探女")
			.addSpell("玉符「乌合之咒」", 0, 1)
			.addSpell("玉符「乌合的逆咒」", 2)
			.addSpell("玉符「乌合的二重咒」", 3)
			.addSpell("玉符「秽身探知型水雷」", 0, 1)
			.addSpell("玉符「秽身探知型水雷 改」", 2, 3)
			.addSpell("玉符「众神的弹冠」", 0, 1)
			.addSpell("玉符「众神的光辉弹冠」", 2, 3)
			.addSpell("「孤翼的白鹭」", 0, 1, 2, 3)

			.setMaster("克劳恩皮丝")
			.addSpell("狱符「地狱日食」", 0, 1)
			.addSpell("狱符「地狱之蚀」", 2, 3)
			.addSpell("狱符「闪光与条纹」", 0, 1)
			.addSpell("狱符「星与条纹」", 2, 3)
			.addSpell("狱炎「擦弹地狱火」", 0, 1)
			.addSpell("狱炎「擦弹的狱意」", 2, 3)
			.addSpell("地狱「条纹状的深渊」", 0, 1, 2, 3)
			.addSpell("「伪阿波罗」", 0, 1)
			.addSpell("「阿波罗捏造说」", 2, 3)

			.setMaster("纯狐")
			.addSpell("「掌上的纯光」", 0, 1, 2, 3)
			.addSpell("「杀意的百合」", 0, 1, 2, 3)
			.addSpell("「原始的神灵界」", 0, 1)
			.addSpell("「现代的神灵界」", 2, 3)
			.addSpell("「战栗的寒冷之星」", 0, 1, 2, 3)
			.addSpell("「纯粹的疯狂」", 0, 1, 2, 3)
			.addSpell("「溢出的暇秽」", 0, 1 , 2)
			.addSpell("「地上秽的纯化」", 3)
			.addSpell("纯符「单纯的子弹地狱」", 0, 1)
			.addSpell("纯符「纯粹的弹幕地狱」", 2, 3)

			.setMaster("哆来咪·苏伊特")
			.addSpell("蝴蝶「取而代之的蝴蝶」", 4)
			.addSpell("超特急「梦幻快车」", 4)
			.addSpell("爬梦「爬行的子弹」", 4)

			.setMaster("赫卡提亚·拉碧斯拉祖利")
			.addSpell("异界「逢魔之刻」", 4)
			.addSpell("地球「邪秽在身」", 4)
			.addSpell("月「阿波罗反射镜」",  4)

			.setMaster("纯狐")
			.addSpell("「用于杀人的纯粹弹幕」", 4)

			.setMaster("赫卡提亚·拉碧斯拉祖利")
			.addSpell("异界「地狱的非理想弹幕」", 4)
			.addSpell("地球「落向地狱的雨」", 4)

			.setMaster("纯狐")
			.addSpell("「用于逼死瓮中鼠的单纯弹幕」", 4)

			.setMaster("赫卡提亚·拉碧丝拉祖利")
			.addSpell("月「月狂冲击」", 4)
			.addSpell("「三位一体论狂想曲」", 4)

			.setMaster("纯狐&赫卡提亚·拉碧斯拉祖利")
			.addSpell("「最初与最后的无名弹幕」", 4);

		System.out.println(scb.getSpell());
	}
}
