package com.meng;

public class RollPlane {
	String[] pl01=new String[]{"打砖块野蛮，打飞机文明"};
	String[] pl02=new String[]{"范围重视型","高灵击伤害 平衡型","威力重视型"};
	String[] pl03=new String[]{"博丽灵梦","魅魔","雾雨魔理沙","爱莲","小兔姬","卡娜·安娜贝拉尔","朝仓理香子","北白河千百合","冈崎梦美"};
	String[] pl04=new String[]{"博丽灵梦 诱导","博丽灵梦 大范围","雾雨魔理沙 激光","雾雨魔理沙 高速射击"};
	String[] pl05=new String[]{"博丽灵梦","雾雨魔理沙","魅魔","幽香"};
	String[] pl06=new String[]{"博丽灵梦 灵符","博丽灵梦 梦符","雾雨魔理沙 魔符","雾雨魔理沙 恋符"};
	String[] pl07=new String[]{"博丽灵梦 灵符","博丽灵梦 梦符","雾雨魔理沙 魔符","雾雨魔理沙 恋符","十六夜咲夜 幻符","十六夜咲夜 时符"};
	String[] pl08=new String[]{"幻想的结界组","咏唱禁咒组","梦幻的红魔组","幽冥之住人组","博丽灵梦","八云紫","雾雨魔理沙","爱丽丝·玛格特罗依德","蕾米莉亚·斯卡蕾特","十六夜咲夜","西行寺幽幽子","魂魄妖梦"};
	String[] pl09=new String[]{"博丽灵梦","雾雨魔理沙","十六夜咲夜","魂魄妖梦","铃仙·优昙华院·因幡","琪露诺","莉莉卡·普莉兹姆利巴","梅露兰·普莉兹姆利巴","露娜萨·普莉兹姆利巴","米斯蒂娅·萝蕾拉","因幡帝","射命丸文","梅蒂欣·梅兰可莉","风见幽香","小野冢小町","四季映姬·亚玛萨那度"};
	String[] pl10=new String[]{"博丽灵梦 诱导装备","博丽灵梦 前方集中装备","博丽灵梦 封印装备","雾雨魔理沙 高威力装备","雾雨魔理沙 贯通装备","雾雨魔理沙 魔法使装备"};
	String[] pl11=new String[]{"博丽灵梦 八云紫","博丽灵梦 伊吹萃香","博丽灵梦 射命丸文","雾雨魔理沙 爱丽丝·玛格特罗依德","雾雨魔理沙 帕秋莉","雾雨魔理沙 河城荷取"};
	String[] pl12=new String[]{"博丽灵梦 诱导型","博丽灵梦 威力重视型","雾雨魔理沙 贯通型","雾雨魔理沙 范围重视型","东风谷早苗 诱导型","东风谷早苗 广范围炸裂型"};
	String[] pl13=new String[]{"博丽灵梦","雾雨魔理沙","东风谷早苗","魂魄妖梦"};
	String[] pl14=new String[]{"博丽灵梦 使用妖器","博丽灵梦 不使用妖器","雾雨魔理沙 使用妖器","雾雨魔理沙 不使用妖器","十六夜咲夜 使用妖器","十六夜咲夜 不使用妖器"};
	String[] pl15=new String[]{"博丽灵梦","雾雨魔理沙","东风谷早苗","铃仙·优昙华院·因幡"};
	String[] pl16=new String[]{"博丽灵梦","琪露诺","射命丸文","雾雨魔理沙"};

	public RollPlane() {

	}

	public boolean check(long fromGroup, String msg) {
		String[] ss = msg.split("\\.");
		if (ss[0].equals("roll")) {
			switch (ss[1]) {
			case "东方灵异传":
			case "th1":
			case "th01":
				switchTh01(fromGroup);
				break;
			case "东方封魔录":
			case "th2":
			case "th02":
				switchTh02(fromGroup);
				break;
			case "东方梦时空":
			case "th3":
			case "th03":
				switchTh03(fromGroup);
				break;
			case "东方幻想乡":
			case "th4":
			case "th04":
				switchTh04(fromGroup);
				break;
			case "东方怪绮谈":
			case "th5":
			case "th05":
				switchTh05(fromGroup);
				break;
			case "东方红魔乡":
			case "th6":
			case "th06":
				switchTh06(fromGroup);
				break;
			case "东方妖妖梦":
			case "th7":
			case "th07":
				switchTh07(fromGroup);
				break;
			case "东方永夜抄":
			case "th8":
			case "th08":
				switchTh08(fromGroup);
				break;
			case "东方花映冢":
			case "th9":
			case "th09":
				switchTh09(fromGroup);
				break;
			case "东方风神录":
			case "th10":
				switchTh10(fromGroup);
				break;
			case "东方地灵殿":
			case "th11":
				switchTh11(fromGroup);
				break;
			case "东方星莲船":
			case "th12":
				switchTh12(fromGroup);
				break;
			case "东方神灵庙":
			case "th13":
				switchTh13(fromGroup);
				break;
			case "东方辉针城":
			case "th14":
				switchTh14(fromGroup);
				break;
			case "东方绀珠传":
			case "th15":
				switchTh15(fromGroup);
				break;
			case "东方天空璋":
			case "th16":
				switchTh16(fromGroup);
				break;
			case "游戏":
				switchThGame(fromGroup);
				break;
			}
			return true;
		}
		return false;
	}
	private void switchTh01(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl01[Autoreply.random.nextInt(pl01.length)]);	
	}
	private void switchTh02(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl02[Autoreply.random.nextInt(pl02.length)]);	
	}
	private void switchTh03(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl03[Autoreply.random.nextInt(pl03.length)]);	
	}
	private void switchTh04(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl04[Autoreply.random.nextInt(pl04.length)]);	
	}
	private void switchTh05(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl05[Autoreply.random.nextInt(pl05.length)]);	
	}
	private void switchTh06(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl06[Autoreply.random.nextInt(pl06.length)]);	
	}
	private void switchTh07(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl07[Autoreply.random.nextInt(pl07.length)]);	
	}
	private void switchTh08(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl08[Autoreply.random.nextInt(pl08.length)]);	
	}
	private void switchTh09(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl09[Autoreply.random.nextInt(pl09.length)]);	
	}
	private void switchTh10(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl10[Autoreply.random.nextInt(pl10.length)]);	
	}
	private void switchTh11(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl11[Autoreply.random.nextInt(pl11.length)]);	
	}
	private void switchTh12(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl12[Autoreply.random.nextInt(pl12.length)]);	
	}
	private void switchTh13(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl13[Autoreply.random.nextInt(pl13.length)]);	
	}
	private void switchTh14(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl14[Autoreply.random.nextInt(pl14.length)]);	
	}
	private void switchTh15(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl15[Autoreply.random.nextInt(pl15.length)]);	
	}
	private void switchTh16(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, pl16[Autoreply.random.nextInt(pl16.length)]);	
	}
	private void switchThGame(long fromGroup) {
		Autoreply.sendGroupMessage(fromGroup, "th"+(Autoreply.random.nextInt(16)+1));	
	}
}
