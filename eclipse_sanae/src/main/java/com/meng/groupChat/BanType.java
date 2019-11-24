package com.meng.groupChat;

public enum BanType {
    ByMaster(4),
    ByGroupMaster(3),
    ByAdmin(2),
    ByGroupAdmin(1),
    ByUser(0);

    private int permission;

    BanType(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }
}
