package com.dlsw.cn.web.enumerate;

public enum RebateType {
    信平级返利("信平级返利", 1), 级差返利("级差返利", 2);

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
            return 信平级返利;
        }
        if (code == 2) {
            return 级差返利;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}