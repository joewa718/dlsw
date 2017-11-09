package com.dlsw.cn.repositories;

import com.dlsw.cn.po.PromoteLog;
import com.dlsw.cn.po.Rebate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromoteLogRepository extends CrudRepository<PromoteLog, Long>, JpaSpecificationExecutor<PromoteLog> {
    
}
