package com.dlsw.cn.web.enumerate;

import com.dlsw.cn.web.po.User;

public enum DirectorLevel {

    无("无", 0, 0), 仁("仁", 2, 20), 义("义", 4, 24), 礼("礼", 6, 28), 智("智", 8, 32), 信("信", 10, 36);

    DirectorLevel(String name, int code, int percent) {
        this.name = name;
        this.code = code;
        this.percent = percent;
    }

    private int code;
    private String name;
    private int percent;

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
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
            return 礼;
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

    public static DirectorLevel getDirectorLevel(User user) {
        long count = user.getLower().stream().filter(u -> u.getRoleType() == RoleType.高级合伙人).count();
        if (count >= 2 && count <= 3) {
            return DirectorLevel.仁;
        } else if (count >= 4 && count <= 5) {
            return DirectorLevel.义;
        } else if (count >= 6 && count <= 7) {
            return DirectorLevel.礼;
        } else if (count >= 8 && count <= 9) {
            return DirectorLevel.智;
        } else if (count >= 10) {
            return DirectorLevel.信;
        }
        return DirectorLevel.无;
    }
}