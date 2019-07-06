package com.meng;

import com.meng.config.ConfigManager;

public class Banner {
    private ConfigManager configManager;

    public Banner(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean checkBan(long fromQQ, long fromGroup, String msg) {
        String[] strs = msg.split("\\.");
        switch (strs.length) {
            case 1:
                if (configManager.isAdmin(fromQQ)) {
                    if (msg.equalsIgnoreCase("allban")) {
                        Autoreply.CQ.setGroupWholeBan(fromGroup, true);
                        return true;
                    }
                    if (msg.equalsIgnoreCase("allrelease")) {
                        Autoreply.CQ.setGroupWholeBan(fromGroup, false);
                        return true;
                    }
                }
                break;
            case 2:
                if (configManager.isAdmin(fromQQ)) {
                    try {
                        if (strs[0].equalsIgnoreCase("allban")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), true);
                            return true;
                        }
                        if (strs[0].equalsIgnoreCase("allrelease")) {
                            Autoreply.CQ.setGroupWholeBan(Long.parseLong(strs[1]), false);
                            return true;
                        }
                    } catch (Exception e) {
                        Autoreply.sendMessage(fromGroup, fromQQ, e.toString());
                    }
                }
                break;
            case 3:
                if (strs[0].equals("ban")) {
                    long targetQQ;
                    int time;
                    try {
                        targetQQ = Long.parseLong(strs[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        targetQQ = Autoreply.instence.CC.getAt(strs[1]);
                        if (targetQQ == -1) {
                            return false;
                        }
                    }
                    try {
                        time = Integer.parseInt(strs[2]);
                    } catch (Exception e) {
                        targetQQ = fromQQ;
                        time = 2592000;
                        e.printStackTrace();
                    }
                    if (checkBan(fromGroup, targetQQ, fromQQ, time)) {
                        return true;
                    }
                }
                break;
            case 4:
                if (strs[0].equals("ban")) {
                    long targetGroup;
                    long targetQQ;
                    int time;
                    try {
                        targetGroup = Long.parseLong(strs[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Autoreply.sendMessage(fromGroup, fromQQ, "群号错误");
                        return false;
                    }
                    try {
                        targetQQ = Long.parseLong(strs[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        targetQQ = Autoreply.instence.CC.getAt(strs[2]);
                        if (targetQQ == -1) {
                            return false;
                        }
                    }
                    try {
                        time = Integer.parseInt(strs[3]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        targetQQ = fromQQ;
                        time = 2592000;
                    }
                    if (checkBan(targetGroup, targetQQ, fromQQ, time)) {
                        return true;
                    }
                }
        }
        return checkSleep(fromGroup, fromQQ, strs);
    }

    private boolean checkSleep(long fromGroup, long fromQQ, String[] str) {
        if (str.length == 3 && str[0].equals("sleep")) {
            if (configManager.isAdmin(fromQQ)) {
                return true;
            }
            int time;
            switch (str[1]) {
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
                default:
                    time = 5400;
                    break;
            }
            int sleepTime;
            try {
                sleepTime = Integer.parseInt(str[2]) * time;
                if (sleepTime > 2592000) {
                    sleepTime = 2592000;
                }
            } catch (Exception e) {
                sleepTime = 2592000;
            }
            if (sleepTime <= 0) {
                Methods.ban(fromGroup, fromQQ, 5400);
                return true;
            } else {
                Methods.ban(fromGroup, fromQQ, sleepTime);
                return true;
            }
        }
        return false;
    }

    private boolean checkBan(long targetGroup, long targetQQ, long fromQQ, int time) {
        try {
            if (configManager.isAdmin(fromQQ)) {
                if (time > 2592000) {
                    time = 2592000;
                }
            } else {
                if (time > 120) {
                    time = 120;
                }
            }
            if (time < 0) {
                time = 0;
            }
        } catch (Exception e) {
            time = 2592000;
            targetQQ = fromQQ;
        }
        if (configManager.isMaster(targetQQ)) {
            if (!configManager.isMaster(fromQQ)) {
                Methods.ban(targetGroup, fromQQ, time);
                return true;
            }
        } else {
            Methods.ban(targetGroup, targetQQ, time);
            return true;
        }
        return false;
    }
}
