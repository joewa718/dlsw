package com.dlsw.cn.mapper;

import com.dlsw.cn.converter.ArrayMapper;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.dto.RebateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring",uses=ArrayMapper.class)
public interface RebateMapper {
    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "order.orderCode", target = "orderCode")
    })
    RebateDTO RebateToRebateDTO(Rebate Rebate);

    List<RebateDTO> RebateToRebateDTOList(List<Rebate> RebateList);

    List<RebateDTO> RebateToRebateDTOList(Iterable<Rebate> RebateList);
}