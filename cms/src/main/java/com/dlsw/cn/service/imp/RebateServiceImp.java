package com.dlsw.cn.service.imp;

import com.dlsw.cn.enumerate.RebateStatus;
import com.dlsw.cn.repositories.RebateRepository;
import com.dlsw.cn.service.RebateService;
import com.dlsw.cn.po.Rebate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Override
    public List<Rebate> getRebateList(Rebate rebate) {
        return rebateRepository.findAll((root, cq, cb) -> {
            List<Predicate> list = new ArrayList();
            if(rebate.getRebateStatus() !=null){
                list.add(cb.equal(root.get("rebateStatus").as(String.class), "%"+rebate.getRebateStatus()+"%"));
            }
            if(rebate.getRebateTime() != null){
                list.add(cb.equal(root.get("rebateTime").as(Integer.class), rebate.getRebateTime()));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        });
    }

    @Override
    public void updateRebate(List<Long> ids) {
        ids.forEach(id ->{
            Rebate rebate = rebateRepository.findOne(id);
            rebate.setRebateStatus(RebateStatus.已确认);
            rebateRepository.save(rebate);
        });

    }
}
