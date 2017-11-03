package com.dlsw.cn.web.service.imp;

import com.dlsw.cn.dto.PageDTO;
import com.dlsw.cn.dto.RebateDTO;
import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.mapper.RebateMapper;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.repositories.RebateRepository;
import com.dlsw.cn.web.service.BaseService;
import com.dlsw.cn.web.service.RebateService;
import com.dlsw.cn.vo.RebateVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-09-30 15:15
 **/
@Service
public class RebateServiceImp extends BaseService implements RebateService {

    @Autowired
    RebateRepository rebateRepository;
    @Autowired
    RebateMapper rebateMapper;

    @Override
    public PageDTO<RebateDTO> fetchPage(RebateVo rebateVo) {
        Page page = rebateRepository.findAll((root, cq, cb) -> {
            List<Predicate> list = new ArrayList();
            if(!StringUtils.isBlank(rebateVo.getOrderCode())){
                list.add(cb.equal(root.get("order").get("orderCode").as(String.class),rebateVo.getOrderCode()));
            }
            if(!StringUtils.isBlank(rebateVo.getSearchDate())){
                list.add(cb.like(root.get("rebateTime").as(String.class), "%"+rebateVo.getSearchDate()+"%"));
            }
            if(rebateVo.getRebateStatus() != null){
                list.add(cb.equal(root.get("rebateStatus").as(RebateStatus.class), rebateVo.getRebateStatus()));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        }, super.getPageRequest(rebateVo));

        PageDTO<RebateDTO> rebateDTOPageDTO = new PageDTO<>();
        rebateDTOPageDTO.setTotalElements(page.getTotalElements());
        rebateDTOPageDTO.setContent(rebateMapper.RebateToRebateDTOList(page.getContent()));
        return rebateDTOPageDTO;
    }


    @Override
    public void setStatus(String ids) {
        String[] id_list = ids.split(",");
        for(String id : id_list){
            Rebate rebate = rebateRepository.findOne(Long.parseLong(id));
            rebate.setRebateStatus(RebateStatus.已返利);
            rebateRepository.save(rebate);
        }
    }
}
