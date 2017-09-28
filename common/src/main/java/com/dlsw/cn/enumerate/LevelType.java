package com.dlsw.cn.enumerate;

public enum LevelType {
    第一代("第一代", 1, 8), 第三代("第三代", 3, 12);

    LevelType(String name, int code, int reward) {
        this.name = name;
        this.code = code;
        this.reward = reward;
    }

    private int code;
    private String name;
    private int reward;

    public static LevelType fromCode(int code) {
        if (code == 1) {
            return 第一代;
        }
        if (code == 3) {
            return 第三代;
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

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}