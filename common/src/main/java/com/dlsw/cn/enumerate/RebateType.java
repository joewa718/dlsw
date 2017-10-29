package com.dlsw.cn.enumerate;

public enum RebateType {
    平级信返利("平级信返利", 1), 平级高级返利("平级高级返利", 2), 团队返利("团队返利", 3);

    RebateType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

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

    public static RebateType fromCode(int code) {
        if (code == 1) {
            return 平级信返利;
        }
        if (code == 2) {
            return 平级高级返利;
        }
        if (code == 3) {
            return 团队返利;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}