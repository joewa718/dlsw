package com.dlsw.cn.service.imp;

import com.dlsw.cn.dto.RebateDTO;
import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.mapper.RebateMapper;
import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.repositories.RebateRepository;
import com.dlsw.cn.repositories.UserRepository;
import com.dlsw.cn.service.RebateService;
import com.dlsw.cn.vo.RebateVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-09-30 15:15
 **/
@Service
public class RebateServiceImp implements RebateService{

    @Autowired
    RebateRepository rebateRepository;
    @Autowired
    RebateMapper rebateMapper;
    @Override
    public List<RebateDTO> getRebateList(RebateVo rebateVo) {
        List<Rebate> rebateList = rebateRepository.findAll((root, cq, cb) -> {
            List<Predicate> list = new ArrayList();
            if(!StringUtils.isBlank(rebateVo.getOrderCode())){
                list.add(cb.equal(root.get("order").get("orderCode").as(String.class),rebateVo.getOrderCode()));
            }
            if(!StringUtils.isBlank(rebateVo.getYearMonth())){
                list.add(cb.like(root.get("rebateTime").as(String.class), "%"+rebateVo.getYearMonth()+"%"));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        });
        return rebateMapper.RebateToRebateDTOList(rebateList);
    }

    @Override
    public void updateRebate(String ids) {
        String[] id_list = ids.split(",");
        for(String id : id_list){
            Rebate rebate = rebateRepository.findOne(Long.parseLong(id));
            rebate.setRebateStatus(RebateStatus.已确认);
            rebateRepository.save(rebate);
        }
    }
}
