package com.dlsw.cn.enumerate;

public enum RoleType {
    普通("普通", 0), VIP("VIP", 1), 合伙人("合伙人", 2), 高级合伙人("高级合伙人", 3);

    RoleType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

    public static RoleType fromCode(int code) {
        if (code == 0) {
            return 普通;
        }
        if (code == 1) {
            return VIP;
        }
        if (code == 2) {
            return 合伙人;
        }
        if (code == 3) {
            return 高级合伙人;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}