package com.dlsw.cn.cms.service;

import com.dlsw.cn.common.dto.PageDTO;
import com.dlsw.cn.common.dto.RebateDTO;
import com.dlsw.cn.cms.vo.RebateVo;

/**
 * @author zhanwang
 * @create 2017-09-30 15:14
 **/
public interface RebateService {

    PageDTO<RebateDTO> fetchPage(RebateVo rebateVo);

    void setStatus(String ids);
}
