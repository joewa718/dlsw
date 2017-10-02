package com.dlsw.cn.vo;/**
 * Created by zhanwa01 on 2017/6/12.
 */

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhanwang
 * @create 2017-06-12 19:22
 **/
public class PageVo {
    @ApiParam(value = "默认值第一页")
    protected int page;
    @ApiParam(value = "默认值每页10条")
    protected int size;
    protected String sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}

