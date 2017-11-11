package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.dto.DeliveryAddressDTO;
import com.dlsw.cn.common.po.DeliveryAddress;
import com.dlsw.cn.shopping.vo.DeliveryAddressVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryAddressMapper {

    DeliveryAddressDTO deliveryAddressToDeliveryAddressDTO(DeliveryAddress deliveryAddress);

    DeliveryAddress deliveryAddressVoToDeliveryAddress(DeliveryAddressVo deliveryAddress);


    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(List<DeliveryAddress> deliveryAddressList);

    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(Iterable<DeliveryAddress> deliveryAddressList);
}