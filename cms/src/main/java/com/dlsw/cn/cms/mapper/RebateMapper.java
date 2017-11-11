package com.dlsw.cn.cms.mapper;

import com.dlsw.cn.common.converter.ArrayMapper;
import com.dlsw.cn.common.po.Rebate;
import com.dlsw.cn.common.dto.RebateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring",uses=ArrayMapper.class)
public interface RebateMapper {
    @Mappings({
            @Mapping(source = "user.nickname", target = "nickname"),
            @Mapping(source = "order.orderCode", target = "orderCode"),
            @Mapping(source = "order.id", target = "orderId")
    })
    RebateDTO RebateToRebateDTO(Rebate Rebate);

    List<RebateDTO> RebateToRebateDTOList(List<Rebate> RebateList);

    List<RebateDTO> RebateToRebateDTOList(Iterable<Rebate> RebateList);
}