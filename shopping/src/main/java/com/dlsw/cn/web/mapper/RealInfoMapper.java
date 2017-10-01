package com.dlsw.cn.web.mapper;

import com.dlsw.cn.dto.RealInfoDTO;
import com.dlsw.cn.po.RealInfo;
import com.dlsw.cn.web.vo.RealInfoVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RealInfoMapper {

    RealInfoDTO realInfoToRealInfoDTO(RealInfo realInfo);

    RealInfo realInfoToVoRealInfo(RealInfoVo realInfoVo);

    List<RealInfoDTO> realInfoToRealInfoDTOList(List<RealInfo> realInfoList);

    List<RealInfoDTO> realInfoToRealInfoDTOList(Iterable<RealInfo> realInfoList);
}