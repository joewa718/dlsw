package com.dlsw.cn.common.repositories;

import com.dlsw.cn.common.po.Rebate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RebateRepository extends CrudRepository<Rebate, Long>, JpaSpecificationExecutor<Rebate> {
}
