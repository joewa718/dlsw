package com.dlsw.cn.enumerate;

public enum RebateStatus {
    未确认("未确认", 1), 已确认("已确认", 2), 已返还("已返还", 3);

    RebateStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

    public static RebateStatus fromCode(int code) {
        if (code == 1) {
            return 未确认;
        }
        if (code == 2) {
            return 已确认;
        }
        if (code == 3) {
            return 已返还;
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