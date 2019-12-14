import m.*;

public class Main {
	public static void main(String[] args) {
		SpellCardBuilder scb=new SpellCardBuilder()

			.setMaster("露米娅")
			.addSpell("月符「月光」", 2, 3)
			.addSpell("夜符「夜雀」", 0, 1, 2, 3)
			.addSpell("暗符「境界线」", 0, 1, 2, 3)

			.setMaster("琪露诺")
			.addSpell("冰符「冰瀑」", 0, 1)
			.addSpell("雹符「冰雹暴风」", 2, 3)
			.addSpell("冻符「完美冻结」", 0, 1, 2, 3)
			.addSpell("雪符「钻石风暴」", 1, 2, 3)

			.setMaster("红美铃")
			.addSpell("华符「芳华绚烂」", 0, 1)
			.addSpell("华符「Selaginella 9」", 2, 3)
			.addSpell("虹符「彩虹风铃」", 0, 1, 2, 3)
			.addSpell("幻符「华想梦葛」", 2, 3)
			.addSpell("彩符「彩雨」", 0, 1)
			.addSpell("彩符「彩光乱舞」", 2, 3)
			.addSpell("彩符「极彩台风」", 1, 2, 3)

			.setMaster("帕秋莉·诺蕾姬")
			.addSpell("火符「火神之光」",  0, 1)
			.addSpell("水符「水精公主」", 0, 1)
			.addSpell("木符「风灵的角笛」", 0, 1)
			.addSpell("土符「慵懒三石塔」", 0, 1)
			.addSpell("金符「金属疲劳」", 1)
			.addSpell("火符「火神之光 上级」", 1, 2, 3)
			.addSpell("木符「风灵的角笛 上级」", 1, 2, 3)
			.addSpell("土符「慵懒三石塔 上级」", 1, 2, 3)
			.addSpell("水符「湖葬」", 2, 3)
			.addSpell("木符「翠绿风暴」", 2, 3)
			.addSpell("土符「三石塔的震动」", 2, 3)
			.addSpell("金符「银龙」", 2, 3)
			.addSpell("火&土符「环状熔岩带」", 0, 1, 2, 3)
			.addSpell("木&火符「森林大火」", 0, 1, 2, 3)
			.addSpell("水&木符「水精灵」", 0, 1, 2, 3)
			.addSpell("金&水符「水银之毒」", 1, 2, 3)
			.addSpell("土&金符「翡翠巨石」", 0, 1, 2, 3)

			.setMaster("十六夜咲夜")
			.addSpell("奇术「误导」", 0, 1)
			.addSpell("奇术「幻惑误导」", 2, 3)
			.addSpell("幻在「钟表的残骸」", 0, 1)
			.addSpell("幻幽「迷幻杰克」", 2, 3)
			.addSpell("幻象「月神之钟」", 0, 1)
			.addSpell("幻世「世界」", 2, 3)
			.addSpell("女仆秘技「操弄玩偶」", 0, 1)
			.addSpell("女仆秘技「杀人玩偶」", 2, 3)
			.addSpell("奇术「永恒的温柔」", 0, 1, 2, 3)

			.setMaster("蕾米莉亚·斯卡雷特")
			.addSpell("天罚「大卫之星」", 0, 1)
			.addSpell("神罚「年幼的恶魔之王」", 2, 3)
			.addSpell("冥符「红色的冥界」", 0, 1)
			.addSpell("狱符「千根针的针山」", 2, 3)
			.addSpell("诅咒「弗拉德·特佩斯的诅咒」", 0, 1)
			.addSpell("神术「吸血鬼幻想」", 2, 3)
			.addSpell("红符「绯红射击」", 0, 1)
			.addSpell("红符「绯红之主」", 2, 3)
			.addSpell("「Red Magic」", 1)
			.addSpell("「红色的幻想乡」", 2, 3)

			.setMaster("帕秋莉·诺蕾姬")
			.addSpell("月符「静息的月神」", 4)
			.addSpell("日符「皇家烈焰」", 4)
			.addSpell("火水木金土符「贤者之石」", 4)

			.setMaster("芙兰朵露·斯卡雷特")
			.addSpell("禁忌「红莓陷阱」", 4)
			.addSpell("禁忌「莱瓦汀」", 4)
			.addSpell("禁忌「四重存在」", 4)
			.addSpell("禁忌「笼中鸟」", 4)
			.addSpell("禁忌「恋之迷宫」", 4)
			.addSpell("禁弹「星弧破碎」", 4)
			.addSpell("禁弹「折反射」", 4)
			.addSpell("禁弹「刻着过去的钟表」", 4)
			.addSpell("秘弹「之后就一个人都没有了吗？」", 4)
			.addSpell("QED「495年的波纹」", 4);

		System.out.println(scb.getSpell());
	}
}
