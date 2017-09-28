package com.dlsw.cn.web.mapper;

import com.dlsw.cn.web.dto.DeliveryAddressDTO;
import com.dlsw.cn.po.DeliveryAddress;
import com.dlsw.cn.web.vo.DeliveryAddressVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryAddressMapper {

    DeliveryAddressDTO deliveryAddressToDeliveryAddressDTO(DeliveryAddress deliveryAddress);

    DeliveryAddress deliveryAddressVoToDeliveryAddress(DeliveryAddressVo deliveryAddress);


    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(List<DeliveryAddress> deliveryAddressList);

    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(Iterable<DeliveryAddress> deliveryAddressList);
}