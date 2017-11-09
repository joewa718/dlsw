package com.dlsw.cn.web.repositories;

import com.dlsw.cn.web.po.PromoteLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromoteLogRepository extends CrudRepository<PromoteLog, Long>, JpaSpecificationExecutor<PromoteLog> {

}
