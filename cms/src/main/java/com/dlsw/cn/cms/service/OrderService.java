package com.dlsw.cn.cms.service;

import com.dlsw.cn.common.dto.OrderDTO;
import com.dlsw.cn.common.dto.PageDTO;
import com.dlsw.cn.common.dto.RebateDTO;
import com.dlsw.cn.cms.vo.OrderVo;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {

    /**
     * 分页
     * @param rebateVo
     * @return
     */
    PageDTO<RebateDTO> fetchPage(OrderVo rebateVo);

    /**
     * 获得订单详细信息
     *
     * @param orderId
     * @return
     */
    OrderDTO getOrder(long orderId);

    /**
     * 确认订单
     * @param orderId
     * @return
     */
    OrderDTO sureOrder(long orderId);

}
