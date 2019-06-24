package com.meng;

import java.util.HashMap;

public class RollPlane {
    private String[] pl01 = new String[]{"打砖块野蛮，打飞机文明"};
    private String[] pl02 = new String[]{"范围重视型", "高灵击伤害 平衡型", "威力重视型"};
    private String[] pl03 = new String[]{"博丽灵梦", "魅魔", "雾雨魔理沙", "爱莲", "小兔姬", "卡娜·安娜贝拉尔", "朝仓理香子", "北白河千百合", "冈崎梦美"};
    private String[] pl04 = new String[]{"博丽灵梦 诱导", "博丽灵梦 大范围", "雾雨魔理沙 激光", "雾雨魔理沙 高速射击"};
    private String[] pl05 = new String[]{"博丽灵梦", "雾雨魔理沙", "魅魔", "幽香"};
    private String[] pl06 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符"};
    private String[] pl07 = new String[]{"博丽灵梦 灵符", "博丽灵梦 梦符", "雾雨魔理沙 魔符", "雾雨魔理沙 恋符", "十六夜咲夜 幻符", "十六夜咲夜 时符"};
    private String[] pl08 = new String[]{"幻想的结界组", "咏唱禁咒组", "梦幻的红魔组", "幽冥之住人组", "博丽灵梦", "八云紫", "雾雨魔理沙", "爱丽丝·玛格特罗依德", "蕾米莉亚·斯卡蕾特", "十六夜咲夜", "西行寺幽幽子", "魂魄妖梦"};
    private String[] pl09 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜", "魂魄妖梦", "铃仙·优昙华院·因幡", "琪露诺", "莉莉卡·普莉兹姆利巴", "梅露兰·普莉兹姆利巴", "露娜萨·普莉兹姆利巴", "米斯蒂娅·萝蕾拉", "因幡帝", "射命丸文", "梅蒂欣·梅兰可莉", "风见幽香", "小野冢小町", "四季映姬·亚玛萨那度"};
    private String[] pl10 = new String[]{"博丽灵梦 诱导装备", "博丽灵梦 前方集中装备", "博丽灵梦 封印装备", "雾雨魔理沙 高威力装备", "雾雨魔理沙 贯通装备", "雾雨魔理沙 魔法使装备"};
    private String[] pl11 = new String[]{"博丽灵梦 八云紫", "博丽灵梦 伊吹萃香", "博丽灵梦 射命丸文", "雾雨魔理沙 爱丽丝·玛格特罗依德", "雾雨魔理沙 帕秋莉", "雾雨魔理沙 河城荷取"};
    private String[] pl12 = new String[]{"博丽灵梦 诱导型", "博丽灵梦 威力重视型", "雾雨魔理沙 贯通型", "雾雨魔理沙 范围重视型", "东风谷早苗 诱导型", "东风谷早苗 广范围炸裂型"};
    private String[] pl13 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "魂魄妖梦"};
    private String[] pl14 = new String[]{"博丽灵梦", "雾雨魔理沙", "十六夜咲夜"};
    private String[] pl14s = new String[]{"使用妖器", "不使用妖器"};
    private String[] pl15 = new String[]{"博丽灵梦", "雾雨魔理沙", "东风谷早苗", "铃仙·优昙华院·因幡"};
    private String[] pl16 = new String[]{"博丽灵梦", "琪露诺", "射命丸文", "雾雨魔理沙"};
    private String[] pl16s = new String[]{"春", "夏", "秋", "冬"};
    private String[] pl17 = new String[]{"博丽灵梦", "雾雨魔理沙", "魂魄妖梦"};
    private String[] pl17s = new String[]{"熊哥", "鸟哥", "狗哥"};
    private String[] plDiff = new String[]{"easy", "normal", "hard", "lunatic"};
    private String[] cham = new String[]{"开局按↓去正下版底贴一下", "开局原地小转3圈顺时针", "开局原地小转3圈逆时针", "开局↑↓↑↓", "开局←→←→"};

    public RollPlane() {

    }

    public boolean check(long fromGroup, String msg) {
        String[] ss = msg.split("\\.");
        if (ss.length > 1) {
            if (ss[0].equals("roll")) {
                switch (ss[1]) {
                    case "pl":
                    case "plane":
                    case "player":
                        if (ss.length == 3) {
                            rollPlane(ss[2], fromGroup);
                        } else if (ss.length == 4) {
                            rollPlane(ss[2] + "." + ss[3], fromGroup);
                        }
                        break;
                    case "游戏":
                    case "game":
                        Autoreply.sendMessage(fromGroup, 0, "th" + (Autoreply.instence.random.nextInt(16) + 1));
                        break;
                    case "diff":
                    case "difficult":
                    case "难度":
                        Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(plDiff));
                        break;
                    case "act":
                    case "动作":
                        Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(cham));
                        break;
                    case "stage":
                    case "关卡":
                    case "面数":
                        rollStage(ss, fromGroup);
                        break;
                    case "help":
                    case "帮助":
                        String str = "roll.help roll.帮助 不解释（\nroll.act roll.动作 孙晋芳杯规定动作随机选择（\nroll.game roll.游戏 可以随机选择游戏\nroll.difficult roll.diff roll.难度 可以随机选择难度\nroll.player roll.pl roll.plane接作品名或编号可随机选择机体（仅官方整数作）\nroll.stage roll.关卡 roll.面数 加玩家名可用来接力时随机选择面数，多个玩家名之间用.隔开\n";
                        Autoreply.sendMessage(fromGroup, 0, str);
                        break;
                }
                return true;
            }
        }
        return false;
    }

    private void rollPlane(String ss, long fromGroup) {
        switch (ss) {
            case "东方灵异传":
            case "th1":
            case "th01":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl01));
                break;
            case "东方封魔录":
            case "th2":
            case "th02":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl02));
                break;
            case "东方梦时空":
            case "th3":
            case "th03":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl03));
                break;
            case "东方幻想乡":
            case "th4":
            case "th04":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl04));
                break;
            case "东方怪绮谈":
            case "th5":
            case "th05":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl05));
                break;
            case "东方红魔乡":
            case "th6":
            case "th06":
            case "tEoSD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl06));
                break;
            case "东方妖妖梦":
            case "th7":
            case "th07":
            case "PCB":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl07));
                break;
            case "东方永夜抄":
            case "th8":
            case "th08":
            case "IN":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl08));
                break;
            case "东方花映冢":
            case "th9":
            case "th09":
            case "PoFV":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl09));
                break;
            case "东方风神录":
            case "th10":
            case "MoF":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl10));
                break;
            case "东方地灵殿":
            case "th11":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl11));
                break;
            case "东方星莲船":
            case "th12":
            case "UFO":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl12));
                break;
            case "东方神灵庙":
            case "th13":
            case "TD":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl13));
                break;
            case "东方辉针城":
            case "th14":
            case "DDC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl14) + " " + Methods.rfa(pl14s));
                break;
            case "东方绀珠传":
            case "th15":
            case "LoLK":
                Autoreply.sendMessage(fromGroup, 0, (String) Methods.rfa(pl15));
                break;
            case "东方天空璋":
            case "th16":
            case "HSiFS":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl16) + " " + Methods.rfa(pl16s));
                break;
            case "东方鬼形兽":
            case "th17":
            case "WBaWC":
                Autoreply.sendMessage(fromGroup, 0, Methods.rfa(pl17) + "+" + Methods.rfa(pl17s));
                break;

            case "东方文花帖":
            case "th9.5":
            case "StB":
            case "东方文花帖DS":
            case "th12.5":
            case "DS":
            case "妖精大战争":
            case "弹幕天邪鬼":
            case "th14.3":
            case "ISC":
            case "秘封噩梦日记":
            case "th16.5":
            case "VD":
                Autoreply.sendMessage(fromGroup, 0, "就一个飞机你roll你[CQ:emoji,id=128052]呢");
                break;
        }
    }

    private void rollStage(String[] ss, long fromGroup) {
        HashMap<Integer, String> hMap = new HashMap<>();// 存放数据
        for (int i = 2; i < ss.length; i++) {
            hMap.put(Autoreply.instence.random.nextInt(), ss[i]);// 随机key
        }
        int flag = 1;
        StringBuilder sBuilder = new StringBuilder();
        for (Integer key : hMap.keySet()) {// 遍历
            sBuilder.append("stage").append(flag).append(":").append(hMap.get(key)).append("\n");
            flag++;
        }
        Autoreply.sendMessage(fromGroup, 0, sBuilder.append("完成").toString());
    }
}
