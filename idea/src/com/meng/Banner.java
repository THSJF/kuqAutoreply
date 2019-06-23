package com.meng;

public class Banner {

    public Banner() {
    }

    public boolean checkBan(long fromQQ, long fromGroup, String msg) {
        if (fromQQ == 2856986197L || fromQQ == 1592608126L || fromQQ == 183889179L || fromQQ == 3291680841L
                || fromQQ == 983689136L || fromQQ == 1012539034L || fromQQ == 2956832566L || fromQQ == 1355225380L
                || fromQQ == 2331232772L || fromQQ == 1594703250L) {
            // 字符串无法转换为数字会NumberFormatException
            try {
                if (msg.equalsIgnoreCase("wholeban")) {
                    Autoreply.CQ.setGroupWholeBan(fromGroup, true);
                    return true;
                } else if (msg.equalsIgnoreCase("wholerelease")) {
                    Autoreply.CQ.setGroupWholeBan(fromGroup, false);
                    return true;
                }
                String[] strs = msg.split("\\.");
                int sleepTime = 0;
                switch (strs.length) {
                    case 2:
                        if (strs[0].equalsIgnoreCase("wholeban")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), true);
                            return true;
                        } else if (strs[0].equalsIgnoreCase("wholerelease")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), false);
                            return true;
                        }
                    case 3:
                        if (strs[0].equalsIgnoreCase("ban")) {
                            sleepTime = Integer.parseInt(strs[2]);
                            sleepTime = sleepTime > 1800 ? 1800 : sleepTime;
                            sleepTime = sleepTime < 1 ? 1 : sleepTime;
                            sleepTime = Long.parseLong(strs[1]) == 2856986197L ? 1 : sleepTime;
                            sleepTime = Long.parseLong(strs[1]) == 2856986197L ? 1 : Integer.parseInt(strs[2]);
                            Autoreply.CQ.setGroupBan(fromGroup, Long.parseLong(strs[1]), sleepTime);
                            return true;
                        }
                        break;
                    case 4:
                        if (strs[0].equalsIgnoreCase("ban")) {
                            sleepTime = Integer.parseInt(strs[3]);
                            sleepTime = sleepTime > 1800 ? 1800 : sleepTime;
                            sleepTime = sleepTime < 1 ? 1 : sleepTime;
                            sleepTime = Long.parseLong(strs[1]) == 2856986197L ? 1 : sleepTime;
                            Autoreply.CQ.setGroupBan(Long.parseLong(strs[1]), Long.parseLong(strs[2]), sleepTime);
                            return true;
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        } else {
            try {
                String[] strings = msg.split("\\.");
                if (strings.length == 3 && strings[0].equalsIgnoreCase("ban")) {
                    int sleepTime = Integer.parseInt(strings[2]);
                    sleepTime = sleepTime > 120 ? 120 : sleepTime;
                    sleepTime = sleepTime < 1 ? 1 : sleepTime;
                    if (Long.parseLong(strings[1]) == 2856986197L) {
                        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                        return true;
                    }
                    if (Autoreply.instence.random.nextInt() % 2 == 0) {
                        Autoreply.CQ.setGroupBan(fromGroup, Long.parseLong(strings[1]), sleepTime);
                    } else {
                        if (fromQQ == 1592608126L) {
                            return true;
                        }
                        Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
                    }
                    return true;
                }
            } catch (NumberFormatException e) {
                return true;
            }
        }
        try {
            String[] st = msg.split("\\.");
            if (st.length == 3 && st[0].equalsIgnoreCase("sleep")) {
                if (fromQQ == 2856986197L || fromQQ == 1592608126L || fromQQ == 943486447L || fromQQ == 183889179L
                        || fromQQ == 350795616L) {
                    return true;
                }
                int time = 1;
                switch (st[1]) {
                    case "s":
                    case "second":
                    case "sec":
                        time = 1;
                        break;
                    case "min":
                    case "minute":
                        time = 60;
                        break;
                    case "h":
                    case "hour":
                        time = 3600;
                        break;
                    case "d":
                    case "day":
                        time = 86400;
                        break;
                    case "w":
                    case "week":
                        time = 604800;
                        break;
                    case "m":
                    case "month":
                        time = 2592000;
                        break;
                }
                int sleepTime = Integer.parseInt(st[2]) * time;
                sleepTime = sleepTime > 2592000 ? 2592000 : sleepTime;
                sleepTime = sleepTime < 0 ? 0 : sleepTime;
                Autoreply.CQ.setGroupBan(fromGroup, fromQQ, sleepTime);
            }
        } catch (NumberFormatException e) {
            Autoreply.CQ.setGroupBan(fromGroup, fromQQ, 9961);
            return true;
        }
        return false;
    }
}
