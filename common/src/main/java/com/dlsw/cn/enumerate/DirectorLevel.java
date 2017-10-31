package com.dlsw.cn.enumerate;

public enum DirectorLevel {
    无("无", 0),仁("仁", 2), 义("义", 4), 理("理", 6), 智("智", 8), 信("信", 10);
    DirectorLevel(String name, int code) {
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

    public static DirectorLevel fromCode(int code) {
        if (code == 0) {
            return 无;
        }
        if (code == 2) {
            return 仁;
        }
        if (code == 4) {
            return 义;
        }
        if (code == 6) {
            return 理;
        }
        if (code == 8) {
            return 智;
        }
        if (code == 10) {
            return 信;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}