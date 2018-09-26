package com.meng;

public class RollPlane {
	private String[] pl01=new String[]{"打砖块野蛮，打飞机文明"};
	private String[] pl02=new String[]{"范围重视型","高灵击伤害 平衡型","威力重视型"};
	private String[] pl03=new String[]{"博丽灵梦","魅魔","雾雨魔理沙","爱莲","小兔姬","卡娜·安娜贝拉尔","朝仓理香子","北白河千百合","冈崎梦美"};
	private String[] pl04=new String[]{"博丽灵梦 诱导","博丽灵梦 大范围","雾雨魔理沙 激光","雾雨魔理沙 高速射击"};
	private String[] pl05=new String[]{"博丽灵梦","雾雨魔理沙","魅魔","幽香"};
	private String[] pl06=new String[]{"博丽灵梦 灵符","博丽灵梦 梦符","雾雨魔理沙 魔符","雾雨魔理沙 恋符"};
	private String[] pl07=new String[]{"博丽灵梦 灵符","博丽灵梦 梦符","雾雨魔理沙 魔符","雾雨魔理沙 恋符","十六夜咲夜 幻符","十六夜咲夜 时符"};
	private String[] pl08=new String[]{"幻想的结界组","咏唱禁咒组","梦幻的红魔组","幽冥之住人组","博丽灵梦","八云紫","雾雨魔理沙","爱丽丝·玛格特罗依德","蕾米莉亚·斯卡蕾特","十六夜咲夜","西行寺幽幽子","魂魄妖梦"};
	private String[] pl09=new String[]{"博丽灵梦","雾雨魔理沙","十六夜咲夜","魂魄妖梦","铃仙·优昙华院·因幡","琪露诺","莉莉卡·普莉兹姆利巴","梅露兰·普莉兹姆利巴","露娜萨·普莉兹姆利巴","米斯蒂娅·萝蕾拉","因幡帝","射命丸文","梅蒂欣·梅兰可莉","风见幽香","小野冢小町","四季映姬·亚玛萨那度"};
	private String[] pl10=new String[]{"博丽灵梦 诱导装备","博丽灵梦 前方集中装备","博丽灵梦 封印装备","雾雨魔理沙 高威力装备","雾雨魔理沙 贯通装备","雾雨魔理沙 魔法使装备"};
	private String[] pl11=new String[]{"博丽灵梦 八云紫","博丽灵梦 伊吹萃香","博丽灵梦 射命丸文","雾雨魔理沙 爱丽丝·玛格特罗依德","雾雨魔理沙 帕秋莉","雾雨魔理沙 河城荷取"};
	private String[] pl12=new String[]{"博丽灵梦 诱导型","博丽灵梦 威力重视型","雾雨魔理沙 贯通型","雾雨魔理沙 范围重视型","东风谷早苗 诱导型","东风谷早苗 广范围炸裂型"};
	private String[] pl13=new String[]{"博丽灵梦","雾雨魔理沙","东风谷早苗","魂魄妖梦"};
	private String[] pl14=new String[]{"博丽灵梦","雾雨魔理沙","十六夜咲夜"};
	private String[] pl14s=new String[]{"使用妖器","不使用妖器"};
	private String[] pl15=new String[]{"博丽灵梦","雾雨魔理沙","东风谷早苗","铃仙·优昙华院·因幡"};
	private String[] pl16=new String[]{"博丽灵梦","琪露诺","射命丸文","雾雨魔理沙"};
	private String[] pl16s=new String[]{"春","夏","秋","冬"};
	private String[] plDiff=new String[]{"easy","normal","hard","lunatic"};

	public RollPlane() {

	}

	public boolean check(long fromGroup, String msg) {
		String[] ss = msg.split("\\.");
		if (ss[0].equals("roll")) {
			switch (ss[1]) {
			case "东方灵异传":
			case "th1":
			case "th01":
				send(fromGroup, MainSwitch.rfa(pl01));	
				break;
			case "东方封魔录":
			case "th2":
			case "th02":
				send(fromGroup, MainSwitch.rfa(pl02));	
				break;
			case "东方梦时空":
			case "th3":
			case "th03":
				send(fromGroup, MainSwitch.rfa(pl03));	
				break;
			case "东方幻想乡":
			case "th4":
			case "th04":
				send(fromGroup, MainSwitch.rfa(pl04));	
				break;
			case "东方怪绮谈":
			case "th5":
			case "th05":
				send(fromGroup, MainSwitch.rfa(pl05));	
				break;
			case "东方红魔乡":
			case "th6":
			case "th06":
				send(fromGroup, MainSwitch.rfa(pl06));	
				break;
			case "东方妖妖梦":
			case "th7":
			case "th07":
				send(fromGroup, MainSwitch.rfa(pl07));	
				break;
			case "东方永夜抄":
			case "th8":
			case "th08":
				send(fromGroup, MainSwitch.rfa(pl08));	
				break;
			case "东方花映冢":
			case "th9":
			case "th09":
				send(fromGroup, MainSwitch.rfa(pl09));	
				break;
			case "东方风神录":
			case "th10":
				send(fromGroup, MainSwitch.rfa(pl10));	
				break;
			case "东方地灵殿":
			case "th11":
				send(fromGroup, MainSwitch.rfa(pl11));	
				break;
			case "东方星莲船":
			case "th12":
				send(fromGroup, MainSwitch.rfa(pl12));	
				break;
			case "东方神灵庙":
			case "th13":
				send(fromGroup, MainSwitch.rfa(pl13));	
				break;
			case "东方辉针城":
			case "th14":
				send(fromGroup, MainSwitch.rfa(pl14)+" "+MainSwitch.rfa(pl14s));	
				break;
			case "东方绀珠传":
			case "th15":
				send(fromGroup, MainSwitch.rfa(pl15));	
				break;
			case "东方天空璋":
			case "th16":
				send(fromGroup, MainSwitch.rfa(pl16)+" "+MainSwitch.rfa(pl16s));	
				break;
			case "游戏":
			case "game":
				send(fromGroup, "th"+(Autoreply.random.nextInt(16))+1);	
				break;
			case "diff":
			case "难度":
				send(fromGroup, MainSwitch.rfa(plDiff));	
				break;
			}
			return true;
		}
		return false;
	}
	private void send(long fromGroup,String s) {
		Autoreply.sendGroupMessage(fromGroup,s);	
	}

}
