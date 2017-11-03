package com.nielsen.cn.web.service;

import java.io.File;

/**
 * @author zhanwang
 * @create 2017-11-03 0:06
 **/
class Source {
    //测试目标代码：
    public boolean callInternalInstance(String path) {
        File file = new File(path);
        return file.exists();
    }
}