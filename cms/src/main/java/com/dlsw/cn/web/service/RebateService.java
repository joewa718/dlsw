package com.dlsw.cn.web.service;

import com.dlsw.cn.dto.PageDTO;
import com.dlsw.cn.dto.RebateDTO;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.vo.RebateVo;

import java.util.List;

/**
 * @author zhanwang
 * @create 2017-09-30 15:14
 **/
public interface RebateService {

    PageDTO<RebateDTO> fetchPage(RebateVo rebateVo);

    void setStatus(String ids);
}
