package com.dlsw.cn.web.enumerate;

public enum RebateStatus {
    未返利("未返利", 1), 已返利("已返利", 2);

    RebateStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

    public static RebateStatus fromCode(int code) {
        if (code == 1) {
            return 未返利;
        }
        if (code == 2) {
            return 已返利;
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