package com.dlsw.cn.po;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author zhanwang
 * @create 2017-10-02 10:44
 **/
@MappedSuperclass
@DynamicUpdate
@DynamicInsert
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public abstract class BaseEntity {
}
