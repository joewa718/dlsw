package com.dlsw.cn.shopping.mapper;

import com.dlsw.cn.common.dto.RealInfoDTO;
import com.dlsw.cn.common.po.RealInfo;
import com.dlsw.cn.shopping.vo.RealInfoVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RealInfoMapper {

    RealInfoDTO realInfoToRealInfoDTO(RealInfo realInfo);

    RealInfo realInfoToVoRealInfo(RealInfoVo realInfoVo);

    List<RealInfoDTO> realInfoToRealInfoDTOList(List<RealInfo> realInfoList);

    List<RealInfoDTO> realInfoToRealInfoDTOList(Iterable<RealInfo> realInfoList);
}