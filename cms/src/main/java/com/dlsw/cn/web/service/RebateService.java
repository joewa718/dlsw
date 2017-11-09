package com.dlsw.cn.web.service;

import com.dlsw.cn.web.dto.PageDTO;
import com.dlsw.cn.web.dto.RebateDTO;
import com.dlsw.cn.web.vo.RebateVo;

/**
 * @author zhanwang
 * @create 2017-09-30 15:14
 **/
public interface RebateService {

    PageDTO<RebateDTO> fetchPage(RebateVo rebateVo);

    void setStatus(String ids);
}
